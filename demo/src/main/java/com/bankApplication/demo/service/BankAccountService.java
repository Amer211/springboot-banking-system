package com.bankApplication.demo.service;

import com.bankApplication.demo.advice.AccountNotFoundException;
import com.bankApplication.demo.advice.InsufficientFundsException;
import com.bankApplication.demo.dto.AccountResponse;
import com.bankApplication.demo.dto.CreateAccountRequest;
import com.bankApplication.demo.dto.TransferResponse;
import com.bankApplication.demo.model.BankAccount;
import com.bankApplication.demo.model.Transaction;
import com.bankApplication.demo.model.TransactionType;
import com.bankApplication.demo.model.User;
import com.bankApplication.demo.repository.BankAccountRepository;

import com.bankApplication.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankAccountService {


    private final UserRepository userRepository;

    private final BankAccountRepository bankAccountRepository;
    private final TransactionService transactionService;






    // ******************** create bank account ***********************


    @Transactional  // import from Spring not Jakarta
    public AccountResponse createBankAccount(CreateAccountRequest request){

        if(request.getBalance().compareTo(BigDecimal.ZERO)<0){
            log.warn("Balance can't be negative");
            throw new IllegalArgumentException("Balance can't be negative(service layer)");
        }

        //check if an account with the same number already
        //exists in the database.

        BankAccount account = new BankAccount(request.getBalance(), request.getAccountType());


        BankAccount savedAccount =
                bankAccountRepository.save(account);

        String accountNumber;
        do {
            accountNumber = generateAccount();
        } while (isAccountExists(accountNumber));

        account.setAccountNumber(accountNumber);
        User savedUser = userRepository.findById(request.getUserId())
                        .orElseThrow(()-> {
                            log.warn("User with id {} not found", request.getUserId());
                            return new IllegalArgumentException("User not found");
                        });
        account.setUser(savedUser);

        log.info("creating bank account Number : {} with balance: {}",
                account.getAccountNumber(),request.getBalance());

        return AccountResponse.builder()
                .accountId(savedAccount.getId())
                .accountNumber(savedAccount.getAccountNumber())
                .balance(savedAccount.getBalance())
                .accountType(savedAccount.getAccountType())
                .build();
    }






    private String generateAccount() {
        String bankCode = "3223";
        StringBuilder accountNumber = new StringBuilder(bankCode);
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }



    public boolean isAccountExists(String accountNumber){
        boolean exists = bankAccountRepository.
                existsByAccountNumber(accountNumber);
        if(exists){
            log.warn("Account number exists");
        }
        return exists;

    }





    public List<AccountResponse> getAllAccounts(){
        log.info("Retrieving all accounts..");
        return bankAccountRepository.findAll()
                .stream()
                .map(account -> AccountResponse.builder()
                        .accountId(account.getId())
                        .accountNumber(account.getAccountNumber())
                        .balance(account.getBalance())
                        .accountType(account.getAccountType())
                        .transactions(account.getTransactions())
                        .build())
                .toList();
    }






    public BigDecimal getBalanceById(int id) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(()-> new AccountNotFoundException(id));
        return account.getBalance();
    }

    public AccountResponse getAccountById(int id) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(()-> new AccountNotFoundException(id));
        return AccountResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .transactions(account.getTransactions())
                .build();
    }



    // ******************** deposit **********************


    @Transactional    // import from Spring not Jakarta
    public BigDecimal deposit(int id, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(()-> {

                        log.warn("No account found");
                        return new AccountNotFoundException(id);
                });

        // record the transaction first :

        Transaction transaction = transactionService
                .createTransaction(account, amount,TransactionType.DEPOSIT);

        // validation:

        if(amount.compareTo(BigDecimal.ZERO)<=0){
            log.warn("Amount must be positive");
            transactionService.markFailed(transaction.getTransactionId());
            throw new IllegalArgumentException("Amount must be positive");

        }



        account.deposit(amount);
        bankAccountRepository.save(account);

        transactionService.markCompleted(transaction.getTransactionId());




        log.info("$ {} has been deposited into the account number {}",amount,account.getId());
        return account.getBalance();
    }




    //******************** withdraw ***************

    @Transactional
    public CompletableFuture<BigDecimal> withdraw(int id, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(()-> {
                    log.warn("No account found ");
                    return new AccountNotFoundException(id);
                });


        // should be placed first to record all transactions
        //including failed ones

        Transaction transaction = transactionService.createTransaction(
                account,amount,TransactionType.WITHDRAW);


        //then validation next :

        if(amount.compareTo(account.getBalance())>0){
            transactionService.markFailed(transaction.getTransactionId());
            log.warn("Failed to withdraw {}", transaction.getAmount());
            throw new InsufficientFundsException();
        }




        try {
            account.withdraw(amount);
            bankAccountRepository.save(account);


            transactionService.markCompleted(transaction.getTransactionId());

            log.info("{} has been withdrawn from account number {}",
                    amount,account.getId());

            return CompletableFuture.completedFuture(account.getBalance());

        }catch (Exception e){
            transactionService.markFailed(transaction.getTransactionId());
            throw e;
        }





    }

    public List<AccountResponse> getAccountsByUserId(int userId) {
        return bankAccountRepository.findByUser_UserId(userId)
                .stream()
                .map(account -> AccountResponse.builder()
                        .accountId(account.getId())
                        .accountNumber(account.getAccountNumber())
                        .balance(account.getBalance())
                        .accountType(account.getAccountType())
                        .build())
                .toList();
    }




    @Transactional
    public TransferResponse transfer(String from, String to, BigDecimal amount){
        BankAccount sourceAccount = bankAccountRepository
                .findByAccountNumber(from)
                .orElseThrow(()-> {
                    log.warn("Source account not found");
                    return new AccountNotFoundException();
                });

        BankAccount destinationAccount = bankAccountRepository
                .findByAccountNumber(to)
                .orElseThrow(()-> {
                    log.warn("destination account not found");
                    return new AccountNotFoundException();
                });

        Transaction withdrawTransaction = transactionService
                .createTransaction(sourceAccount,
                        amount,
                        TransactionType.TRANSFER);

        Transaction depositTransaction = transactionService
                .createTransaction(destinationAccount
                        ,amount,TransactionType.TRANSFER);


        sourceAccount.transfer(destinationAccount,amount);


        transactionService.markCompleted(depositTransaction.getTransactionId());

        transactionService.markCompleted(withdrawTransaction.getTransactionId());


        log.info("Transferring ${} from account number {} to account number {}",
                amount,
                sourceAccount.getAccountNumber(),
                destinationAccount.getAccountNumber());




        TransferResponse response = new TransferResponse();
        response.setFromAccountNumber(sourceAccount.getAccountNumber());
        response.setToAccountNumber(destinationAccount.getAccountNumber());
        response.setAmount(amount);
        response.setStatus("SUCCESS");
        response.setMessage("Transfer successful");

        return response;




    }










}

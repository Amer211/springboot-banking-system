package com.bankApplication.demo.service;

import com.bankApplication.demo.advice.AccountNotFoundException;
import com.bankApplication.demo.advice.InsufficientFundsException;
import com.bankApplication.demo.dto.AccountResponse;
import com.bankApplication.demo.dto.CreateAccountRequest;
import com.bankApplication.demo.model.BankAccount;
import com.bankApplication.demo.model.Transaction;
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

        if(request.getBalance()<0){
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
        String bankcode = "3223";
        StringBuilder accountNumber = new StringBuilder(bankcode);
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





    public List<BankAccount> getAllAccount(){
        log.info("Retrieving all accounts..");
        return bankAccountRepository.findAll();
    }






    public Double getBalanceById(int id) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(()-> new AccountNotFoundException(id));
        return account.getBalance();
    }




    // ******************** deposit **********************


    @Transactional    // import from Spring not Jakarta
    public double deposit(int id, double amount) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(()-> {

                        log.warn("No account found");
                        return new AccountNotFoundException(id);
                });

        // record the transaction first :

        Transaction transaction = transactionService.createTransaction(account,
                amount);

        // validation:

        if(amount<=0){
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
    public CompletableFuture<Double> withdraw(int id, Double amount) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(()-> {
                    log.warn("No account found ");
                    return new AccountNotFoundException(id);
                });


        // should be placed first to record all transactions
        //including failed ones

        Transaction transaction = transactionService.createTransaction(
                account,amount);


        //then validation next :

        if(amount>account.getBalance()){
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
}

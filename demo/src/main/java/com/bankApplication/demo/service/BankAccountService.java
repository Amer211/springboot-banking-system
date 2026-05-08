package com.bankApplication.demo.service;

import com.bankApplication.demo.model.BankAccount;
import com.bankApplication.demo.model.Transaction;
import com.bankApplication.demo.repository.BankAccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class BankAccountService {



    private static final Logger log = LoggerFactory.getLogger(BankAccountService.class);
    private final BankAccountRepository bankAccountRepository;
    private final TransactionService transactionService;


    public BankAccountService(BankAccountRepository bankAccountRepository, TransactionService transactionService) {
        this.bankAccountRepository = bankAccountRepository;

        this.transactionService = transactionService;
    }

    @Transactional  // import from Spring not Jakarta
    public BankAccount createBankAccount(double balance){

        if(balance<0){
            throw new IllegalArgumentException("Balance can't be negative");
        }

        //check if an account with the same number already
        //exists in the database.

        BankAccount account = new BankAccount(balance);
        String accountNumber;
        do {
            accountNumber = generateAccount();
        } while (isAccountExists(accountNumber));

        account.setAccountNumber(accountNumber);

        log.info("creating bank account Number : {} with balance: {}",
                account.getAccountNumber(),balance);

        return bankAccountRepository.save(account);
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
                .orElseThrow(()-> new RuntimeException("Account not found"));
        return account.getBalance();
    }




    // ******************** deposit **********************


    @Transactional    // import from Spring not Jakarta
    public double deposit(int id, double amount) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(()-> {

                        log.warn("No account found");
                        return new RuntimeException("Account not found");
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
                    return new RuntimeException("Account not found");
                });


        // should be placed first to record all transactions
        //including failed ones

        Transaction transaction = transactionService.createTransaction(
                account,amount);


        //then validation next :

        if(amount>account.getBalance()){
            transactionService.markFailed(transaction.getTransactionId());
            log.warn("Failed to withdraw {}", transaction.getAmount());
            throw new IllegalArgumentException("Insufficient funds");
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

}

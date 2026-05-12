package com.bankApplication.demo.controller;

import com.bankApplication.demo.dto.*;
import com.bankApplication.demo.model.BankAccount;
import com.bankApplication.demo.service.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class BankAccountController {

    private final BankAccountService bankAccountService;


    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }


    // **********************create**************************

    @PostMapping("/create")
    public ResponseEntity<BankAccount> createBankAccount
            (@Valid @RequestBody CreateAccountRequest request){
        BankAccount account = bankAccountService.createBankAccount
                (request.getBalance(), request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(account);
    }






    @GetMapping("getAll")
    public ResponseEntity<List<BankAccount>> getAllAccounts(){
        return ResponseEntity.ok(bankAccountService.getAllAccount());
    }



    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit
            (@RequestBody DepositRequest request){
        DepositResponse response = new DepositResponse("Success",
                bankAccountService.deposit(request.getId(),
                        request.getAmount()));

           return ResponseEntity.ok(response);
    }







    @PostMapping("/withdraw")
    public ResponseEntity<CompletableFuture<WithdrawResponse>>withdraw
            (@RequestBody WithdrawRequest request){

        CompletableFuture<WithdrawResponse> response = bankAccountService.withdraw(
                request.getId(), request.getAmount()
        )
                .thenApply(newBalance -> new WithdrawResponse("success",
                        newBalance));

        return ResponseEntity
                .ok(response);
    }



    @GetMapping("/balance/{id}")
    public ResponseEntity<Double> getBalance(@PathVariable int id){
        return ResponseEntity.ok(bankAccountService.getBalanceById(id));
    }






}

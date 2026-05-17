package com.bankApplication.demo.controller;

import com.bankApplication.demo.dto.*;
import com.bankApplication.demo.model.BankAccount;
import com.bankApplication.demo.service.BankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;





    // **********************create**************************

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createBankAccount
            (@Valid @RequestBody CreateAccountRequest request){

        AccountResponse response = bankAccountService.createBankAccount(request);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
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
                        new BigDecimal("newBalance")));

        return ResponseEntity
                .ok(response);
    }



    @GetMapping("/balance/{id}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable int id){
        return ResponseEntity.ok(bankAccountService.getBalanceById(id));
    }






}

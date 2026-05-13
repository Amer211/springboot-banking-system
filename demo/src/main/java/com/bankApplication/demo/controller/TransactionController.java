package com.bankApplication.demo.controller;

import com.bankApplication.demo.model.Transaction;
import com.bankApplication.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;



    @GetMapping("/getAll")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable long id) {
        return transactionService.getTransactionById(id);
    }


    @GetMapping("/reference/{referenceId}")
    public Transaction getTransactionByReferenceId(@PathVariable String referenceId) {
        return transactionService.getTransactionByReferenceId(referenceId);
    }






}

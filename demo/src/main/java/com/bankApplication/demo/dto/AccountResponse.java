package com.bankApplication.demo.dto;

import com.bankApplication.demo.model.AccountType;
import com.bankApplication.demo.model.Transaction;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@JsonPropertyOrder({"accountId", "accountNumber", "balance", "accountType"})
public class AccountResponse {

    private Integer accountId;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private List<Transaction> transactions;




}

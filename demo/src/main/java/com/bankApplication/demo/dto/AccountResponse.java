package com.bankApplication.demo.dto;

import com.bankApplication.demo.model.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

    private Integer accountId;
    private String accountNumber;
    private double balance;
    private AccountType accountType;




}

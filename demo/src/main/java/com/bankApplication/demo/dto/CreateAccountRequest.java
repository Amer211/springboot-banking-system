package com.bankApplication.demo.dto;

import com.bankApplication.demo.model.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateAccountRequest {

    @PositiveOrZero(message = "balance can't be negative")
    private BigDecimal balance;

    @NotNull(message = "userId is required")
    private Integer userId;

    @NotNull(message = "Account type can't be null")
    private AccountType accountType;
}

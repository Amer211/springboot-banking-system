package com.bankApplication.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferResponse {

        private String fromAccountNumber;
        private String toAccountNumber;
        private BigDecimal amount;
        private String status;
        private String message;
}

package com.bankApplication.demo.dto;

import com.bankApplication.demo.model.BankAccount;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonPropertyOrder({"userId", "firstName", "lastName", "email", "accounts"})
public class UserResponse {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AccountResponse> accounts;






}

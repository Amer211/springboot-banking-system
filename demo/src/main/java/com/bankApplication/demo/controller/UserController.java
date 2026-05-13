package com.bankApplication.demo.controller;

import com.bankApplication.demo.dto.AccountResponse;
import com.bankApplication.demo.dto.UserRequest;
import com.bankApplication.demo.dto.UserResponse;
import com.bankApplication.demo.model.AccountType;
import com.bankApplication.demo.model.BankAccount;
import com.bankApplication.demo.model.User;
import com.bankApplication.demo.service.BankAccountService;
import com.bankApplication.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final BankAccountService bankAccountService;







    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request){
        UserResponse response = userService.saveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }



    @GetMapping("/getAll")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById
            (@PathVariable int userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }




    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountResponse>> getUserAccounts
            (@PathVariable int userId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(bankAccountService.getAccountsByUserId(userId));
    }




}

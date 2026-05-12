package com.bankApplication.demo.controller;

import com.bankApplication.demo.model.BankAccount;
import com.bankApplication.demo.model.User;
import com.bankApplication.demo.service.BankAccountService;
import com.bankApplication.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;
    private final BankAccountService bankAccountService;





    public UserController(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }






    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1 = userService.saveUser(user);

        BankAccount account=bankAccountService.createBankAccount(0.0, user1.getUserId());

        user1.setAccounts(List.of(account));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user1);
    }



    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }





}

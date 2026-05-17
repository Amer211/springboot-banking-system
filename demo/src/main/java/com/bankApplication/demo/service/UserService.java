package com.bankApplication.demo.service;

import com.bankApplication.demo.dto.AccountResponse;
import com.bankApplication.demo.dto.CreateAccountRequest;
import com.bankApplication.demo.dto.UserRequest;
import com.bankApplication.demo.dto.UserResponse;
import com.bankApplication.demo.model.User;
import com.bankApplication.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final BankAccountService bankAccountService;

    private final Logger log = LoggerFactory.getLogger(UserService.class);




    // ********************** create user **************************

    @Transactional
    public UserResponse saveUser(UserRequest request){

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        User savedUser = userRepository.save(user);
        log.info("user with id: {} saved successfully.", savedUser.getUserId());


        AccountResponse account = bankAccountService.createBankAccount(
                new CreateAccountRequest(new BigDecimal("0.0"),
                        savedUser.getUserId(),
                        request.getAccountType())
        );

        return UserResponse.builder()
                .userId(savedUser.getUserId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .accounts(List.of(account))
                .build();





    }


    public List<UserResponse> getAllUsers(){
        log.info("fetching all users.");
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }


    public UserResponse getUserById(int userId) {
        log.info("fetching user with id: {}.", userId);
        User user= userRepository.findById(userId)
               .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
               return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accounts(user.getAccounts().stream()
                        .map(account -> AccountResponse.builder()
                                .accountId(account.getId())
                                .accountNumber(account.getAccountNumber())
                                .balance(account.getBalance())
                                .accountType(account.getAccountType())
                                .build())
                        .toList())
                .build();
    }
}

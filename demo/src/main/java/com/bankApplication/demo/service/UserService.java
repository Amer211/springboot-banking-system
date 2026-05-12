package com.bankApplication.demo.service;

import com.bankApplication.demo.model.BankAccount;
import com.bankApplication.demo.model.User;
import com.bankApplication.demo.repository.UserRepository;
import jakarta.validation.constraints.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;


    public final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public User saveUser(User user){
        log.info("saving user with id: {}.", user.getUserId());

        return userRepository.save(user);
    }


    public List<User> getAllUsers(){
        log.info("fetching all users.");
        return userRepository.findAll();
    }


    public User getUserById(int userId) {
        log.info("fetching user with id: {}.", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }
}

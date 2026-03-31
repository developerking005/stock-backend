package com.library.seatmanager.service;

import com.library.seatmanager.entity.User;
import com.library.seatmanager.repo.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public Double updateWallet(String phone, Double amount){

        User user = repo.findByPhone(phone)
                .orElseThrow(()-> new RuntimeException("User not found"));

        user.setWalletBalance(user.getWalletBalance() + amount);

        repo.save(user);

        return user.getWalletBalance();
    }

    public User getUser(String phone){
        Optional<User> usr = repo.findByPhone(phone);
        return usr.get();
    }
}

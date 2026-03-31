package com.library.seatmanager.service;

import com.library.seatmanager.config.JwtUtil;
import com.library.seatmanager.dto.LoginRequest;
import com.library.seatmanager.dto.SignupRequest;
import com.library.seatmanager.entity.User;
import com.library.seatmanager.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository repo;


    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String,Object> signup(SignupRequest req){

        if(repo.findByPhone(req.getPhone()).isPresent())
            throw new RuntimeException("Phone already exists");

        User user = User.builder()
                .name(req.getName())
                .phone(req.getPhone())
                .password(encoder.encode(req.getPassword()))
                .wallet(0.0)
                .role("ADMIN")
                .createdAt(LocalDateTime.now())
                .build();

        repo.save(user);

        System.out.println("name : " + req.getName());

        System.out.println("user name  : " + user.getName());

        String token = jwtUtil.generateToken(req.getPhone());

        Map<String,Object> response = new HashMap<>();
        response.put("token", token);
        response.put("name", req.getName());
        response.put("wallet", 0);

        System.out.println(token + " token" + "name : " + user.getName());
        return response;
    }


    public Map<String,Object> login(LoginRequest req){

        User user = repo.findByPhone(req.getPhone())
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(!encoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid password");

        String token = jwtUtil.generateToken(user.getPhone());

        Map<String,Object> response = new HashMap<>();
        response.put("token", token);
        response.put("name", user.getName());
        response.put("wallet", user.getWalletBalance());
        return response;
    }
}

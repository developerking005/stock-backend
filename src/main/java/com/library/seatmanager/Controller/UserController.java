package com.library.seatmanager.Controller;

import com.library.seatmanager.config.JwtUtil;
import com.library.seatmanager.dto.WalletUpdateRequest;
import com.library.seatmanager.entity.User;
import com.library.seatmanager.repo.UserRepository;
import com.library.seatmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private final UserService service;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepo;

    public UserController(UserService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/user/wallet/update")
    public Map<String,Object> updateWallet(
            @RequestBody WalletUpdateRequest req,
            @RequestHeader("Authorization") String authHeader){

        // Remove "Bearer "
        String token = authHeader.substring(7);

        // extract phone from token
        String phone = jwtUtil.extractUsername(token);

        Double newBalance = service.updateWallet(phone, req.getAmount());

        return Map.of(
                "wallet", newBalance,
                "status", "updated"
        );
    }

    @GetMapping("/user")
    public User getUser(@RequestHeader("Authorization") String authHeader){
        // Remove "Bearer "
        String token = authHeader.substring(7);
        // extract phone from token
        String phone = jwtUtil.extractUsername(token);
        return service.getUser(phone);
    }
}
package com.library.seatmanager.Controller;

import com.library.seatmanager.dto.LoginRequest;
import com.library.seatmanager.dto.SignupRequest;
import com.library.seatmanager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req){
        return ResponseEntity.ok(service.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        return ResponseEntity.ok(service.login(req));
    }
}

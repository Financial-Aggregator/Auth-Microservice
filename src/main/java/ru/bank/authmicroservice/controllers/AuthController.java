package ru.bank.authmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.bank.authmicroservice.dtos.AuthResponseDto;
import ru.bank.authmicroservice.dtos.LoginRequestDto;
import ru.bank.authmicroservice.dtos.RegisterRequestDto;
import ru.bank.authmicroservice.services.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}

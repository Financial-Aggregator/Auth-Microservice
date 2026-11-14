package ru.bank.authmicroservice.services;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.bank.authmicroservice.dtos.AuthResponseDto;
import ru.bank.authmicroservice.dtos.LoginRequestDto;
import ru.bank.authmicroservice.dtos.RegisterRequestDto;
import ru.bank.authmicroservice.entitys.UserCredential;
import ru.bank.authmicroservice.entitys.UserCredentialDetails;
import ru.bank.authmicroservice.repositories.UserCredentialRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserCredentialRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Lazy
    @GrpcClient("user-service")
    private user.UserServiceGrpc.UserServiceBlockingStub userStub;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        user.CreateUserRequest grpcRequest = user.CreateUserRequest.newBuilder()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setMiddleName(request.getMiddleName())
                .setInn(request.getInn())
                .setIndex(request.getIndex())
                .setRegAddress(request.getRegAddress())
                .setFactAddress(request.getFactAddress())
                .setPhoneNumber(request.getPhoneNumber())
                .setEmail(request.getEmail())
                .setPassportSeries(request.getPassportSeries())
                .setPassportNumber(request.getPassportNumber())
                .build();

        user.UserResponse grpcResponse;

        try {
            grpcResponse = userStub.createUserProfile(grpcRequest);
        } catch (StatusRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "User Service временно недоступен. Попробуйте позже.", e);
        }

        UUID userId = UUID.fromString(grpcResponse.getUserId());

        UserCredential credential = UserCredential.builder()
                .userId(userId)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        repository.save(credential);

        String token = jwtService.generateToken(credential.getUsername(), userId);
        return new AuthResponseDto(token, userId);
    }

    public AuthResponseDto login(LoginRequestDto request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );

        UserCredentialDetails userDetails = (UserCredentialDetails) auth.getPrincipal();

        String token = jwtService.generateToken(userDetails.getUsername(), userDetails.getUserId());
        return new AuthResponseDto(token, userDetails.getUserId());
    }
}

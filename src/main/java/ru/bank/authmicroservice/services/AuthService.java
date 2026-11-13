package ru.bank.authmicroservice.services;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    public String register(RegisterRequest request) {
        CreateUserRequest grpcRequest = CreateUserRequest.newBuilder()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .build();

        UserResponse grpcResponse = userStub.createUserProfile(grpcRequest);
        UUID userId = UUID.fromString(grpcResponse.getUserId());

        UserCredential credential = UserCredential.builder()
                .userId(userId)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();
        repository.save(credential);

        return jwtService.generateToken(credential.getUsername(), userId);
    }

    public String login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserCredentialDetails userDetails = (UserCredentialDetails) auth.getPrincipal();
        return jwtService.generateToken(userDetails.getUsername(), userDetails.getUserId());
    }
}

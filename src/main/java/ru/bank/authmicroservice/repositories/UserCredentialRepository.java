package ru.bank.authmicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bank.authmicroservice.entitys.UserCredential;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialRepository extends JpaRepository<UserCredential, UUID> {
    Optional<UserCredential> findByUsername(String username);
}

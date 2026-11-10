package ru.bank.authmicroservice.entitys;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullUnmarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NullUnmarked
@RequiredArgsConstructor
public class UserCredentialDetails implements UserDetails {
    private final UserCredential userCredential;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return userCredential.getPassword();
    }

    @Override
    public String getUsername() {
        return userCredential.getUsername();
    }

    public UUID getUserId() {
        return userCredential.getUserId();
    }

    @Override
    public String toString() {
        return "UserCredentialDetails{" +
                "userCredential=" + userCredential +
                '}';
    }
}

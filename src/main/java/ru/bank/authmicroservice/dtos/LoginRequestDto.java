package ru.bank.authmicroservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}

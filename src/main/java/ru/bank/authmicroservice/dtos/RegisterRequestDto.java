package ru.bank.authmicroservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String middleName;

    @NotBlank
    private String inn;

    @NotBlank
    private String index;

    @NotBlank
    private String regAddress;

    @NotBlank
    private String factAddress;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String passportSeries;

    @NotBlank
    private String passportNumber;

    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank @Size(min = 6)
    private String password;
}

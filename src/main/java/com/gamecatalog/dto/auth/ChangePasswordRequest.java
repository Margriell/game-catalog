package com.gamecatalog.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank(message = "Stare hasło jest wymagane")
    private String oldPassword;

    @NotBlank(message = "Nowe hasło jest wymagane")
    @Size(min = 6, message = "Nowe hasło musi mieć co najmniej 6 znaków")
    private String newPassword;
}
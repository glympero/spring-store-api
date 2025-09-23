package com.glympero.store.dtos;

import com.glympero.store.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticateUserRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Lowercase(message = "Email must be lowercase")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 25, message = "Password must be between 8 and 25 characters")
    private String password;
}

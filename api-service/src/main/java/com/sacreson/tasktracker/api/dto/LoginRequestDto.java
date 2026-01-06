package com.sacreson.tasktracker.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "Name cannot be empty")
    private String username;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}

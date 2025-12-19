package com.sacreson.tasktracker.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectDto {

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, max = 255, message = "Name length must be between 3 and 255")
    private String name;
}

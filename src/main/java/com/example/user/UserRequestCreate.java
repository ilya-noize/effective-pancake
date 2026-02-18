package com.example.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestCreate(
        @NotBlank
        @Size(min=3, max=32)
        String name,

        @NotBlank
        @Size(min=5, max=255)
        String email
){
}

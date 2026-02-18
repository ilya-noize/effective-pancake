package com.example.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record UserSearchFilter(
        @NotBlank
        String name,

        @NotBlank
        String email,

        @Min(0)
        @DefaultValue("0")
        Integer pageNumber,

        @Min(3)
        @Max(100)
        @DefaultValue("3")
        Integer pageSize,

        @DefaultValue("true")
        Boolean ascending,

        @NotBlank
        @DefaultValue("name")
        String sortBy // for example: "id", "name", "email";
) {

}

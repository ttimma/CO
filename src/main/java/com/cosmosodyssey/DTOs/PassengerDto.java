package com.cosmosodyssey.DTOs;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PassengerDto {
    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;
}

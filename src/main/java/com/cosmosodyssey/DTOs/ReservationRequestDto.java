package com.cosmosodyssey.DTOs;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class ReservationRequestDto {

    // New field for the price list identifier.
    private Long priceListId;

    // List of passengers (each with firstName and lastName)
    @NotEmpty(message = "At least one passenger must be provided")
    private List<PassengerDto> passengers;

    // List of route IDs.
    @NotEmpty(message = "At least one route must be selected")
    private List<Long> routeIds;
}

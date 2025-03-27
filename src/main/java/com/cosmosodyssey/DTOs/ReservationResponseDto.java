package com.cosmosodyssey.DTOs;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationResponseDto {
    private Long id;
    private List<PassengerDto> passengers;
    private String routeIds;
    private double totalPrice;
    private long totalTravelTime;
    private String companyNames;
    private LocalDateTime createdAt;
}

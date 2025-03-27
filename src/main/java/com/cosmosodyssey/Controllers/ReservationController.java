package com.cosmosodyssey.Controllers;

import com.cosmosodyssey.DTOs.PassengerDto;
import com.cosmosodyssey.DTOs.ReservationRequestDto;
import com.cosmosodyssey.DTOs.ReservationResponseDto;
import com.cosmosodyssey.Entities.Reservation;
import com.cosmosodyssey.Services.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationResponseDto createReservation(@RequestBody ReservationRequestDto reservationRequest) {
        Reservation reservation = reservationService.createReservation(reservationRequest);
        return mapToReservationResponseDto(reservation);
    }

    private ReservationResponseDto mapToReservationResponseDto(Reservation reservation) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setId(reservation.getId());
        dto.setRouteIds(reservation.getRouteIds());
        dto.setCompanyNames(reservation.getCompanyNames());
        dto.setTotalPrice(reservation.getTotalPrice());
        dto.setTotalTravelTime(reservation.getTotalTravelTime());
        dto.setCreatedAt(reservation.getCreatedAt());

        // Map all passengers
        if (reservation.getPassengers() != null) {
            List<PassengerDto> passengerDtos = reservation.getPassengers().stream().map(p -> {
                PassengerDto pd = new PassengerDto();
                pd.setFirstName(p.getFirstName());
                pd.setLastName(p.getLastName());
                return pd;
            }).toList();
            dto.setPassengers(passengerDtos);
        }
        return dto;
    }

}

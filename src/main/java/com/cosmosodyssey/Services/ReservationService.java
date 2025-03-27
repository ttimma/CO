package com.cosmosodyssey.Services;

import com.cosmosodyssey.DTOs.ReservationRequestDto;
import com.cosmosodyssey.Entities.Passenger;
import com.cosmosodyssey.Entities.PriceList;
import com.cosmosodyssey.Entities.Reservation;
import com.cosmosodyssey.Entities.Route;
import com.cosmosodyssey.Repositories.PriceListRepository;
import com.cosmosodyssey.Repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PriceListRepository priceListRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              PriceListRepository priceListRepository) {
        this.reservationRepository = reservationRepository;
        this.priceListRepository = priceListRepository;
    }

    public Reservation createReservation(ReservationRequestDto request) {
        // Validate that a Price List ID is provided.
        if (request.getPriceListId() == null) {
            throw new IllegalArgumentException("A valid Price List ID must be provided.");
        }

        // Fetch the PriceList from the database.
        Optional<PriceList> optionalPriceList = priceListRepository.findById(request.getPriceListId());
        if (optionalPriceList.isEmpty()) {
            throw new IllegalArgumentException("Referenced Price List not found.");
        }
        PriceList existingPriceList = optionalPriceList.get();

        // Check if the PriceList is still active.
        if (existingPriceList.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot make a reservation on an expired Price List.");
        }

        // Calculate totals based on selected routes.
        double totalPrice = 0.0;
        long totalTravelTime = 0;
        List<String> companyNamesList = new ArrayList<>();

        for (Long routeId : request.getRouteIds()) {
            for (Route route : existingPriceList.getRoutes()) {
                if (route.getId().equals(routeId)) {
                    totalPrice += route.getPrice();
                    totalTravelTime += Duration.between(route.getFlightStart(), route.getFlightEnd()).toMinutes();
                    if (!companyNamesList.contains(route.getCompanyName())) {
                        companyNamesList.add(route.getCompanyName());
                    }
                    break;
                }
            }
        }
        String companyNamesStr = String.join(", ", companyNamesList);
        String routeIdsString = request.getRouteIds()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        // Create the Reservation.
        Reservation reservation = new Reservation();
        reservation.setPriceList(existingPriceList);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setRouteIds(routeIdsString);
        reservation.setTotalPrice(totalPrice);
        reservation.setTotalTravelTime(totalTravelTime);
        reservation.setCompanyNames(companyNamesStr);

        // Create Passenger entities from the DTO list.
        List<Passenger> passengerEntities = request.getPassengers()
                .stream()
                .map(dto -> {
                    Passenger passenger = new Passenger();
                    passenger.setFirstName(dto.getFirstName());
                    passenger.setLastName(dto.getLastName());
                    passenger.setReservation(reservation); // Set the relationship.
                    return passenger;
                })
                .collect(Collectors.toList());
        reservation.setPassengers(passengerEntities);

        return reservationRepository.save(reservation);
    }
}

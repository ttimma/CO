package com.cosmosodyssey.Controllers;

import com.cosmosodyssey.DTOs.SeatDto;
import com.cosmosodyssey.Services.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flights/{flightNumber}/seats")
public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // Return all seats (as a list) for a given flight.
    @GetMapping
    public List<SeatDto> getSeats(@PathVariable String flightNumber) {
        // Convert the flight's seat map into a list
        return seatService.getSeatsForFlight(flightNumber);
    }


     // Return the 2D seat array for a given flight
     // Using "/matrix" to avoid collision with the list endpoint.

    @GetMapping("/matrix")
    public ResponseEntity<SeatDto[][]> getSeatMap(@PathVariable String flightNumber) {
        SeatDto[][] seatMap = seatService.getSeatMapForFlight(flightNumber);
        return ResponseEntity.ok(seatMap);
    }

    // Book a seat on a given flight.
    @PostMapping("/book")
    public ResponseEntity<String> bookSeat(@PathVariable String flightNumber,
                                           @RequestBody Map<String, Integer> request) {
        // Validate row & col.
        if (request == null || !request.containsKey("row") || !request.containsKey("col")) {
            return ResponseEntity.badRequest().body("Row and column must be provided.");
        }
        Integer row = request.get("row");
        Integer col = request.get("col");
        if (row == null || col == null) {
            return ResponseEntity.badRequest().body("Row and column values cannot be null.");
        }

        // Try to book the seat
        boolean success = seatService.bookSeat(flightNumber, row, col);
        if (success) {
            return ResponseEntity.ok("Seat booked successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Seat already taken.");
        }
    }


     // Recommend seats for a given flight based on preferences.
    @GetMapping("/recommend")
    public ResponseEntity<List<SeatDto>> recommendSeats(
            @PathVariable String flightNumber,
            @RequestParam int numberOfSeats,
            @RequestParam boolean preferWindow,
            @RequestParam boolean preferExtraLegroom,
            @RequestParam boolean preferAisle) {

        List<SeatDto> recommendations = seatService.recommendSeats(
                flightNumber,
                numberOfSeats,
                preferWindow,
                preferExtraLegroom,
                preferAisle
        );

        if (recommendations.isEmpty()) {
            // Return 404 Not Found if no seats are recommended.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(recommendations);
    }
}

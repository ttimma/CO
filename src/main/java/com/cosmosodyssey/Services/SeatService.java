package com.cosmosodyssey.Services;

import com.cosmosodyssey.DTOs.SeatDto;
import com.cosmosodyssey.Entities.Seat;
import com.cosmosodyssey.Repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatService {
    private final int ROWS = 28;     // Number of rows (for an A320)
    private final int COLUMNS = 6;   // Columns A-F

    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    // Returns all seats for a given flight.
    // If seats are not found in the database, a new seat map is initialized and saved.
    public List<SeatDto> getSeatsForFlight(String flightNumber) {
        List<Seat> seats = seatRepository.findByFlightNumber(flightNumber);
        if (seats.isEmpty()) {
            seats = initializeSeatMapForFlight(flightNumber);
        }
        return seats.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Returns a 2D seat map for a given flight.
    public SeatDto[][] getSeatMapForFlight(String flightNumber) {
        List<SeatDto> seatDtos = getSeatsForFlight(flightNumber);
        SeatDto[][] seatMatrix = new SeatDto[ROWS][COLUMNS];
        for (SeatDto seat : seatDtos) {
            // Adjust for 1-based numbering in SeatDto.
            seatMatrix[seat.getRow() - 1][seat.getColumn() - 1] = seat;
        }
        return seatMatrix;
    }

    // Initializes a new seat map for a flight and saves it to the database
    private List<Seat> initializeSeatMapForFlight(String flightNumber) {
        List<Seat> seats = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                boolean isTaken = random.nextDouble() < 0.3; // 30% chance
                int realRowNumber = i + 1; // 1-based numbering
                boolean hasExtraLegroom = (realRowNumber == 11 || realRowNumber == 12);
                int realColumnNumber = j + 1;
                boolean isWindowSeat = (realColumnNumber == 1 || realColumnNumber == COLUMNS);
                boolean isAisle = (realColumnNumber == 3 || realColumnNumber == 4);

                Seat seat = new Seat();
                seat.setFlightNumber(flightNumber);
                seat.setRowNumber(realRowNumber);
                seat.setColumn(realColumnNumber);
                seat.setTaken(isTaken);
                seat.setExtraLegroom(hasExtraLegroom);
                seat.setWindowSeat(isWindowSeat);
                seat.setAisle(isAisle);
                seats.add(seat);
            }
        }
        return seatRepository.saveAll(seats);
    }

    // Books a seat on a given flight.
    public boolean bookSeat(String flightNumber, int row, int col) {
        Optional<Seat> optionalSeat = seatRepository.findByFlightNumberAndRowNumberAndColumn(flightNumber, row, col);
        if (optionalSeat.isPresent()) {
            Seat seat = optionalSeat.get();
            if (!seat.isTaken()) {
                seat.setTaken(true);
                seatRepository.save(seat);
                return true;
            }
        }
        return false;
    }

    // Recommends seats based on user preferences.
    public List<SeatDto> recommendSeats(String flightNumber, int numberOfSeats, boolean preferWindow, boolean preferExtraLegroom, boolean preferAisle) {
        List<SeatDto> availableSeats = getSeatsForFlight(flightNumber).stream()
                .filter(seat -> !seat.isTaken() && matchesPreferences(seat, preferWindow, preferExtraLegroom, preferAisle))
                .collect(Collectors.toList());

        // For a single seat recommendation, sort by a scoring function.
        if (numberOfSeats == 1 && !availableSeats.isEmpty()) {
            availableSeats.sort(Comparator.comparingDouble(seat -> seatScore(seat, preferWindow, preferExtraLegroom, preferAisle)));
            return Collections.singletonList(availableSeats.get(0));
        } else {
            // For multiple seats, try to find consecutive seats in the same row.
            SeatDto[][] seatMatrix = getSeatMapForFlight(flightNumber);
            for (int i = 0; i < ROWS; i++) {
                List<SeatDto> consecutive = new ArrayList<>();
                for (int j = 0; j < COLUMNS; j++) {
                    SeatDto seat = seatMatrix[i][j];
                    if (seat != null && !seat.isTaken() && matchesPreferences(seat, preferWindow, preferExtraLegroom, preferAisle)) {
                        consecutive.add(seat);
                        if (consecutive.size() == numberOfSeats) {
                            return consecutive;
                        }
                    } else {
                        consecutive.clear();
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    // Helper: Check if a seat matches preferences.
    private boolean matchesPreferences(SeatDto seat, boolean preferWindow, boolean preferExtraLegroom, boolean preferAisle) {
        if (preferWindow && !seat.isWindow()) return false;
        if (preferExtraLegroom && !seat.isExtraLegroom()) return false;
        if (preferAisle && !seat.isAisle()) return false;
        return true;
    }

    // Helper: Calculate a score for a seat (lower is better).
    private double seatScore(SeatDto seat, boolean preferWindow, boolean preferExtraLegroom, boolean preferAisle) {
        double score = 0;
        if (preferWindow) score += seat.isWindow() ? 0 : 10;
        if (preferExtraLegroom) score += seat.isExtraLegroom() ? 0 : 5;
        if (preferAisle) score += seat.isAisle() ? 0 : 4;
        return score;
    }

    // Convert Seat entity to SeatDto.
    private SeatDto convertToDto(Seat seat) {
        SeatDto dto = new SeatDto(
                seat.getRowNumber(),
                seat.getColumn(),
                seat.isTaken(),
                seat.isExtraLegroom(),
                seat.isWindowSeat(),
                seat.isAisle()
        );
        return dto;
    }
}

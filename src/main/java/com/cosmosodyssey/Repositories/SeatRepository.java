package com.cosmosodyssey.Repositories;

import com.cosmosodyssey.Entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByFlightNumber(String flightNumber);
    Optional<Seat> findByFlightNumberAndRowNumberAndColumn(String flightNumber, int row, int column);
}

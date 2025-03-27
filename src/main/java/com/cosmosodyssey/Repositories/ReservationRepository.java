package com.cosmosodyssey.Repositories;

import com.cosmosodyssey.Entities.PriceList;
import com.cosmosodyssey.Entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByPriceList(PriceList priceList);

    @Query("SELECT DISTINCT r FROM Reservation r JOIN r.passengers p WHERE p.firstName = :firstName AND p.lastName = :lastName")
    List<Reservation> findByPassengerName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    void deleteAllByPriceList(PriceList priceList);
}

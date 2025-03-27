package com.cosmosodyssey.Repositories;

import com.cosmosodyssey.Entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findByFromAndToAndPriceList_ValidUntilAfter(String from, String to, LocalDateTime now);

    @Query("SELECT DISTINCT r.from FROM Route r")
    List<String> findDistinctOrigins();

    @Query("SELECT DISTINCT r.to FROM Route r")
    List<String> findDistinctDestinations();

    @Query("SELECT DISTINCT r.companyName FROM Route r")
    List<String> findDistinctCompanies();

    @Query("SELECT DISTINCT r.to FROM Route r WHERE r.from = :origin")
    List<String> findDistinctDestinationsByOrigin(@Param("origin") String origin);

}

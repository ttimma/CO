package com.cosmosodyssey.Repositories;

import com.cosmosodyssey.Entities.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {
    Optional<PriceList> findByApiId(String apiId);
    List<PriceList> findAllByOrderByValidUntilDesc();
}

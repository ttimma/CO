package com.cosmosodyssey.Services;

import com.cosmosodyssey.DTOs.PriceListDto;
import com.cosmosodyssey.Entities.PriceList;
import com.cosmosodyssey.Entities.Route;
import com.cosmosodyssey.Repositories.PriceListRepository;
import com.cosmosodyssey.Repositories.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PriceListService {
    private final Object updateLock = new Object();

    private final PriceListRepository priceListRepository;
    private final PriceListFetchService priceListFetchService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public PriceListService(PriceListRepository priceListRepository,
                            PriceListFetchService priceListFetchService,
                            ReservationRepository reservationRepository) {
        this.priceListRepository = priceListRepository;
        this.priceListFetchService = priceListFetchService;
        this.reservationRepository = reservationRepository;
    }

    public PriceList updatePriceList() {
        synchronized (updateLock) {
            try {
                PriceListDto dto = priceListFetchService.fetchPriceList().block();
                if (dto == null) {
                    throw new IllegalStateException("No PriceList data received from API");
                }

                // Convert validUntil to local time
                OffsetDateTime odt = OffsetDateTime.parse(dto.getValidUntil());
                LocalDateTime localValidUntil = LocalDateTime.ofInstant(odt.toInstant(), ZoneId.systemDefault());

                Optional<PriceList> existingOpt = priceListRepository.findByApiId(dto.getId());
                if (existingOpt.isPresent()) {
                    PriceList existing = existingOpt.get();
                    // If the existing PriceList is still active, do nothing and return it
                    if (existing.getValidUntil().isAfter(LocalDateTime.now())) {
                        return existing;
                    }
                    // Otherwise, proceed to create a new snapshot
                }

                PriceList priceList = PriceListMapper.convertDtoToEntity(dto);
                PriceList saved = priceListRepository.save(priceList);

                // Generate flight numbers
                for (Route route : saved.getRoutes()) {
                    route.setFlightNumber("CO" + String.format("%04d", route.getId()));
                }

                PriceList finalSaved = priceListRepository.save(saved);
                return finalSaved;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public void cleanupOldPriceLists() {
        // Retrieve all price lists ordered by validUntil (latest first)
        List<PriceList> allPriceLists = priceListRepository.findAllByOrderByValidUntilDesc();
        if (allPriceLists.size() > 15) {
            // Keep the first 15 (most recent), delete the rest
            List<PriceList> toDelete = allPriceLists.subList(15, allPriceLists.size());
            toDelete.forEach(priceList -> {
                // Delete reservations associated with this price list
                reservationRepository.deleteAllByPriceList(priceList);
                // Delete the price list itself
                priceListRepository.delete(priceList);
            });
        }
    }

    @Transactional
    // Scheduled to run every ten minutes
    @Scheduled(fixedRate = 600000)
    public void scheduledUpdateAndCleanup() {
        updatePriceList();
        cleanupOldPriceLists();
    }
}

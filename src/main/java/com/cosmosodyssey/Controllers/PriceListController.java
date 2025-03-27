package com.cosmosodyssey.Controllers;

import com.cosmosodyssey.Entities.PriceList;
import com.cosmosodyssey.Services.PriceListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pricelists")
public class PriceListController {

    private final PriceListService priceListService;

    public PriceListController(PriceListService priceListService) {
        this.priceListService = priceListService;
    }

    // Endpoint to force an immediate update of the PriceList
    @GetMapping("/update")
    public ResponseEntity<?> updatePriceList() {
        try {
            // Delegate to the service.
            PriceList updated = priceListService.updatePriceList();
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            // If something fails, respond with 500 and the error message.
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}

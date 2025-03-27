package com.cosmosodyssey.Controllers;

import com.cosmosodyssey.Repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LookupController {

    private final RouteRepository routeRepository;

    @Autowired
    public LookupController(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @GetMapping("/origins")
    public List<String> getOrigins() {
        return routeRepository.findDistinctOrigins();
    }

    @GetMapping("/destinations")
    public List<String> getDestinations(@RequestParam(required = false) String origin) {
        if (origin != null && !origin.trim().isEmpty()) {
            return routeRepository.findDistinctDestinationsByOrigin(origin);
        }
        return routeRepository.findDistinctDestinations();
    }

    @GetMapping("/companies")
    public List<String> getCompanies() {
        return routeRepository.findDistinctCompanies();
    }


}

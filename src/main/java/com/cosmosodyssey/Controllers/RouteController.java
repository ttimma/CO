package com.cosmosodyssey.Controllers;

import com.cosmosodyssey.DTOs.RouteDto; // a DTO you might create to return route info
import com.cosmosodyssey.Services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public List<RouteDto> getRoutes(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String sortBy) {
        return routeService.searchRoutes(origin, destination, companyName, sortBy);
    }
}

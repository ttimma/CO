package com.cosmosodyssey.Services;

import com.cosmosodyssey.DTOs.RouteDto;
import com.cosmosodyssey.Entities.Route;
import com.cosmosodyssey.Repositories.RouteRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<RouteDto> searchRoutes(String origin, String destination, String companyName, String sortBy) {
        LocalDateTime now = LocalDateTime.now();
        List<Route> routes = routeRepository.findByFromAndToAndPriceList_ValidUntilAfter(origin, destination, now);

        // Filter by company name if provided
        if (companyName != null && !companyName.trim().isEmpty()) {
            routes = routes.stream()
                    .filter(route -> route.getCompanyName().equalsIgnoreCase(companyName))
                    .collect(Collectors.toList());
        }

        // Sort routes based on the provided sortBy parameter
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Comparator<Route> comparator;
            switch (sortBy.toLowerCase()) {
                case "price":
                    comparator = Comparator.comparing(Route::getPrice);
                    break;
                case "distance":
                    comparator = Comparator.comparing(Route::getDistance);
                    break;
                case "time":
                case "traveltime":
                    // Calculate travel time as the duration in minutes between flightStart and flightEnd
                    comparator = Comparator.comparing(route -> Duration.between(route.getFlightStart(), route.getFlightEnd()).toMinutes());
                    break;
                default:
                    comparator = Comparator.comparing(Route::getId); // Default sort by ID
                    break;
            }
            routes = routes.stream().sorted(comparator).collect(Collectors.toList());
        }

        // Map the Route entities to RouteDto using a mapping method
        return routes.stream()
                .map(RouteDto::fromEntity)
                .collect(Collectors.toList());
    }
}

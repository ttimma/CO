package com.cosmosodyssey.DTOs;

import com.cosmosodyssey.Entities.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto {
    private Long id;
    private String legId;
    private String apiRouteInfoId;
    private String from;
    private String to;
    private long distance;
    private double price;
    private LocalDateTime flightStart;
    private LocalDateTime flightEnd;
    private String companyName;
    private String flightNumber;
    private Long priceListId;

    public static RouteDto fromEntity(Route route) {
        RouteDto dto = new RouteDto();
        dto.setId(route.getId());
        dto.setLegId(route.getLegId());
        dto.setApiRouteInfoId(route.getApiRouteInfoId());
        dto.setFrom(route.getFrom());
        dto.setTo(route.getTo());
        dto.setDistance(route.getDistance());
        dto.setPrice(route.getPrice());
        dto.setFlightStart(route.getFlightStart());
        dto.setFlightEnd(route.getFlightEnd());
        dto.setCompanyName(route.getCompanyName());
        dto.setFlightNumber(route.getFlightNumber());
        // Set the priceListId from the associated PriceList (if present)
        dto.setPriceListId(route.getPriceList() != null ? route.getPriceList().getId() : null);
        return dto;
    }
}

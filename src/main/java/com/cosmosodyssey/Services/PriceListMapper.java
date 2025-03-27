package com.cosmosodyssey.Services;

import com.cosmosodyssey.DTOs.PriceListDto;
import com.cosmosodyssey.DTOs.LegDto;
import com.cosmosodyssey.DTOs.RouteInfoDto;
import com.cosmosodyssey.DTOs.ProviderDto;
import com.cosmosodyssey.Entities.PriceList;
import com.cosmosodyssey.Entities.Route;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class PriceListMapper {

    public static PriceList convertDtoToEntity(PriceListDto dto) {
        PriceList priceList = new PriceList();
        priceList.setApiId(dto.getId());
        // Parse validUntil string from the API (which is in UTC) and convert to local time
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dto.getValidUntil());
        LocalDateTime localValidUntil = LocalDateTime.ofInstant(offsetDateTime.toInstant(), ZoneId.systemDefault());
        priceList.setValidUntil(localValidUntil);

        List<Route> routes = new ArrayList<>();
        // Iterate over each leg in the DTO
        for (LegDto leg : dto.getLegs()) {
            RouteInfoDto routeInfo = leg.getRouteInfo();
            String origin = routeInfo.getFrom().getName();
            String destination = routeInfo.getTo().getName();
            long distance = routeInfo.getDistance();

            // For each provider in the leg, create a Route entity
            for (ProviderDto provider : leg.getProviders()) {
                Route route = new Route();
                // Save the leg id and routeInfo id if needed
                route.setLegId(leg.getId());
                route.setApiRouteInfoId(routeInfo.getId());
                route.setFrom(origin);
                route.setTo(destination);
                route.setDistance(distance);
                route.setPrice(provider.getPrice());
                // Parse flight times into LocalDateTime
                route.setFlightStart(OffsetDateTime.parse(provider.getFlightStart()).toLocalDateTime());
                route.setFlightEnd(OffsetDateTime.parse(provider.getFlightEnd()).toLocalDateTime());
                // Set the company name from the provider's company data
                route.setCompanyName(provider.getCompany().getName());

                // Set the bidirectional relationship: each route belongs to this price list
                route.setPriceList(priceList);
                routes.add(route);
            }
        }
        priceList.setRoutes(routes);
        return priceList;
    }
}

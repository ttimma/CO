package com.cosmosodyssey.DTOs;
import lombok.Data;

@Data
public class RouteInfoDto {
    private String id;
    private PlanetDto from;
    private PlanetDto to;
    private long distance;
}
package com.cosmosodyssey.DTOs;
import lombok.Data;
import java.util.List;

@Data
public class LegDto {
    private String id;
    private RouteInfoDto routeInfo;
    private List<ProviderDto> providers;
}
package com.cosmosodyssey.DTOs;

import lombok.Data;

@Data
public class ProviderDto {
    private String id;
    private CompanyDto company;
    private double price;
    private String flightStart;
    private String flightEnd;
}
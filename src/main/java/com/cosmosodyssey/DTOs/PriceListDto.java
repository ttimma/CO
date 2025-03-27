package com.cosmosodyssey.DTOs;

import lombok.Data;
import java.util.List;

@Data
public class PriceListDto {
    private String id;
    private String validUntil;
    private List<LegDto> legs;
}
package com.cosmosodyssey.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;

    private String legId;
    private String apiRouteInfoId;

    @Column(name = "origin")
    private String from;

    @Column(name = "destination")
    private String to;

    private long distance;
    private double price;
    private LocalDateTime flightStart;
    private LocalDateTime flightEnd;
    private String companyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_list_id")
    private PriceList priceList;
}

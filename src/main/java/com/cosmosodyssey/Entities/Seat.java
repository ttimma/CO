package com.cosmosodyssey.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;

    @Column(name = "row_number")
    private int rowNumber;

    @Column(name = "seat_column")
    private int column;

    private boolean taken;

    private boolean extraLegroom;

    private boolean aisle;

    @Column(name = "window_seat", nullable = false)
    private boolean windowSeat;

    // Getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFlightNumber() {
        return flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    public int getRowNumber() {
        return rowNumber;
    }
    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
    public int getColumn() {
        return column;
    }
    public void setColumn(int column) {
        this.column = column;
    }
    public boolean isTaken() {
        return taken;
    }
    public void setTaken(boolean taken) {
        this.taken = taken;
    }
    public boolean isExtraLegroom() {
        return extraLegroom;
    }
    public void setExtraLegroom(boolean extraLegroom) {
        this.extraLegroom = extraLegroom;
    }
    public boolean isAisle() {
        return aisle;
    }
    public void setAisle(boolean aisle) {
        this.aisle = aisle;
    }
    public boolean isWindowSeat() {
        return windowSeat;
    }
    public void setWindowSeat(boolean windowSeat) {
        this.windowSeat = windowSeat;
    }
}

package com.cosmosodyssey.DTOs;

public class SeatDto {
    private int row;
    private int column;
    private boolean taken;
    private boolean extraLegroom;
    private boolean window;
    private boolean aisle;

    public SeatDto(int row, int column, boolean taken, boolean extraLegroom, boolean window, boolean aisle) {
        this.row = row;
        this.column = column;
        this.taken = taken;
        this.extraLegroom = extraLegroom;
        this.window = window;
        this.aisle = aisle;
    }

    // Getters and setters

    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
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
    public boolean isWindow() {
        return window;
    }
    public void setWindow(boolean window) {
        this.window = window;
    }
    public boolean isAisle() {
        return aisle;
    }
    public void setAisle(boolean aisle) {
        this.aisle = aisle;
    }
}

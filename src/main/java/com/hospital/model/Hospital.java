package com.hospital.model;

public class Hospital {
    private int id;
    private String name;
    private String location;
    private int totalBeds;
    private int availableBeds;

    public Hospital(int id, String name, String location, int totalBeds, int availableBeds) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.totalBeds = totalBeds;
        this.availableBeds = availableBeds;
    }

    public Hospital(String name, String location, int totalBeds, int availableBeds) {
        this(0, name, location, totalBeds, availableBeds);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getTotalBeds() { return totalBeds; }
    public int getAvailableBeds() { return availableBeds; }

    public void setAvailableBeds(int availableBeds) { this.availableBeds = availableBeds; }

    @Override
    public String toString() {
        return String.format("%d | %-15s | %-12s | total:%3d | available:%3d",
                id, name, location, totalBeds, availableBeds);
    }
}

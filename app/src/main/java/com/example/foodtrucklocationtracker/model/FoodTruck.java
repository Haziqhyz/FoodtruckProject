package com.example.foodtrucklocationtracker.model;

public class FoodTruck {
    private String Type;
    private String reportedBy;
    private String lastReported;
    private double latitude;
    private double longitude;

    public FoodTruck() {} // Firestore requires empty constructor

    public String getType() { return Type; }
    public void setType(String type) { this.Type = type; }

    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }

    public String getLastReported() { return lastReported; }
    public void setLastReported(String lastReported) { this.lastReported = lastReported; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}

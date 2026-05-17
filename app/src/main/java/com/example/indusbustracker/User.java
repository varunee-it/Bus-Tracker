package com.example.indusbustracker;

public class User {
    private final String name;
    private final String busNo;
    private final String phoneNo;
    private final double latitude;
    private final double longitude;
    private String key; // To store the automatically generated ID

    public User(String name, String busNo, String phoneNo, double latitude, double longitude) {
        this.name = name;
        this.busNo = busNo;
        this.phoneNo = phoneNo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setKey(String key) {
        this.key = key;

        // Getters and setters for all fields (optional)
    }
}

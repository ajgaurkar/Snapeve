package com.umbcapp.gaurk.snapeve.Controllers;

import java.util.ArrayList;

public class LocationCoordinatesList {


    String location;
    double lat;
    double lon;

    public LocationCoordinatesList(String location, double lat, double lon) {
        this.location = location;
        this.lat = lat;
        this.lon = lon;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}

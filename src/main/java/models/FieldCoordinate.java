package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldCoordinate {
    @JsonProperty("Lat")
    private float lat;
    @JsonProperty("Lng")
    private float lng;

    public FieldCoordinate(){}

    public FieldCoordinate(float lat, float lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    
}

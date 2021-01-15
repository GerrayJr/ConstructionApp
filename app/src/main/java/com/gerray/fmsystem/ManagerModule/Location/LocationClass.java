package com.gerray.fmsystem.ManagerModule.Location;

public class LocationClass {
    private String facilityName;
    private String facilityType;
    private String email;
    private String latitude;
    private String longitude;

    public LocationClass() {
    }

    public LocationClass(String facilityName, String facilityType, String email, String latitude, String longitude) {
        this.facilityName = facilityName;
        this.facilityType = facilityType;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

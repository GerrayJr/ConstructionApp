package com.gerray.fmsystem.ManagerModule;

public class FacProfile {
    private String facilityName;
    private String authorityName;
    private String facilityType;
    private String postalAddress;
    private String emailAddress;
    private int occupancyNo;
    private String facilityID;
    private String facilityImageUrl;

    public FacProfile() {

    }

    public FacProfile(String facilityName, String authorityName, String facilityType, String postalAddress, String emailAddress, int occupancyNo, String facilityID, String facilityImageUrl) {
        this.facilityName = facilityName;
        this.authorityName = authorityName;
        this.facilityType = facilityType;
        this.postalAddress = postalAddress;
        this.emailAddress = emailAddress;
        this.occupancyNo = occupancyNo;
        this.facilityID = facilityID;
        this.facilityImageUrl = facilityImageUrl;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public int getOccupancyNo() {
        return occupancyNo;
    }

    public void setOccupancyNo(int occupancyNo) {
        this.occupancyNo = occupancyNo;
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFacilityImageUrl() {
        return facilityImageUrl;
    }

    public void setFacilityImageUrl(String facilityImageUrl) {
        this.facilityImageUrl = facilityImageUrl;
    }
}

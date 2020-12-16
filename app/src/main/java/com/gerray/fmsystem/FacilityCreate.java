package com.gerray.fmsystem;

public class FacilityCreate {
    private String FacilityName;
    private String AuthorityName;
    private String FacilityType;
    private String PostalAddress;
    private String EmailAddress;
    private int OccupancyNo;
    private String FacilityID;
    private String FacilityImageUrl;

    public FacilityCreate() {

    }

    public FacilityCreate(String facilityName, String authorityName, String facilityType, String postalAddress, String emailAddress, int occupancyNo, String facilityID, String facilityImageUrl) {
        FacilityName = facilityName;
        AuthorityName = authorityName;
        FacilityType = facilityType;
        PostalAddress = postalAddress;
        EmailAddress = emailAddress;
        OccupancyNo = occupancyNo;
        FacilityID = facilityID;
        FacilityImageUrl = facilityImageUrl;
    }

    public String getFacilityName() {
        return FacilityName;
    }

    public void setFacilityName(String facilityName) {
        FacilityName = facilityName;
    }

    public String getAuthorityName() {
        return AuthorityName;
    }

    public void setAuthorityName(String authorityName) {
        AuthorityName = authorityName;
    }

    public String getFacilityType() {
        return FacilityType;
    }

    public void setFacilityType(String facilityType) {
        FacilityType = facilityType;
    }

    public int getOccupancyNo() {
        return OccupancyNo;
    }

    public void setOccupancyNo(int occupancyNo) {
        OccupancyNo = occupancyNo;
    }

    public String getFacilityID() {
        return FacilityID;
    }

    public void setFacilityID(String facilityID) {
        FacilityID = facilityID;
    }

    public String getPostalAddress() {
        return PostalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        PostalAddress = postalAddress;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getFacilityImageUrl() {
        return FacilityImageUrl;
    }

    public void setFacilityImageUrl(String facilityImageUrl) {
        FacilityImageUrl = facilityImageUrl;
    }
}

package com.gerray.fmsystem;

public class FacRegister {
    private String FacilityID;
    private String UserID;
    private String Name;
    private String FacilityManager;

    public FacRegister() {
    }

    public FacRegister(String facilityID, String userID, String name, String facilityManager){
        FacilityID = facilityID;
        UserID = userID;
        Name = name;
        FacilityManager = facilityManager;
    }

    public String getFacilityID() {
        return FacilityID;
    }

    public void setFacilityID(String facilityID) {
        FacilityID = facilityID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFacilityManager() {
        return FacilityManager;
    }

    public void setFacilityManager(String facilityManager) {
        FacilityManager = facilityManager;
    }
}

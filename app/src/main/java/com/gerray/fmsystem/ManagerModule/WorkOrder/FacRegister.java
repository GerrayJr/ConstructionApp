package com.gerray.fmsystem.ManagerModule.WorkOrder;

public class FacRegister {
    private String facilityID;
    private String userID;
    private String name;
    private String facilityManager;

    public FacRegister() {
    }

    public FacRegister(String facilityID, String userID, String name, String facilityManager){
        this.facilityID = facilityID;
        this.userID = userID;
        this.name = name;
        this.facilityManager = facilityManager;
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacilityManager() {
        return facilityManager;
    }

    public void setFacilityManager(String facilityManager) {
        this.facilityManager = facilityManager;
    }
}

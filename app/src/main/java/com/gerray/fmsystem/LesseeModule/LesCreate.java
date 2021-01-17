package com.gerray.fmsystem.LesseeModule;

public class LesCreate {
    public String contactName;
    public String lesseeName;
    public String activityType;
    public String lesseeID;
    public String userID;

    public LesCreate() {

    }

    public LesCreate(String contactName, String lesseeName, String activityType, String lesseeID, String userID) {
        this.contactName = contactName;
        this.lesseeName = lesseeName;
        this.activityType = activityType;
        this.lesseeID = lesseeID;
        this.userID = userID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLesseeName() {
        return lesseeName;
    }

    public void setLesseeName(String lesseeName) {
        this.lesseeName = lesseeName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getLesseeID() {
        return lesseeID;
    }

    public void setLesseeID(String lesseeID) {
        this.lesseeID = lesseeID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

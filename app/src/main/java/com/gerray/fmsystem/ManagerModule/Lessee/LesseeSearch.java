package com.gerray.fmsystem.ManagerModule.Lessee;

public class LesseeSearch {
    public String contactName, lesseeName, activityType, lesseeID, userID;

    public LesseeSearch() {
    }

    public LesseeSearch(String contactName, String lesseeName, String activityType, String lesseeID) {
        this.contactName = contactName;
        this.lesseeName = lesseeName;
        this.activityType = activityType;
        this.lesseeID = lesseeID;
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

    public String getActivity() {
        return activityType;
    }

    public void setActivity(String activityType) {
        this.activityType = activityType;
    }

    public String getLesseeID() {
        return lesseeID;
    }

    public void setLesseeID(String lesseeID) {
        this.lesseeID = lesseeID;
    }
}

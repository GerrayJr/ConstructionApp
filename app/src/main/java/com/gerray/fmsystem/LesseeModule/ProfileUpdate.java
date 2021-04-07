package com.gerray.fmsystem.LesseeModule;

public class ProfileUpdate {
    public String contactName;
    public String lesseeName;
    public String activityType;
    public String emailAddress;
    public String  phoneNumber;
    public String uri;

    public ProfileUpdate() {
    }

    public ProfileUpdate(String contactName, String lesseeName, String activityType, String emailAddress, String phoneNumber, String uri) {
        this.contactName = contactName;
        this.lesseeName = lesseeName;
        this.activityType = activityType;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.uri = uri;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

package com.gerray.fmsystem.LesseeModule;

public class LesseeRequestClass {
    public String requestID;
    public String userID;
    public String description;
    public String requestDate;
    public String imageUrl;

    public LesseeRequestClass() {
    }

    public LesseeRequestClass(String requestID, String userID,  String description, String requestDate, String imageUrl) {
        this.requestID = requestID;
        this.userID = userID;
        this.description = description;
        this.requestDate = requestDate;
        this.imageUrl = imageUrl;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

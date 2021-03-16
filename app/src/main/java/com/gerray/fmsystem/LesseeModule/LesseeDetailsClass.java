package com.gerray.fmsystem.LesseeModule;

public class LesseeDetailsClass {
    public String workID;
    public String lesseeID;
    public String requester;
    public String requestDate;
    public String workDate;
    public String workDescription;
    public String status;
    public String consultantID;
    public Integer cost;
    public String imageUrl;

    public LesseeDetailsClass() {
    }

    public LesseeDetailsClass(String workID, String lesseeID, String requester, String requestDate, String workDate, String workDescription, String status, String consultantID, Integer cost, String imageUrl) {
        this.workID = workID;
        this.lesseeID = lesseeID;
        this.requester = requester;
        this.requestDate = requestDate;
        this.workDate = workDate;
        this.workDescription = workDescription;
        this.status = status;
        this.consultantID = consultantID;
        this.cost = cost;
        this.imageUrl = imageUrl;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }

    public String getLesseeID() {
        return lesseeID;
    }

    public void setLesseeID(String lesseeID) {
        this.lesseeID = lesseeID;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsultantID() {
        return consultantID;
    }

    public void setConsultantID(String consultantID) {
        this.consultantID = consultantID;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

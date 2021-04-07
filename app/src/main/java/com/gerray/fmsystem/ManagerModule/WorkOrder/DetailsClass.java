package com.gerray.fmsystem.ManagerModule.WorkOrder;

public class DetailsClass {
    public String workID;
    public String fManagerID;
    public String requester;
    public String requestDate;
    public String workDate;
    public String workDescription;
    public String status;
    public String consultantID;
    public Integer cost;
    public String imageUrl;

    public DetailsClass() {
    }

    public DetailsClass(String workID, String fManagerID, String requester, String requestDate, String workDate, String workDescription, String status, String consultantID, Integer cost, String imageUrl) {
        this.workID = workID;
        this.fManagerID = fManagerID;
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

    public String getfManagerID() {
        return fManagerID;
    }

    public void setfManagerID(String fManagerID) {
        this.fManagerID = fManagerID;
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

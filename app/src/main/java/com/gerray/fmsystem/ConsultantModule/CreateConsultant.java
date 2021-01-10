package com.gerray.fmsystem.ConsultantModule;

public class CreateConsultant {
    public String consultantName;
    public String category;
    public String companyName;
    public String specialization;
    public String consultantLocation;
    public String consID;
    public String userID;

    public CreateConsultant() {
    }

    public CreateConsultant(String consultantName, String category, String companyName, String specialization, String consultantLocation, String consID, String userID) {
        this.consultantName = consultantName;
        this.category = category;
        this.companyName = companyName;
        this.specialization = specialization;
        this.consultantLocation = consultantLocation;
        this.consID = consID;
        this.userID = userID;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getConsultantLocation() {
        return consultantLocation;
    }

    public void setConsultantLocation(String consultantLocation) {
        this.consultantLocation = consultantLocation;
    }

    public String getConsID() {
        return consID;
    }

    public void setConsID(String consID) {
        this.consID = consID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

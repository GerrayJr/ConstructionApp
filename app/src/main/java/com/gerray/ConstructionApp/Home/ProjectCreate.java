package com.gerray.ConstructionApp.Home;

public class ProjectCreate {
    private String ProjectName;
    private String CreationDate;
    private String ProjectID;

    public ProjectCreate() {

    }

    public ProjectCreate(String projectName, String creationDate, String projectID) {
        ProjectName = projectName;
        CreationDate = creationDate;
        ProjectID = projectID;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }
}


package com.gerray.ConstructionApp.Authentication;

public class InfoUser {
    public String Category;
    public String Email;
    public String FirstName;
    public String Phone;
    public String SecondName;
    public String UserID;

    public InfoUser() {
    }

    public InfoUser(String firstName, String secondName, String email, String phone, String userID, String category) {
        this.FirstName = firstName;
        this.SecondName = secondName;
        this.Email = email;
        this.Phone = phone;
        this.UserID = userID;
        this.Category = category;
    }
}

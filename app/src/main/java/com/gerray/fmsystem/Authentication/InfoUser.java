package com.gerray.fmsystem.Authentication;

public class InfoUser {
    public String FirstName;
    public String SecondName;
    public String Email;
    public String Category;
    public int Phone;

    public InfoUser() {

    }

    public InfoUser(String firstName, String secondName, String email, int phone, String category) {
        FirstName = firstName;
        SecondName = secondName;
        Email = email;
        Phone = phone;
        Category = category;

    }
}

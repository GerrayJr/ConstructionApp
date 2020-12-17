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

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getSecondName() {
        return SecondName;
    }

    public void setSecondName(String secondName) {
        SecondName = secondName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getPhone() {
        return Phone;
    }

    public void setPhone(int phone) {
        Phone = phone;
    }
}

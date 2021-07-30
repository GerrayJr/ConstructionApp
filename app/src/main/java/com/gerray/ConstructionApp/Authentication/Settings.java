package com.gerray.ConstructionApp.Authentication;

public class Settings {
    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber.equals("")) {
            return "";
        }
        if (phoneNumber.length() < 11 & phoneNumber.startsWith("0")) {

            return phoneNumber.replaceFirst("^0", "+254");
        }
        if (phoneNumber.length() == 12 && phoneNumber.startsWith("2")) {
            return phoneNumber.replaceFirst("2", "+2");
        }
        return phoneNumber;
    }
}

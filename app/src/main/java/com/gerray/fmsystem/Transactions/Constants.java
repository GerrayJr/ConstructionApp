package com.gerray.fmsystem.Transactions;


import android.util.Base64;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constants {
//    public static final int CONNECT_TIMEOUT = 60 * 1000;
//
//    public static final int READ_TIMEOUT = 60 * 1000;
//
//    public static final int WRITE_TIMEOUT = 60 * 1000;
//
//    public static final String BASE_URL = "https://sandbox.safaricom.co.ke/";
//
//    public static final String BUSINESS_SHORT_CODE = "174379";
//    public static final String PASSKEY = "MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjEwMzE0MDgxMjAw";
//    public static final String TRANSACTION_TYPE = "CustomerPayBillOnline";
//    public static final String PARTYB = "174379"; //same as business shortcode above
    public static final String CALLBACKURL = "http://mpesa-requestbin.herokuapp.com/v5zzbkv6";

    public static final  String MPESA_BASE_URL = "https://sandbox.safaricom.co.ke";
    public static final String CONSUMER_KEY = "AwPFI8FI8bMXE5S3sP63EWuFHeVyKf0S";
    public static final String CONSUMER_SECRET = "ZAL8boGZBpcRfhqG";
    //STKPush Properties
    public static final String BUSINESS_SHORT_CODE = "174379";
    public static final String PHONE = "";
    public static final String PASSKEY = "MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjEwMzE0MDgxMjAw";
    public static final String TRANSACTION_TYPE = "CustomerPayBillOnline";
    public static final String PARTYB = "174379";
    public static final String CALLBACK_URL = "http://mpesa-requestbin.herokuapp.com/v5zzbkv6";
    /*
     * Function to get the current timestamp using the yyyyMMddHHmmss format
     */
    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
    }
    /*
     * Function used to obtain the required phone number format
     */
    public static String sanitizePhoneNumber(String phone) {

        if (phone.equals("")) {
            return "";
        }

        if (phone.length() < 11 & phone.startsWith("0")) {
            String p = phone.replaceFirst("^0", "254");
            return p;
        }
        if (phone.length() == 13 && phone.startsWith("+")) {
            String p = phone.replaceFirst("^+", "");
            return p;
        }
        return phone;
    }
    /*
     * Function to get the password using the base64 encoding
     */
    public static String getPassword(String businessShortCode, String passkey, String timestamp) {
        String str = businessShortCode + passkey + timestamp;
        //encode the password to Base64
        return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
    }
}
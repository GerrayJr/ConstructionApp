package com.gerray.fmsystem.CommunicationModule;

import java.util.Date;

public class ChatClass {
    public String title;
    public String chatID;
    public String senderID;
    public String receiverID;
    public String receiverName;
    public String receiverContact;
    public String senderName;
    public Date time;

    public ChatClass() {
    }

    public ChatClass(String title, String chatID, String senderID, String receiverID, Date time, String receiverName, String receiverContact, String senderName) {
        this.title = title;
        this.chatID = chatID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.time = time;
        this.receiverContact = receiverContact;
        this.receiverName = receiverName;
        this.senderName = senderName;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

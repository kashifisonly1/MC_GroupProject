package com.mcgroupproject.whatsappclone.Model;

public class User {
    private String userID;
    private String username;
    private String lastMessage;
    private String Date;
    private String Phone;
    private String urlProfile;

    public User() {
    }

    public User(String userID, String phone, String username, String lastMessage, String date, String urlProfile) {
        this.userID = userID;
        this.Phone = phone;
        this.username = username;
        this.lastMessage = lastMessage;
        Date = date;
        this.urlProfile = urlProfile;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}

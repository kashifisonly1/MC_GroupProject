package com.mcgroupproject.whatsappclone.model;

public class ChatList {
    private String userID;
    private String username;
    private String lastMessage;
    private String Date;
    private String urlProfile;

    public ChatList() {
    }

    public ChatList(String userID, String username, String lastMessage, String date, String urlProfile) {
        this.userID = userID;
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
}

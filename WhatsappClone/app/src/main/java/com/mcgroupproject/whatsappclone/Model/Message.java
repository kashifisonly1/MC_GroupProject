package com.mcgroupproject.whatsappclone.Model;

import com.mcgroupproject.whatsappclone.Utilities.DatetimeUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    String id;
    String senderID;
    String receiverID;
    String pushId;
    String text;
    int status;
    String time;
    long timeInt;
    String date;
    String replyATID = "Not Null";
    User senderObj;
    User receiverObj;
    Message replyObj;

    public Message()
    {}
    public Message(String text, String time, int status)
    {
        this.text = text;
        this.time = time;
        this.status = status;
    }
    public void setTimeInt(long t) { timeInt = t; }
    public long getTimeInt() { return timeInt; }
    public Message(String id, String senderID, String receiverID, String text, int status, String time, String date, String replyATID, User senderObj, User receiverObj, Message replyObj, long t) {
        this.id = id;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.text = text;
        this.status = status;
        this.time = time;
        this.date = date;
        timeInt = t;
        this.replyATID = replyATID;
        this.senderObj = senderObj;
        this.receiverObj = receiverObj;
        this.replyObj = replyObj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReplyATID() {
        return replyATID;
    }

    public void setReplyATID(String replyATID) {
        this.replyATID = replyATID;
    }

    public User getSenderObj() {
        return senderObj;
    }

    public void setSenderObj(User senderObj) {
        this.senderObj = senderObj;
    }

    public User getReceiverObj() {
        return receiverObj;
    }

    public void setReceiverObj(User receiverObj) {
        this.receiverObj = receiverObj;
    }

    public Message getReplyObj() {
        return replyObj;
    }

    public void setReplyObj(Message replyObj) {
        this.replyObj = replyObj;
    }
    public void setDateTime()
    {
        this.time = DatetimeUtility.GetTime(timeInt);
        this.date = DatetimeUtility.GetDate(timeInt);
    }
}
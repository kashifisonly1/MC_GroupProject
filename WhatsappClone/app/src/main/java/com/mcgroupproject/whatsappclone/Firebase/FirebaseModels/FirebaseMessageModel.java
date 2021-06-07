package com.mcgroupproject.whatsappclone.Firebase.FirebaseModels;

public class FirebaseMessageModel {
    public String sender;
    public String msg;
    public String type;
    public String msgID;
    public long time;
    public FirebaseMessageModel(){}

    @Override
    public String toString() {
        return "MessageModel{" +
                "sender='" + sender + '\'' +
                ", msg='" + msg + '\'' +
                ", type='" + type + '\'' +
                ", msgID='" + msgID + '\'' +
                ", time=" + time +
                '}';
    }
}
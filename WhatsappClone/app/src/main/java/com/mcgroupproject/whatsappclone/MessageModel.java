package com.mcgroupproject.whatsappclone;

public class MessageModel {
    public String sender;
    public String msg;
    public String type;
    public String msgID;
    public long time;
    public MessageModel(){}

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
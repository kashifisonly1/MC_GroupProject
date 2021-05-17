package com.mcgroupproject.whatsappclone;

public class User {
    public String UID;
    public String Name;
    public String Phone;
    public String Status;
    public User(){}
    public User(String id, String nm, String ph, String s) {
        UID=id;
        Name=nm;
        Phone = ph;
        Status=s;
    }
}

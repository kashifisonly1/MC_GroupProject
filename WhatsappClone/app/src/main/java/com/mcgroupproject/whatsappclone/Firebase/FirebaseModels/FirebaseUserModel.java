package com.mcgroupproject.whatsappclone.Firebase.FirebaseModels;

public class FirebaseUserModel {
    public String UID;
    public String Name;
    public String Phone;
    public String Status;
    public FirebaseUserModel(){}
    public FirebaseUserModel(String id, String nm, String ph, String s) {
        UID=id;
        Name=nm;
        Phone = ph;
        Status=s;
    }
}

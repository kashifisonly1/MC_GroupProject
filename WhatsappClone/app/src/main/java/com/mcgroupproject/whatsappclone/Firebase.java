package com.mcgroupproject.whatsappclone;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Firebase {
    public static final FirebaseAuth auth = FirebaseAuth.getInstance();
    public static final FirebaseDatabase db = FirebaseDatabase.getInstance();
    public static void init(){
    }
}

package com.mcgroupproject.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(getBaseContext(), SignupPage.class);
            startActivity(intent);
        }
        TextView textView = findViewById(R.id.name);
        textView.setText("Welcome, " + mAuth.getCurrentUser().getDisplayName());
    }

    public void signout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(getBaseContext(), SignupPage.class);
        startActivity(intent);
    }
}
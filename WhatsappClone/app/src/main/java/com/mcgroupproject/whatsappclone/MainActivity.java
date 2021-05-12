package com.mcgroupproject.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signinphone(View view) {
        EditText phoneField = findViewById(R.id.phoneField);
        System.out.println(phoneField.getText());
    }
}
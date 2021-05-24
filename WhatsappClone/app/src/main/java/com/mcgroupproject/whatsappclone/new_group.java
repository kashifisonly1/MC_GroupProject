package com.mcgroupproject.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class new_group extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8F00B7")));
    }
}
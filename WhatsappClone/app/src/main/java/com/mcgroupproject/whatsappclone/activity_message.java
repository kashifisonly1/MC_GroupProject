package com.mcgroupproject.whatsappclone;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class activity_message extends Activity {

    TextView name;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_message);

            name = (TextView)findViewById(R.id.receiver);
            String user = getIntent().getStringExtra("username");
            Toast.makeText(this, user, Toast.LENGTH_LONG);
            name.setText(user);
    }
}

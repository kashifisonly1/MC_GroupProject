package com.mcgroupproject.whatsappclone;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mcgroupproject.whatsappclone.adapter.MessagesAdapter;
import com.mcgroupproject.whatsappclone.model.Message;

import java.util.ArrayList;
import java.util.List;

public class activity_message extends Activity {

    private RecyclerView recyclerView;
    private List<Message> messageList =  new ArrayList<>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_message);

            TextView name = (TextView)findViewById(R.id.receiver);
            String user = getIntent().getStringExtra("username");
            name.setText(user);

            recyclerView = (RecyclerView) findViewById(R.id.messages_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            getMessagesList();
    }

    private void getMessagesList() {

            messageList.add(new Message("1","1","1","this message is not yet sent",1,"12:30AM","1/1/21","1","s1","r1","rep1"));
            messageList.add(new Message("2","2","2","this message sent",2,"01:23AM","2/3/21","2","s2","r2","rep2"));
            messageList.add(new Message("3","3","3","this message delivered",3,"03:43PM","3/23/21","3","s3","r3","rep3"));
            messageList.add(new Message("4","4","4","this is is seen by the receiver",4,"12:00AM","4/12/21","4","s4","r4","rep4"));
            recyclerView.setAdapter(new MessagesAdapter(messageList, this));
    }
}

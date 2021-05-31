package com.mcgroupproject.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mcgroupproject.whatsappclone.Firebase;
import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.model.Message;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.Holder> {
    private List<Message> list;
    private Context context;

    public MessagesAdapter(List<Message> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.messages_template,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Message message = list.get(position);
        holder.text.setText(message.getText());
        holder.time.setText(message.getTime());
        String sent ="@drawable/message_sent";
        String delivered ="@drawable/message_delivered";
        String seen ="@drawable/message_seen";
        String notSent ="@drawable/message_not_sent";
        if(!Firebase.auth.getUid().equals(message.getSenderID()))
        {
            RelativeLayout.LayoutParams lpe = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpe.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            holder.msgBox.setLayoutParams(lpe);
            lpe.setMargins(10,10,10,10);
        }
        else
        {
            RelativeLayout.LayoutParams lpe = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpe.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            holder.msgBox.setLayoutParams(lpe);
            lpe.setMargins(10,10,10,10);
        }
        switch (message.getStatus())
        {
            case 1:
                holder.img.setImageResource(R.drawable.message_not_sent);
                break;
            case 2:
                holder.img.setImageResource(R.drawable.message_sent);
                break;
            case 3:
                holder.img.setImageResource(R.drawable.message_delivered);
                break;
            case 4:
                holder.img.setImageResource(R.drawable.message_seen);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView text,time;
        ImageView img;
        private LinearLayout msgBox;
        public Holder(@NonNull View itemView) {
            super(itemView);
            text= itemView.findViewById(R.id.message_text);
            time = itemView.findViewById(R.id.message_time);
            img = itemView.findViewById(R.id.message_status_symbol);
            msgBox = itemView.findViewById(R.id.message_template_box);
        }
    }
}

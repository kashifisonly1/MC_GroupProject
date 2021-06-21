package com.mcgroupproject.whatsappclone.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseProfileActions;
import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.Model.Message;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.Holder> {
    private List<Message> list;
    private FirebaseProfileActions firebaseProfileActions;
    private Context context;

    public MessagesAdapter(List<Message> list, Context context) {
        this.list = list;
        this.context = context;
        this.firebaseProfileActions = new FirebaseProfileActions(null);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.template_message,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Message message = list.get(position);
        holder.dateArea.setText(message.getDate());
        if(position!=0 && list.get(position-1).getDate().equals(message.getDate())) {
            holder.dateArea.setVisibility(View.GONE);
        }
        else {
            holder.dateArea.setVisibility(View.VISIBLE);
        }
        holder.text.setText(message.getText());
        holder.time.setText(message.getTime());
        if(firebaseProfileActions.GetCurrentUser().getUid().equals(message.getSenderID()))
        {
            RelativeLayout.LayoutParams lpe = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpe.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            lpe.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            lpe.addRule(RelativeLayout.BELOW, holder.dateArea.getId());
            holder.img.setVisibility(View.VISIBLE);
            holder.msgBox.setLayoutParams(lpe);
            lpe.setMargins(10,10,10,10);
            holder.msgBox.setBackgroundColor(Color.rgb(7,94,84));
        }
        else
        {
            RelativeLayout.LayoutParams lpe = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpe.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            lpe.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            lpe.addRule(RelativeLayout.BELOW, holder.dateArea.getId());
            holder.msgBox.setLayoutParams(lpe);
            holder.img.setVisibility(View.GONE);
            lpe.setMargins(10,10,10,10);
            holder.msgBox.setBackgroundColor(Color.rgb(54, 70, 77));
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
        TextView dateArea;
        private LinearLayout msgBox;
        public Holder(@NonNull View itemView) {
            super(itemView);
            dateArea = itemView.findViewById(R.id.date_area_id);
            text= itemView.findViewById(R.id.message_text);
            time = itemView.findViewById(R.id.message_time);
            img = itemView.findViewById(R.id.message_status_symbol);
            msgBox = itemView.findViewById(R.id.message_template_box);
        }
    }
}

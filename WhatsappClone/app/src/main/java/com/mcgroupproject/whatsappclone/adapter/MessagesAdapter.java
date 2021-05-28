package com.mcgroupproject.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        String sent ="";
        String delivered ="";
        String seen ="";
        String notSent ="";
        switch (message.getStatus())
        {
            case 1:
                Glide.with(context).load(seen).into(holder.img);
                break;
            case 2:
                Glide.with(context).load(delivered).into(holder.img);
                break;
            case 3:
                Glide.with(context).load(sent).into(holder.img);
                break;
            case 4:
                Glide.with(context).load(notSent).into(holder.img);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView text,time;
        CircularImageView img;
        public Holder(@NonNull View itemView) {
            super(itemView);
            text= itemView.findViewById(R.id.message_text);
            time = itemView.findViewById(R.id.message_time);
            img = itemView.findViewById(R.id.message_status_symbol);
        }
    }
}

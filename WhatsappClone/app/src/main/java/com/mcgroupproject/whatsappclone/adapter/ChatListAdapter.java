package com.mcgroupproject.whatsappclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mcgroupproject.whatsappclone.Firebase;
import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.activity_message;
import com.mcgroupproject.whatsappclone.model.User;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {
    private List<User> list;
    private Context context;
    private FirebaseStorage storage;
    public ChatListAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_template,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        storage = Firebase.storage;
        User user = list.get(position);
        holder.name.setText(user.getUsername());
        holder.lastMessage.setText((user.getLastMessage()));
        //holder.date.setText(chatList.getDate());
        storage.getReference().child("profile/"+ user.getUserID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(holder.profilePic);
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView name, lastMessage, date;
        private CircularImageView profilePic;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.profile_fname_label);
            lastMessage = itemView.findViewById(R.id.last_message);
            //date = itemView.findViewById(R.id.lastMessageDate);
            profilePic = itemView.findViewById(R.id.profile_picture_chatlist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = list.get(getAdapterPosition());
                    Intent intent = new Intent(context, activity_message.class);
                    intent.putExtra("name",name.getText());
                    intent.putExtra("status", "loading...");
                    intent.putExtra("image", user.getUrlProfile());
                    intent.putExtra("phone", user.getPhone());
                    intent.putExtra("uid", user.getUserID());
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}

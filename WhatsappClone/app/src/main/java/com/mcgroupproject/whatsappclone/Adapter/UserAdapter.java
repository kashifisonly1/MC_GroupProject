package com.mcgroupproject.whatsappclone.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseStorageActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.DownloadImageListener;
import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.Activity.MessageActivity;
import com.mcgroupproject.whatsappclone.Model.User;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Holder> {
    private List<User> list;
    private Context context;
    private FirebaseStorageActions firebaseStorageActions;
    Activity activity;
    public UserAdapter(List<User> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseStorageActions = new FirebaseStorageActions(activity);
        View view = LayoutInflater.from(context).inflate(R.layout.template_user,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        User user = list.get(position);
        holder.name.setText(user.getUsername());
        holder.lastMessage.setText((user.getLastMessage()));
        firebaseStorageActions.DownloadProfileImage(user.getUserID())
                .addOnSuccessListener(new DownloadImageListener(activity, holder.profilePic));
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
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("name",name.getText());
                    intent.putExtra("status", "loading...");
                    intent.putExtra("image", user.getUrlProfile());
                    intent.putExtra("phone", user.getPhone());
                    intent.putExtra("uid", user.getUserID());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}

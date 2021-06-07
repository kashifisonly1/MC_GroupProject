package com.mcgroupproject.whatsappclone.Fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseActions.FirebaseUserDBActions;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.ActionFailureListener;
import com.mcgroupproject.whatsappclone.Firebase.FirebaseListeners.SearchUserListener;
import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.Model.User;
import java.util.List;

public class SearchFragment extends Fragment {
    List<User> users;
    Activity activity;
    public SearchFragment(){}
    public SearchFragment(List<User> users) {
        this.users=users;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        view.findViewById(R.id.searchPhoneBTN).setOnClickListener(v -> {
            EditText t =  view.findViewById(R.id.searchPhoneField);
            FirebaseUserDBActions firebaseUserDBActions = new FirebaseUserDBActions(activity);
            firebaseUserDBActions.SearchUserByPhone(t.getText().toString())
                .addOnCompleteListener(new SearchUserListener(activity, users))
                .addOnFailureListener(new ActionFailureListener(activity));
        });
        return view;
    }
}
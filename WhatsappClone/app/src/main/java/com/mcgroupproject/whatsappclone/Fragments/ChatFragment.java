package com.mcgroupproject.whatsappclone.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.Adapter.UserAdapter;
import com.mcgroupproject.whatsappclone.Model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> list;
    private UserAdapter adapter;
    public ChatFragment(){this.list=new ArrayList<>();}
    public ChatFragment(List<User> list) {
        this.list=list;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getChatList();
        return view;
    }
    public void notifyChange()
    {
        adapter.notifyDataSetChanged();
    }
    private void getChatList() {
            adapter = new UserAdapter(list,getContext(), getActivity());
            recyclerView.setAdapter(adapter);
    }
}
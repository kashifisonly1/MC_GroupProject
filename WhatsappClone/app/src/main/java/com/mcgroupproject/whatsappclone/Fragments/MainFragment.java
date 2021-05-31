package com.mcgroupproject.whatsappclone.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcgroupproject.whatsappclone.R;
import com.mcgroupproject.whatsappclone.adapter.ChatListAdapter;
import com.mcgroupproject.whatsappclone.model.ChatList;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ChatList> list = new ArrayList<>();
    public MainFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getChatList();
        return view;
    }
    private void getChatList() {
        list.add(new ChatList("fDB0q1YfHcSC1wQ5jSvdWeuodeH2","+923176774650","Kashif","Ok, see you soon","1/2/12","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-1/p200x200/155014352_2779390632322575_6048001805041938144_n.jpg?_nc_cat=108&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeFl8FcrooFFhysgjhcl7tAYqDfDwlXjA1OoN8PCVeMDU70k5t9Wijg2rsnW-Yk1tAQ2jP_FuoLBHIDkCew8gmNp&_nc_ohc=HZk3qhB7R8QAX8bSkMI&tn=wC49WAlqcaojgb8w&_nc_ht=scontent.fmux2-1.fna&tp=6&oh=696d4ce6498d68dff3fa2b4c4e326d8e&oe=60D45842"));
        list.add(new ChatList("2","+923209317575","Gullsher","Ok, see you soon","1/2/13","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-1/c0.0.200.200a/p200x200/134265626_3018484121712082_1854137723180140704_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeEN0rDDxRNgw6qqXm5RpHGCKCE4MRpc5YsoITgxGlzli4SUpEtDifDI8dTlWKvP7SV9E2Q6pJKs5udYWHM7Z12W&_nc_ohc=mHsfjuJJuVIAX9EKier&_nc_ht=scontent.fmux2-1.fna&tp=27&oh=c8447be514c7d9eef41f96e41db71215&oe=60D65660"));
        list.add(new ChatList("3","+923209317575","Atif","Ok, see you soon","1/2/14","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-9/120801086_1710820455754358_6431086236380345897_n.jpg?_nc_cat=111&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeEavEbvjXMRmO05uj8HKshbDj_dvno5AtQOP92-ejkC1Czlmapma_ZzqW5nB2hJ7PqdTf9ViEAXZTXxvtxVY_8Y&_nc_ohc=0peDzeKBjFAAX_hoj68&_nc_ht=scontent.fmux2-1.fna&oh=a68503cf1c29506000c22912f06773b0&oe=60D636AD"));
        list.add(new ChatList("4","+923209317575","Usama","Ok, see you soon","1/2/15","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.18169-9/29512375_643250086007886_4191629906057832412_n.jpg?_nc_cat=100&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeEUliQjauMykU40e9sFWwyluBDgPY8vDYq4EOA9jy8NijRpaoDFIsq_0b1OSRqb1obl-t_XAuMZJlLidsLqJWYd&_nc_ohc=_r1-hlDH9QsAX9DdIQY&_nc_ht=scontent.fmux2-1.fna&oh=662d9e9ff60cf2523b5a2d3009b1f1a6&oe=60D64A39"));
        list.add(new ChatList("0fXeReen1nXibjIwJsuR53lBUCv1","+923209317575","Kashif","Ok, see you soon","1/2/12","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-1/p200x200/155014352_2779390632322575_6048001805041938144_n.jpg?_nc_cat=108&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeFl8FcrooFFhysgjhcl7tAYqDfDwlXjA1OoN8PCVeMDU70k5t9Wijg2rsnW-Yk1tAQ2jP_FuoLBHIDkCew8gmNp&_nc_ohc=HZk3qhB7R8QAX8bSkMI&tn=wC49WAlqcaojgb8w&_nc_ht=scontent.fmux2-1.fna&tp=6&oh=696d4ce6498d68dff3fa2b4c4e326d8e&oe=60D45842"));
        list.add(new ChatList("2","+923209317575","Gullsher","Ok, see you soon","1/2/13","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-1/c0.0.200.200a/p200x200/134265626_3018484121712082_1854137723180140704_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeEN0rDDxRNgw6qqXm5RpHGCKCE4MRpc5YsoITgxGlzli4SUpEtDifDI8dTlWKvP7SV9E2Q6pJKs5udYWHM7Z12W&_nc_ohc=mHsfjuJJuVIAX9EKier&_nc_ht=scontent.fmux2-1.fna&tp=27&oh=c8447be514c7d9eef41f96e41db71215&oe=60D65660"));
        list.add(new ChatList("3","+923209317575","Atif","Ok, see you soon","1/2/14","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-9/120801086_1710820455754358_6431086236380345897_n.jpg?_nc_cat=111&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeEavEbvjXMRmO05uj8HKshbDj_dvno5AtQOP92-ejkC1Czlmapma_ZzqW5nB2hJ7PqdTf9ViEAXZTXxvtxVY_8Y&_nc_ohc=0peDzeKBjFAAX_hoj68&_nc_ht=scontent.fmux2-1.fna&oh=a68503cf1c29506000c22912f06773b0&oe=60D636AD"));
        list.add(new ChatList("4","+923209317575","Usama","Ok, see you soon","1/2/15","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.18169-9/29512375_643250086007886_4191629906057832412_n.jpg?_nc_cat=100&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeEUliQjauMykU40e9sFWwyluBDgPY8vDYq4EOA9jy8NijRpaoDFIsq_0b1OSRqb1obl-t_XAuMZJlLidsLqJWYd&_nc_ohc=_r1-hlDH9QsAX9DdIQY&_nc_ht=scontent.fmux2-1.fna&oh=662d9e9ff60cf2523b5a2d3009b1f1a6&oe=60D64A39"));
        list.add(new ChatList("0fXeReen1nXibjIwJsuR53lBUCv1","+923209317575","Kashif","Ok, see you soon","1/2/12","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-1/p200x200/155014352_2779390632322575_6048001805041938144_n.jpg?_nc_cat=108&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeFl8FcrooFFhysgjhcl7tAYqDfDwlXjA1OoN8PCVeMDU70k5t9Wijg2rsnW-Yk1tAQ2jP_FuoLBHIDkCew8gmNp&_nc_ohc=HZk3qhB7R8QAX8bSkMI&tn=wC49WAlqcaojgb8w&_nc_ht=scontent.fmux2-1.fna&tp=6&oh=696d4ce6498d68dff3fa2b4c4e326d8e&oe=60D45842"));
        list.add(new ChatList("2","+923209317575","Gullsher","Ok, see you soon","1/2/13","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-1/c0.0.200.200a/p200x200/134265626_3018484121712082_1854137723180140704_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeEN0rDDxRNgw6qqXm5RpHGCKCE4MRpc5YsoITgxGlzli4SUpEtDifDI8dTlWKvP7SV9E2Q6pJKs5udYWHM7Z12W&_nc_ohc=mHsfjuJJuVIAX9EKier&_nc_ht=scontent.fmux2-1.fna&tp=27&oh=c8447be514c7d9eef41f96e41db71215&oe=60D65660"));
        list.add(new ChatList("3","+923209317575","Atif","Ok, see you soon","1/2/14","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-9/120801086_1710820455754358_6431086236380345897_n.jpg?_nc_cat=111&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeEavEbvjXMRmO05uj8HKshbDj_dvno5AtQOP92-ejkC1Czlmapma_ZzqW5nB2hJ7PqdTf9ViEAXZTXxvtxVY_8Y&_nc_ohc=0peDzeKBjFAAX_hoj68&_nc_ht=scontent.fmux2-1.fna&oh=a68503cf1c29506000c22912f06773b0&oe=60D636AD"));
        list.add(new ChatList("4","+923209317575","Usama","Ok, see you soon","1/2/15","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.18169-9/29512375_643250086007886_4191629906057832412_n.jpg?_nc_cat=100&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeEUliQjauMykU40e9sFWwyluBDgPY8vDYq4EOA9jy8NijRpaoDFIsq_0b1OSRqb1obl-t_XAuMZJlLidsLqJWYd&_nc_ohc=_r1-hlDH9QsAX9DdIQY&_nc_ht=scontent.fmux2-1.fna&oh=662d9e9ff60cf2523b5a2d3009b1f1a6&oe=60D64A39"));
        list.add(new ChatList("0fXeReen1nXibjIwJsuR53lBUCv1","+923209317575","Kashif","Ok, see you soon","1/2/12","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-1/p200x200/155014352_2779390632322575_6048001805041938144_n.jpg?_nc_cat=108&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeFl8FcrooFFhysgjhcl7tAYqDfDwlXjA1OoN8PCVeMDU70k5t9Wijg2rsnW-Yk1tAQ2jP_FuoLBHIDkCew8gmNp&_nc_ohc=HZk3qhB7R8QAX8bSkMI&tn=wC49WAlqcaojgb8w&_nc_ht=scontent.fmux2-1.fna&tp=6&oh=696d4ce6498d68dff3fa2b4c4e326d8e&oe=60D45842"));
        list.add(new ChatList("2","+923209317575","Gullsher","Ok, see you soon","1/2/13","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-1/c0.0.200.200a/p200x200/134265626_3018484121712082_1854137723180140704_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeEN0rDDxRNgw6qqXm5RpHGCKCE4MRpc5YsoITgxGlzli4SUpEtDifDI8dTlWKvP7SV9E2Q6pJKs5udYWHM7Z12W&_nc_ohc=mHsfjuJJuVIAX9EKier&_nc_ht=scontent.fmux2-1.fna&tp=27&oh=c8447be514c7d9eef41f96e41db71215&oe=60D65660"));
        list.add(new ChatList("3","+923209317575","Atif","Ok, see you soon","1/2/14","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.6435-9/120801086_1710820455754358_6431086236380345897_n.jpg?_nc_cat=111&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeEavEbvjXMRmO05uj8HKshbDj_dvno5AtQOP92-ejkC1Czlmapma_ZzqW5nB2hJ7PqdTf9ViEAXZTXxvtxVY_8Y&_nc_ohc=0peDzeKBjFAAX_hoj68&_nc_ht=scontent.fmux2-1.fna&oh=a68503cf1c29506000c22912f06773b0&oe=60D636AD"));
        list.add(new ChatList("4","+923209317575","Usama","Ok, see you soon","1/2/15","https://scontent.fmux2-1.fna.fbcdn.net/v/t1.18169-9/29512375_643250086007886_4191629906057832412_n.jpg?_nc_cat=100&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeEUliQjauMykU40e9sFWwyluBDgPY8vDYq4EOA9jy8NijRpaoDFIsq_0b1OSRqb1obl-t_XAuMZJlLidsLqJWYd&_nc_ohc=_r1-hlDH9QsAX9DdIQY&_nc_ht=scontent.fmux2-1.fna&oh=662d9e9ff60cf2523b5a2d3009b1f1a6&oe=60D64A39"));
        recyclerView.setAdapter(new ChatListAdapter(list,getContext()));
    }

}
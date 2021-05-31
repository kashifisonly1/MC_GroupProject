package com.mcgroupproject.whatsappclone.database;

import com.mcgroupproject.whatsappclone.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageDB {
    public static final List<Message> list = new ArrayList<>();
    public static void Add(Message i)
    {
        for(int a =0; a<list.size(); a++)
        {
            if(list.get(a).getId().equals(i.getId()))
                return;
        }
        list.add(i);
    }
    public static void Remove(Message i)
    {
        list.remove(i);
    }
    public static void Update(String id, String status)
    {
        for(int a =0; a<list.size(); a++)
        {
            if(list.get(a).getId().equals(id))
            {
                list.get(a).setStatus(Integer.parseInt(status));
                return;
            }
        }
    }
    public static List<Message> Get(String uid)
    {
        List<Message> l = new ArrayList<>();
        for(int a =0; a<list.size(); a++)
        {
            Message m = list.get(a);
            if(m.getSenderID().equals(uid) || m.getReceiverID().equals(uid))
            {
                l.add(m);
            }
        }

        return l;
    }
}
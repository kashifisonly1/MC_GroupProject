package com.mcgroupproject.whatsappclone.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mcgroupproject.whatsappclone.model.ChatList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Users {
    public static final List<ChatList> list = new ArrayList<>();
    public static boolean Add(ChatList i)
    {
        for(int a = 0; a<list.size();a++)
        {
            if(list.get(a).getUserID().equals(i.getUserID()))
                return false;
        }
        list.add(i);
        return true;
    }
    public static void Remove(ChatList i)
    {
        list.remove(i);
    }
    public static void Update(ChatList i)
    {
        list.set(list.indexOf(i), i);
    }
    public static List<ChatList> Get()
    {
        List<ChatList> l  = new ArrayList<>();
        for(int i = 0; i<list.size(); i++)
            l.add(list.get(i));
        return l;
    }
    public static int isUserExist(String id)
    {
        for(int i = 0; i<list.size(); i++)
            if(list.get(i).getUserID().equals(id))
                return i;
        return -1;
    }
}

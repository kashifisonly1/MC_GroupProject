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
    public static void Add(ChatList i)
    {
        list.add(i);
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
        return list;
    }
}

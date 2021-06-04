package com.mcgroupproject.whatsappclone.database;

import com.mcgroupproject.whatsappclone.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class Users {
    public static final List<UserModel> list = new ArrayList<>();
    public static boolean Add(UserModel i)
    {
        for(int a = 0; a<list.size();a++)
        {
            if(list.get(a).getUserID().equals(i.getUserID()))
                return false;
        }
        list.add(i);
        return true;
    }
    public static void Remove(UserModel i)
    {
        //TODO: remove user
        list.remove(i);
    }
    public static void Update(UserModel i)
    {
        //TODO: update user
        list.set(list.indexOf(i), i);
    }
    public static List<UserModel> Get()
    {
        List<UserModel> l  = new ArrayList<>();
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

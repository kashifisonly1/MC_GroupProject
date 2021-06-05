package com.mcgroupproject.whatsappclone.database;

import com.mcgroupproject.whatsappclone.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDB {
    public static final List<User> list = new ArrayList<>();
    public static boolean Add(User i)
    {
        for(int a = 0; a<list.size();a++)
        {
            if(list.get(a).getUserID().equals(i.getUserID()))
                return false;
            //ap ne jana ap chaly jao, me kr leta test
            //ek kia hai
            //ye me ab usama ki id se kr rha hun commits
            //
        }
        list.add(i);
        return true;
    }
    public static void Remove(User i)
    {
        //TODO: remove user
        list.remove(i);
    }
    public static void Update(User i)
    {
        //TODO: update user
        list.set(list.indexOf(i), i);
    }
    public static List<User> Get()
    {
        List<User> l  = new ArrayList<>();
        for(int i = 0; i<list.size(); i++)
            l.add(list.get(i));
        return l;
    }
    public static int doesUserExist(String id)
    {
        for(int i = 0; i<list.size(); i++)
            if(list.get(i).getUserID().equals(id))
                return i;
        return -1;
    }
}

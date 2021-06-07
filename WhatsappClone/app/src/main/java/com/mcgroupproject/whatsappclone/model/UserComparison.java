package com.mcgroupproject.whatsappclone.model;

import java.util.Comparator;

public class UserComparison implements Comparator<User> {
        public int compare(User a, User b)
        {
            if(a.getDate()==null || b.getDate()==null)
                return 0;
            return (int)(Long.parseLong(b.getDate())-Long.parseLong(a.getDate()));
        }
}

package com.mcgroupproject.whatsappclone.Utilities;

import com.mcgroupproject.whatsappclone.Model.User;

import java.util.Comparator;

/*
 * Purpose of this function is to keep user updated.. so that user who send last message alwasy stays on the top,,
 * for this we are simply comparing the dates of both user's last message
 * * */

public class UserComparison implements Comparator<User> {
        public int compare(User a, User b)
        {
            if(a.getDate()==null || b.getDate()==null)
                return 0;
            return (int)(Long.parseLong(b.getDate())-Long.parseLong(a.getDate()));
        }
}

package com.mcgroupproject.whatsappclone.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
// obvious as per name
public class DatetimeUtility {
    public static String GetDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d,yyyy", Locale.ENGLISH);
        SimpleDateFormat tdf = new SimpleDateFormat(" h:mm a", Locale.ENGLISH);
        return sdf.format(date);
    }
    public static String GetTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(" h:mm a", Locale.ENGLISH);
        return sdf.format(date);
    }
}

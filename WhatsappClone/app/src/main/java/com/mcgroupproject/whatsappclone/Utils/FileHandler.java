package com.mcgroupproject.whatsappclone.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileHandler {
    public static String getFilePath(Context context, Uri uri)
    {
        return uri.toString();
    }
}

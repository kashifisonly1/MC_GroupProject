package com.mcgroupproject.whatsappclone.Utilities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ImageUtility {
    public static byte[] ImageToBytes(ImageView img) {
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        BitmapDrawable d = (BitmapDrawable) img.getDrawable();
        if(d==null)
            return null;
        Bitmap bitmap = d.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}

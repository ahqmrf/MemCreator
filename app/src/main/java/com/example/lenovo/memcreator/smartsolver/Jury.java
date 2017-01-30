package com.example.lenovo.memcreator.smartsolver;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Lenovo on 1/30/2017.
 */

public class Jury {

    private static int getRatio(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {
        int ratio = 1;
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;

        while (true) {
            if (originalWidth > requiredWidth || originalHeight > requiredHeight) {
                originalWidth /= 2;
                originalHeight /= 2;
                ratio *= 2;
            }
            else break;
        }

        return ratio;
    }

    public static Bitmap getSuitableBitmap(String path, int requiredWidth, int requiredHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path);
        options.inSampleSize = getRatio(options, requiredWidth, requiredHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getSuitableBitmap(Resources resources, int resId, int requiredWidth, int requiredHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = getRatio(options, requiredWidth, requiredHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(resources, resId, options);
    }
}

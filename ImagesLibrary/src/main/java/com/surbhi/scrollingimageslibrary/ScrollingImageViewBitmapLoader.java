package com.surbhi.scrollingimageslibrary;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Surbhi.
 */
public interface ScrollingImageViewBitmapLoader {
    Bitmap loadBitmap(Context context, int resourceId);
}

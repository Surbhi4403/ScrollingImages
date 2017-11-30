package com.surbhi.scrollingimageslibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.surbhi.scrollingimages.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class ScrollingImageView extends View {
    public static ScrollingImageViewBitmapLoader BITMAP_LOADER = new ScrollingImageViewBitmapLoader() {
        @Override
        public Bitmap loadBitmap(Context context, int resourceId) {
            return BitmapFactory.decodeResource(context.getResources(), resourceId);
        }
    };

    private List<Bitmap> bitmaps;
    private float speed;
    private int[] scene;
    private int arrayIndex = 0;
    private int maxBitmapHeight = 0;

    private Rect clipBounds = new Rect();
    private float offset = 0;
    private boolean isStarted = false;

    public ScrollingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ParallaxView, 0, 0);
        try {
            speed = ta.getDimension(R.styleable.ParallaxView_speed, 10);

            int type = isInEditMode() ? TypedValue.TYPE_STRING : ta.peekValue(R.styleable.ParallaxView_src).type;
            if (type == TypedValue.TYPE_REFERENCE) {
                setImages(ta.getResourceId(R.styleable.ParallaxView_src, 0));
            }
        } finally {
            ta.recycle();
        }
        //Start for first time!!
        start();
    }

    public void setImages(int resourceId) {
        TypedArray typedArray = getResources().obtainTypedArray(resourceId);
        try {
            int scene = 0;
            bitmaps = new ArrayList<>();
            this.scene = new int[typedArray.length()];

            for (int i = 0; i < typedArray.length(); i++) {
                Bitmap bitmap = BITMAP_LOADER.loadBitmap(getContext(), typedArray.getResourceId(i, 0));

                int heightScreen = getResources().getDisplayMetrics().heightPixels;
                int widthScreen = getResources().getDisplayMetrics().widthPixels;

                bitmap = Bitmap.createScaledBitmap(bitmap, widthScreen, heightScreen, false);
                bitmaps.add(bitmap);

                maxBitmapHeight = Math.max(bitmap.getHeight(), maxBitmapHeight);

                if (scene > 7)
                    scene = 0;
                this.scene[i] = scene;
                scene++;
            }
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), maxBitmapHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null || bitmaps.isEmpty()) {
            return;
        }

        canvas.getClipBounds(clipBounds);

        while (offset <= -getBitmap(arrayIndex).getWidth()) {
            offset += getBitmap(arrayIndex).getWidth();
            arrayIndex = (arrayIndex + 1) % scene.length;
        }

        float left = offset;
        for (int i = 0; left < clipBounds.width(); i++) {
            Bitmap bitmap = getBitmap((arrayIndex + i) % scene.length);
            int width = bitmap.getWidth();
            canvas.drawBitmap(bitmap, getBitmapLeft(width, left), 0, null);
            left += width;
        }

        if (isStarted && speed != 0) {
            offset -= abs(speed);
            postInvalidateOnAnimation();
        }
    }

    private Bitmap getBitmap(int sceneIndex) {
        return bitmaps.get(scene[sceneIndex]);
    }

    private float getBitmapLeft(float layerWidth, float left) {
        if (speed < 0) {
            return clipBounds.width() - layerWidth - left;
        } else {
            return left;
        }
    }

    /**
     * To start or resume the background animation
     */
    public void start() {
        Log.e("Images", "Started Moving");
        if (!isStarted) {
            isStarted = true;
            postInvalidateOnAnimation();
        }
    }

    /**
     * Check if background is moving.
     * {@link com.surbhi.scrollingimageslibrary.ScrollingImageView#start}
     *
     * @return: true if background moving otherwise false
     */
    public Boolean isMoving() {
        return isStarted;
    }

    /**
     * To stop or pause the background animation
     */
    public void stop() {
        Log.e("Images", "Stopped Moving");
        if (isStarted) {
            isStarted = false;
            invalidate();
        }
    }

    /**
     * Set speed of moving images
     *
     * @param speed: Float value and increase in value will increase the speed of images
     */
    public void setSpeed(float speed) {
        this.speed = speed;
        if (isStarted) {
            postInvalidateOnAnimation();
        }
    }

}

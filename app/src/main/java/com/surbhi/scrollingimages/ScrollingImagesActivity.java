package com.surbhi.scrollingimages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.surbhi.scrollingimageslibrary.ScrollingImageView;

public class ScrollingImagesActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_action;
    ScrollingImageView scrollingImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_images);

        InitializeViews();
    }

    /**
     * Initialization of the views and click-listeners
     */
    private void InitializeViews() {
        button_action = findViewById(R.id.button_action);
        scrollingImageView = findViewById(R.id.scrollView);
        button_action.setOnClickListener(this);

    }

    /**
     * Setup images, Speed and Start Scrolling images
     *
     * @param Resource: Array of images you wanna play in loop
     * @param Speed:    Speed for the images, More the value, more will be speed
     */
    private void SetUpImages(int Resource, float Speed) {
        scrollingImageView.setSpeed(Speed);
        scrollingImageView.setImages(Resource);
    }

    @Override
    public void onClick(View view) {
        if (scrollingImageView.isMoving())
            StopMovingBackground();
        else
            StartMovingBackground();
    }

    /**
     * Setup button and resume state of Scrolling Images
     */
    private void StartMovingBackground() {
        scrollingImageView.start();
        button_action.setText("Pause");
    }

    /**
     * Setup button and pause state of Scrolling Images
     */
    private void StopMovingBackground() {
        scrollingImageView.stop();
        button_action.setText("Resume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scrollingImageView != null) {
            if (scrollingImageView.isMoving())
                scrollingImageView.stop();

            scrollingImageView = null;
        }
    }
}

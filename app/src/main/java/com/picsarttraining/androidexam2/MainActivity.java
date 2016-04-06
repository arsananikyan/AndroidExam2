package com.picsarttraining.androidexam2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private View mainContent;
    private View mainFab;
    private View circleFab;
    private View photoFab;
    private GridView gridView;
    private ImagesAdapter adapter;
    private ArrayList<Uri> imageUris;
    private boolean fabsIsShowing;
    private boolean gridViewIsShowing;
    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mainContent = findViewById(R.id.main_content);
        mainFab = findViewById(R.id.main_fab);
        circleFab = findViewById(R.id.circle_fab);
        photoFab = findViewById(R.id.photo_fab);
        adapter = new ImagesAdapter(this);
        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setY(metrics.heightPixels);
        gridView.setAdapter(adapter);

        fabsIsShowing = false;
        gridViewIsShowing = false;

        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabs();
            }
        });
        photoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleGridAnimation();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toggleGridAnimation();
            }
        });
        imageUris = Utils.getStorageImages(this);
        Utils.loadImagesFromUris(this, imageUris, 90, new Utils.BitmapLoadUpdate() {
            @Override
            public void onProgressUpdate(Bitmap bitmap) {
                adapter.addImage(bitmap);
            }
        });
    }

    private void toggleFabs() {
        if (fabsIsShowing) {
            circleFab.animate().alpha(0.0f).setDuration(300).start();
            circleFab.setClickable(false);
            photoFab.animate().alpha(0.0f).setDuration(300).start();
            photoFab.setClickable(false);
        } else {
            circleFab.animate().alpha(1f).setDuration(300).start();
            circleFab.setClickable(true);
            photoFab.animate().alpha(1f).setDuration(300).start();
            photoFab.setClickable(true);
        }
        fabsIsShowing = !fabsIsShowing;
    }

    private void toggleGridAnimation() {
        if(!gridViewIsShowing)
        {
            ObjectAnimator outAnim = ObjectAnimator.ofFloat(mainContent, "y", 0, -metrics.heightPixels/2);
            ObjectAnimator inAnim = ObjectAnimator.ofFloat(gridView, "y", metrics.heightPixels, metrics.heightPixels/2);
            gridView.setVisibility(View.VISIBLE);
            outAnim.setDuration(500);
            inAnim.setDuration(500);
            outAnim.start();
            inAnim.start();
        }
        else
        {
            ObjectAnimator outAnim = ObjectAnimator.ofFloat(mainContent, "y", -metrics.heightPixels/2,0);
            ObjectAnimator inAnim = ObjectAnimator.ofFloat(gridView, "y", metrics.heightPixels/2,metrics.heightPixels);
            outAnim.setDuration(500);
            inAnim.setDuration(500);
            outAnim.start();
            inAnim.start();
        }
        gridViewIsShowing = !gridViewIsShowing;
    }
}

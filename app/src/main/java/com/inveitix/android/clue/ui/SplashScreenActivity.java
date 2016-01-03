package com.inveitix.android.clue.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.inveitix.android.clue.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Bind(R.id.img_splash)
    ImageView imgSplash;
    private Fade mFade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        imgSplash.startAnimation(fadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgSplash.startAnimation(fadeOut);
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}

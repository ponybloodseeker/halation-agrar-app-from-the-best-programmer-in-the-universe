package com.example.user.hakaton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    public ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setStatusBarColor(getResources().getColor(R.color.DigitalFarmersGreen));

        logo = (ImageView) findViewById(R.id.logo);
        logo.setAlpha(0.0f);
        logo.setTranslationY(48f);
        logo.animate().alpha(1f).
                translationY(0).
                setDuration(500).
                setStartDelay(500).start();

        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        })).start();
    }
}

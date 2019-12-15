package com.example.dressme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashDialogActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        background.start();
    }
}


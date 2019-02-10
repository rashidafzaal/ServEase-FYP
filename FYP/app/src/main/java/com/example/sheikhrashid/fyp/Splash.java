package com.example.sheikhrashid.fyp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class Splash extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Splash.this);
        prefs.edit().clear().commit();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run()
            {
                Intent mainIntent = new Intent(Splash.this,LoginRegister.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

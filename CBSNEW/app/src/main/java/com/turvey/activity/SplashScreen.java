package com.turvey.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.turvey.cbs.R;

public class SplashScreen extends KeepLogin {

    Animation fadein;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        //Animations
        ImageView splashIcon = (ImageView) findViewById(R.id.splashicon);
        TextView splashText = (TextView) findViewById(R.id.splashtext);
        ProgressBar splashLoading = (ProgressBar) findViewById(R.id.splashloading);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        splashIcon.setAnimation(fadein);
        splashText.setAnimation(fadein);
        splashLoading.setAnimation(fadein);
        Handler handler=new Handler();
        Runnable r=new Runnable() {
            public void run() {
                //pref manager
                Boolean Registered;
                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                Registered = sharedPref.getBoolean("Registered", false);
                if (!Registered) {
                    dontLogin();
                } else {
                    startActivity(new Intent(SplashScreen.this, KeepMain.class));

                }
            }
        };
        handler.postDelayed(r, 1800);
    }
    public void dontLogin() {
        startActivity(new Intent(SplashScreen.this, KeepLogin.class));
    }


}

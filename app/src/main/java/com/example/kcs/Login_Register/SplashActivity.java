package com.example.kcs.Login_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kcs.Class.SharedPreferences_data;
import com.example.kcs.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView splash_img;
    private TextView version;
    private Animation slide_down_anim,slide_up_anim,fade_in_anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash_img=findViewById(R.id.splash_img);
        version=findViewById(R.id.id_Version);
        Top_Bg();

    }
    private void Top_Bg() {
        slide_down_anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        slide_up_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        fade_in_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        splash_img.startAnimation(fade_in_anim);
        version.startAnimation(fade_in_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String check=new SharedPreferences_data(SplashActivity.this).getS_email();
                if(check==null||check.isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                else
                {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }

            }
        }, 5000);
    }
}
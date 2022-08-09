package com.example.kcs.Login_Register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.kcs.Fragment.HomeFragment;
import com.example.kcs.MainActivity;
import com.example.kcs.ViewModel.SharedPreferences_data;
import com.example.kcs.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SplashActivity extends AppCompatActivity {

    private ImageView splash_img;
    private TextView version;
    private Animation slide_down_anim,slide_up_anim,fade_in_anim;
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String TAG="SplashActivity";
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




        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users-Id");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                String phone=new SharedPreferences_data(SplashActivity.this).getS_phone_number();
                if(phone==null||phone.isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    if(new SharedPreferences_data(SplashActivity.this).isVerifyOTP())
                    {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    }
                    finish();
                }

                /*String check=new SharedPreferences_data(SplashActivity.this).getS_email();
                if(check==null||check.isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if(new SharedPreferences_data(SplashActivity.this).is_UserGuest())
                    {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    }
                }*/

            }
        }, 5000);

        //share preferences data set empty
        new SharedPreferences_data(getApplicationContext()).setChecked_item_list("");

    }
}
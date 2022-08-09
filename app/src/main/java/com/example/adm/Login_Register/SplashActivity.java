package com.example.adm.Login_Register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SharedPreferences_data;
import com.example.adm.MainActivity;
import com.example.adm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
                }
                finish();



            }
        }, 5000);



    }
}
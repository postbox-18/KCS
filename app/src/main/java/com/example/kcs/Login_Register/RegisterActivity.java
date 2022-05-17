package com.example.kcs.Login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieListener;
import com.example.kcs.Class.MyLog;
import com.example.kcs.Class.SharedPreferences_data;
import com.example.kcs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //primary field
    private EditText user_name, email;
    private TextView already_login;
    private AutoCompleteTextView phone_number, password, re_password;
    private String s_user_name, s_phone_number, s_password, s_re_password, s_email;
    //Tagging
    private String TAG = "RegisterActivity";
    private AppCompatButton register_btn;
    //Lottie anim
    private LottieAnimationView lottie_loading;
    //firebase data authendication
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    //Anim
    private Animation slide_down_anim,slide_up_anim,fade_in_anim;
    private ConstraintLayout bg_banner,head_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //id's
        user_name = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        phone_number = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        re_password = findViewById(R.id.re_password);
        register_btn = findViewById(R.id.register_btn);
        already_login = findViewById(R.id.already_login);
        bg_banner = findViewById(R.id.headings);
        head_layout = findViewById(R.id.head_layout);
        lottie_loading = findViewById(R.id.lottie_loading);
        Top_Bg();
        //lottie
        lottie_loading.setFailureListener(new LottieListener<Throwable>() {
            @Override
            public void onResult(Throwable result) {
                MyLog.e(TAG, "Error:Failure:" + result.getMessage());
            }
        });
        //focus on user_name
        user_name.requestFocus();

        //firebase auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //register onclick
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get string value
                s_user_name = user_name.getText().toString();
                s_phone_number = phone_number.getText().toString();
                s_password = password.getText().toString();
                s_re_password = re_password.getText().toString();
                s_email = email.getText().toString();

                //check the details
                if (CheckDetails()) {
                    mAuth.createUserWithEmailAndPassword(s_email, s_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),
                                                "Registration successful!",
                                                Toast.LENGTH_LONG)
                                                .show();

                                        lottie_loading.setVisibility(View.GONE);
                                        head_layout.setVisibility(View.VISIBLE);

                                        //Next Screen Login
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    } else {

                                        // Registration failed
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Registration failed!!"
                                                        + " Please try again later",
                                                Toast.LENGTH_LONG)
                                                .show();

                                        // hide the progress bar
                                        lottie_loading.setVisibility(View.GONE);
                                        head_layout.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                } else {
                    lottie_loading.setVisibility(View.GONE);
                    head_layout.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "Check the Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //page to login already have a account
        already_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private boolean CheckDetails() {


        //check if value is empty or not

        //check user-name is empty
        if (s_user_name.isEmpty()) {
            user_name.setError("Item cannot be empty");
        }

        //check phone-number
        else if (!isValidPhoneNumber(s_phone_number)) {
            phone_number.setError("Enter a valid phone number");

        }
        //check email-id
        else if (!isValidEmail(s_email)) {
            phone_number.setError("Enter a valid Email id");

        }
        //both password are correct
        else if (!s_password.equals(s_re_password)) {
            re_password.setError("Passwords are not same");
        }
        else if(s_password.length()<7 && s_re_password.length()<7)
        {
            password.setError("Please enter a password");
            re_password.setError("Please enter a password");
        }
        else if(currentUser!=null)
        {
            AlertDialog.Builder alert =new AlertDialog.Builder(this);
            alert.setMessage("This id is Already Login");
            alert.setTitle("Already Login");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog=alert.create();
            alertDialog.show();
        }
        else {
            //shared-preferences
            head_layout.setVisibility(View.GONE);
            lottie_loading.setVisibility(View.VISIBLE);
            lottie_loading.playAnimation();
            new SharedPreferences_data(this).setS_user_name(s_user_name);
            new SharedPreferences_data(this).setS_phone_number(s_phone_number);
            new SharedPreferences_data(this).setS_password(s_password);
            new SharedPreferences_data(this).setS_email(s_email);

            return true;
        }
        return false;

    }
//check valid email id
    private boolean isValidEmail(String s_email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s_email).matches();
    }

    //check valid phone-number
    private boolean isValidPhoneNumber(String s_phone_number) {
        return android.util.Patterns.PHONE.matcher(s_phone_number).matches();

    }
    //anim
    private void Top_Bg() {
        slide_down_anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        slide_up_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        fade_in_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        /*splash_img.startAnimation(fade_in_anim);
        version.startAnimation(fade_in_anim);*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               /* Intent intent = new Intent(RegisterActivity.this, NextActivity.class);
                startActivity(intent);*/

            }
        }, 5000);
    }

}
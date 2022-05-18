package com.example.kcs.Login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieListener;
import com.example.kcs.Classes.LoadingDialogs;
import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.MainActivity;
import com.example.kcs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    //primary field
    private EditText email;
    private AutoCompleteTextView password;
    private AppCompatButton login_btn;
    private TextView no_account;
    private String s_email, s_password;
    private String TAG="LoginActivity";
    private CheckBox remember_me;
    private boolean check_password=false;
    private String s_check_box;
    //firebase auth
    private FirebaseAuth mAuth;
    //Lottie anim
    private LottieAnimationView lottie_loading;
    //anim
    private Animation slide_down_anim,slide_up_anim,fade_in_anim;
    private ConstraintLayout bg_banner,head_layout;
    private LoadingDialogs loadingDialog=new LoadingDialogs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //id's
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        bg_banner = findViewById(R.id.headings);
        head_layout = findViewById(R.id.head_layout);
        lottie_loading = findViewById(R.id.lottie_loading);
        no_account = findViewById(R.id.no_account);
        remember_me = findViewById(R.id.remember_me);

        Top_Bg();
        //checkBox remember me
        s_check_box=new SharedPreferences_data(LoginActivity.this).getBoolen_check();
        if(s_check_box!=null) {
            if (s_check_box.equals("true")) {
                MyLog.e(TAG, "logout>>Check condition>>" + s_check_box);
                login();
            } else if (s_check_box.equals("false")) {
                MyLog.e(TAG, "logout>>Check is condition>>" + s_check_box);
                SharedPreferences_data.logout_User();
            }
        }

        //lottie
        lottie_loading.setFailureListener(new LottieListener<Throwable>() {
            @Override
            public void onResult(Throwable result) {
                MyLog.e(TAG, "Error:Failure:" + result.getMessage());
            }
        });
        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        //login btn onclick
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_email = email.getText().toString();
                s_password = password.getText().toString();
                if (CheckDeatils()) {

                    mAuth.signInWithEmailAndPassword(s_email, s_password)
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(
                                                @NonNull Task<AuthResult> task)
                                        {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Login successful!!",
                                                        Toast.LENGTH_LONG)
                                                        .show();

                                                loadingDialog.dismiss();


                                                if(remember_me.isChecked())
                                                {
                                                    MyLog.e(TAG,"logout>>Check box checked>>"+remember_me.isChecked());
                                                    check_password=true;

                                                }
                                                else
                                                {
                                                    MyLog.e(TAG,"logout>>Check box not checked>>"+remember_me.isChecked());
                                                    SharedPreferences_data.logout_User();
                                                }

                                                login();
                                            }

                                            else {

                                                // sign-in failed
                                                Toast.makeText(getApplicationContext(),
                                                        "Login failed!!",
                                                        Toast.LENGTH_LONG)
                                                        .show();

                                                loadingDialog.dismiss();
                                            }
                                        }
                                    });
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Please check the values", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //page to register can't have a account
        no_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    private void login() {
        // if sign-in is successful
        // intent to home activity
        new SharedPreferences_data(getApplicationContext()).setEnter_password(s_password);
        new SharedPreferences_data(getApplicationContext()).setBoolen_check(String.valueOf(check_password));
        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        startActivity(intent);

    }

    private boolean CheckDeatils() {
        //check details
        if (!isValidEmail(s_email))
        {
            email.setError("Please enter valid Email id");
        }
        else if(s_password.isEmpty() )
        {
            password.setError("Please enter a password");
        }
        else if(s_password.length()<7)
        {
            password.setError("Please enter a valid password");
        }
        else
        {
            loadingDialog.show(getSupportFragmentManager(),"Loading dailog");

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
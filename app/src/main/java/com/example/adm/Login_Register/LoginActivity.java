package com.example.adm.Login_Register;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieListener;
import com.example.adm.Classes.LoadingDialogs;
import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SharedPreferences_data;
import com.example.adm.MainActivity;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    //primary field
    private EditText email;
    private AutoCompleteTextView password;
    private AppCompatButton login_btn;
    private TextView no_account,forgot;
    private String s_email, s_password;
    private String TAG = "LoginActivity";
    private CheckBox remember_me;
    private boolean check_password = false;
    private boolean check_email = false;
    private String s_check_box;
    //firebase auth
    private FirebaseAuth mAuth;
    //Lottie anim
    private LottieAnimationView lottie_loading;
    //anim
    private Animation slide_down_anim, slide_up_anim, fade_in_anim;
    private ConstraintLayout bg_banner, head_layout;
    private LoadingDialogs loadingDialog = new LoadingDialogs();
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private GetViewModel getViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getViewModel = new ViewModelProvider(this).get(GetViewModel.class);

        //id's
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        bg_banner = findViewById(R.id.headings);
        head_layout = findViewById(R.id.head_layout);
        lottie_loading = findViewById(R.id.lottie_loading);
        no_account = findViewById(R.id.no_account);
        remember_me = findViewById(R.id.remember_me);
        forgot = findViewById(R.id.forgot);

        firebaseDatabase = FirebaseDatabase.getInstance();


        Top_Bg();
        //checkBox remember me
        s_check_box = new SharedPreferences_data(LoginActivity.this).getBoolen_check();
        if (s_check_box != null) {
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
                loadingDialog.show(getSupportFragmentManager(), "Loading dailog");
                if (CheckDeatils()) {

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
        getViewModel.getEmailMutable().observe(LoginActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                //check details
                if (!aBoolean) {
                    loadingDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setMessage("Something Went Problem Please Try Again Later");
                    alert.setTitle("Problem");
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                } else {
                    MyLog.e(TAG, "login>>" + aBoolean);
                    Auth();
                }

            }
        });

        //onclick forgot password
        //forgot on click
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Recover Password");
                LinearLayout linearLayout=new LinearLayout(LoginActivity.this);
                final EditText emailet= new EditText(LoginActivity.this);

                // write the email using which you registered
                if((email.getText().toString()).isEmpty())
                {
                    emailet.setHint("Email");
                }
                else {
                    emailet.setText(email.getText().toString());
                }
                emailet.setMinEms(16);
                emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                linearLayout.addView(emailet);
                linearLayout.setPadding(10,10,10,10);
                builder.setView(linearLayout);

                // Click on Recover and a email will be sent to your registered email id
                builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email=emailet.getText().toString().trim();
                        beginRecovery(email);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });


    }
    private void beginRecovery(String email) {

        loadingDialog.show(getSupportFragmentManager(), "Loading dailog");

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingDialog.dismiss();
                if(task.isSuccessful())
                {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(LoginActivity.this,"Done sent",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this,"Error Occurred",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismiss();
                Toast.makeText(LoginActivity.this,"Error Failed",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void Auth() {
        mAuth.signInWithEmailAndPassword(s_email, s_password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                            /*FireseBaseDataDetails(s_email);
                                            getViewModel.setEmail(s_email);
                                            getViewModel.getEmailMutable().observe(LoginActivity.this, new Observer<Boolean>() {
                                                @Override
                                                public void onChanged(Boolean aBoolean) {
                                                    check_email=aBoolean;
                                                }
                                            });
                                            if (task.isSuccessful()&&check_email) {*/

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    loadingDialog.dismiss();

                                    if (remember_me.isChecked()) {
                                        MyLog.e(TAG, "logout>> remember me is checked");
                                        MyLog.e(TAG, "logout>>Check box checked>>" + remember_me.isChecked());
                                        check_password = true;

                                    } else {
                                        MyLog.e(TAG, "logout>>Check box not checked>>" + remember_me.isChecked());
                                        SharedPreferences_data.logout_User();

                                    }

                                    login();
                                } else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    loadingDialog.dismiss();
                                }


                            }
                        });
    }

    /*private void FireseBaseDataDetails(String s_email) {
        databaseReference = firebaseDatabase.getReference("Users-Id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "snap>>" + snapshot);
                for (DataSnapshot datas : snapshot.getChildren()) {
                  *//*  MyLog.e(TAG, "snap>>" + datas.child("username").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("email").getValue().toString());*//*
                    MyLog.e(TAG, "error>>at firebase  emails " + datas.child("email").getValue().toString());
                    if(Objects.equals(s_email, datas.child("email").getValue().toString())) {
                        new SharedPreferences_data(getApplicationContext()).setS_email(datas.child("email").getValue().toString());
                        new SharedPreferences_data(getApplicationContext()).setS_user_name(datas.child("username").getValue().toString());
                        new SharedPreferences_data(getApplicationContext()).setS_phone_number(datas.child("phone_number").getValue().toString());

                                check_email=true;
                        MyLog.e(TAG, "error>>at firebase  emails "+check_email);
                        break;
                    }
                    else
                    {
                        check_email=false;
                        MyLog.e(TAG, "error>>at firebase  emails "+check_email);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }*/

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
        if (!isValidEmail(s_email)) {
            loadingDialog.dismiss();
            MyLog.e(TAG, "error>>e_email is not valid");
            email.setError("Please enter valid Email id");
        } else if (s_password.isEmpty()) {
            loadingDialog.dismiss();
            MyLog.e(TAG, "error>>password is empty");
            password.setError("Please enter a password");
        } else if (s_password.length() < 7) {
            loadingDialog.dismiss();
            MyLog.e(TAG, "error>>pass is <7");
            password.setError("Please enter a valid password");
        } else {
            MyLog.e(TAG, "error>>success");
            getViewModel.setEmail(s_email);
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
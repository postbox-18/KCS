package com.example.kcs.Login_Register;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.kcs.Classes.CheckPhoneNumber;
import com.example.kcs.DialogFragment.LoadingDialogs;
import com.example.kcs.Classes.MyLog;
import com.example.kcs.ViewModel.SharedPreferences_data;
import com.example.kcs.MainActivity;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    //primary field
    private AutoCompleteTextView otp, phone_number;
    private AppCompatButton login_btn;
    private TextView no_account, forgot, msg;
    private ImageView send_otp;
    private ProgressBar progress_bar;
    // verification ID
    private String verificationId;
    private boolean verifyOTP = false;
    private String s_phone_number;
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
    private ConstraintLayout head_layout;
    private ImageView bg_banner;
    private LoadingDialogs loadingDialog = new LoadingDialogs();
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private GetViewModel getViewModel;
    private List<CheckPhoneNumber> checkPhoneNumbers = new ArrayList<>();
    private boolean check_phone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getViewModel = new ViewModelProvider(this).get(GetViewModel.class);

        //id's
        phone_number = findViewById(R.id.phone_number);
        otp = findViewById(R.id.otp);
        send_otp = findViewById(R.id.send_otp);
        progress_bar = findViewById(R.id.progress_bar);
        msg = findViewById(R.id.msg);
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

        //check if phone number is 10 digit
        phone_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                MyLog.e(TAG, "otp>>afterTextChanged>>" + s);
                int counts = s.length();
                MyLog.e(TAG, "otp>>afterTextChanged>>counts>>" + counts);
                MyLog.e(TAG, "otp>>afterTextChanged>>phone_number>>" + phone_number.getText().toString());
                if (counts == 10) {
                    isValidPhoneNumber(phone_number.getText().toString());
                    send_otp.setVisibility(View.VISIBLE);
                } else {
                    send_otp.setVisibility(View.GONE);

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });

        //otp verify
        otp.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                MyLog.e(TAG, "otp>>afterTextChanged>>" + s);
                int counts = s.length();
                MyLog.e(TAG, "otp>>afterTextChanged>>counts>>" + counts);
                MyLog.e(TAG, "otp>>afterTextChanged>>phone_number>>" + otp.getText().toString());
                if (counts == 6) {
                    verifyCode(otp.getText().toString());
                }
                send_otp.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });


        //click on send_otp
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = "+91" + phone_number.getText().toString();

                if (check_phoneNumberExistsInDataBase(phone_number.getText().toString())) {
                    sendVerificationCode(phoneNumber);
                    progress_bar.setVisibility(View.VISIBLE);
                    send_otp.setVisibility(View.GONE);
                    otp.setVisibility(View.VISIBLE);

                } else {

                    //alert dialog
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setMessage("You Haven't Register Yet.");
                    alert.setTitle("Alert");
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
                }
            }

            private boolean check_phoneNumberExistsInDataBase(String phoneNumber) {
                //checkemail list in data base
                getViewModel.getCheckPhoneNumberMutableLiveData().observe(LoginActivity.this, new Observer<List<CheckPhoneNumber>>() {
                    @Override
                    public void onChanged(List<CheckPhoneNumber> checkPhoneNumbers1) {
                        checkPhoneNumbers = checkPhoneNumbers1;
                        for (int i = 0; i < checkPhoneNumbers1.size(); i++) {
                            if (phoneNumber.equals(checkPhoneNumbers1.get(i).getPhone_number())) {
                                check_phone = true;
                                MyLog.e(TAG, "check_phone>>if>>" + check_phone);
                                MyLog.e(TAG, "check_phone>>if>>" + phoneNumber + "==" + checkPhoneNumbers1.get(i).getPhone_number());

                                break;

                            } else {
                                MyLog.e(TAG, "check_phone>>else>>" + check_phone);
                                MyLog.e(TAG, "check_phone>>else>>" + phoneNumber + "==" + checkPhoneNumbers1.get(i).getPhone_number());

                                check_phone = false;
                                continue;
                            }
                        }
                    }
                });
                MyLog.e(TAG, "check_phone>>" + check_phone);
                return check_phone;
            }
        });



       /* getViewModel.getphoneNumberMutable().observe(LoginActivity.this, new Observer<Boolean>() {
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
        });*/

        //login btn onclick
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.show(getSupportFragmentManager(), "Loading dailog");
                if (verifyOTP) {
                    login();
                    getViewModel.GetUserDeatils(phone_number.getText().toString());
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
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

        //forgot on click
        /*forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Recover Password");
                LinearLayout linearLayout = new LinearLayout(LoginActivity.this);
                final EditText emailet = new EditText(LoginActivity.this);

                // write the email using which you registered
                if ((email.getText().toString()).isEmpty()) {
                    emailet.setHint("Email");
                } else {
                    emailet.setText(email.getText().toString());
                }
                emailet.setMinEms(16);
                emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                linearLayout.addView(emailet);
                linearLayout.setPadding(10, 10, 10, 10);
                builder.setView(linearLayout);

                // Click on Recover and a email will be sent to your registered email id
                builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = emailet.getText().toString().trim();
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
        });*/


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
                if (task.isSuccessful()) {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(LoginActivity.this, "Done sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Error Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*private void Auth() {
        mAuth.signInWithEmailAndPassword(s_email, s_password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                            *//*FireseBaseDataDetails(s_email);
                                            getViewModel.setEmail(s_email);
                                            getViewModel.getEmailMutable().observe(LoginActivity.this, new Observer<Boolean>() {
                                                @Override
                                                public void onChanged(Boolean aBoolean) {
                                                    check_email=aBoolean;
                                                }
                                            });
                                            if (task.isSuccessful()&&check_email) {*//*

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
                                        SharedPreferences_data.set_IsGuest(true);


                                    } else {
                                        SharedPreferences_data.set_IsGuest(false);
                                        MyLog.e(TAG, "logout>>Check box not checked>>" + remember_me.isChecked());
                                        SharedPreferences_data.logout_User();

                                    }

                                    login();
                                } else {
                                    SharedPreferences_data.set_IsGuest(false);

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    loadingDialog.dismiss();
                                }


                            }
                        });
    }*/

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

        new SharedPreferences_data(getApplicationContext()).setS_phone_number(phone_number.getText().toString());
        SharedPreferences_data.setVerifyOTP(true);

        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();

    }

    /*private boolean CheckDeatils() {
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
            MyLog.e(TAG, "errors>> continue login");
            getViewModel.setEmail(s_email);
            return true;
        }
        return false;
    }*/

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

    //OTP
    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)         // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)         // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        progress_bar.setVisibility(View.GONE);
        send_otp.setVisibility(View.VISIBLE);

    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                //otp.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                MyLog.e(TAG, "valid>>code>>" + code);
                verifyCode(code);
            } else {
                otp.setError("Please enter the OTP");
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            msg.setText(e.getMessage() + "!");
            Log.e(TAG, "error>>186>>" + e.getMessage());
            send_otp.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.GONE);


        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting
        // credentials from our verification id and code.
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            // after getting credential we are
            // calling sign in method.
            signInWithCredential(credential);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            msg.setText(e.getMessage() + "!");
            send_otp.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.GONE);

        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            /*Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();*/
                            verifyOTP = true;
                            SharedPreferences_data.setVerifyOTP(true);
                            login_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn));
                            login_btn.setClickable(true);
                            MyLog.e(TAG, "valid>>verifyOTP>>" + verifyOTP);
                        } else {
                            verifyOTP = false;
                            SharedPreferences_data.setVerifyOTP(false);
                            login_btn.setClickable(false);
                            MyLog.e(TAG, "valid>>verifyOTP>>" + verifyOTP);
                            login_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_silver));

                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            msg.setText(String.valueOf(task.getException()) + "!");
                            send_otp.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.GONE);
                            Log.e(TAG, "valid>>117>>" + task.getException());
                        }
                    }
                });
    }

}
package com.example.kcs.Login_Register;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.kcs.Classes.CheckPhoneNumber;
import com.example.kcs.DialogFragment.LoadingDialogs;
import com.example.kcs.Classes.MyLog;
import com.example.kcs.MainActivity;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    //primary field
    private EditText user_name, email;
    //private TextView  msg;
    private TextView already_login;
    //private AutoCompleteTextView  otp;
    private AutoCompleteTextView phone_number;
    //private ImageView send_otp;
    //private ProgressBar progress_bar;
    private String s_user_name, s_phone_number, s_email;
    private boolean verifyOTP = false;
    //Tagging
    private String TAG = "RegisterActivity";
    //private AppCompatButton register_btn;
    private AppCompatButton request_SMS;
    //Lottie anim
    //private LottieAnimationView lottie_loading;
    // verification ID
    private String verificationId;
    //firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    //Anim
    private Animation slide_down_anim, slide_up_anim, fade_in_anim;
    private ConstraintLayout head_layout;
    private ImageView bg_banner;

    //loading
    private LoadingDialogs loadingDialog = new LoadingDialogs();

    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private GetViewModel getViewModel;
    //check mail
    private List<CheckPhoneNumber> checkPhoneNumbers = new ArrayList<>();
    private boolean check_phone = false;

    //private List<OTP_VerifyUsers> otp_verifies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getViewModel = new ViewModelProvider(this).get(GetViewModel.class);
        //id's
        user_name = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        phone_number = findViewById(R.id.phone_number);
       /* otp = findViewById(R.id.otp);
        send_otp = findViewById(R.id.send_otp);
        progress_bar = findViewById(R.id.progress_bar);
        msg = findViewById(R.id.msg);*/

        //otp_verifies = new ArrayList<>();

        //register_btn = findViewById(R.id.register_btn);
        request_SMS = findViewById(R.id.request_SMS);
        already_login = findViewById(R.id.already_login);
        bg_banner = findViewById(R.id.headings);
        head_layout = findViewById(R.id.head_layout);
        //lottie_loading = findViewById(R.id.lottie_loading);

        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebase auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Top_Bg();

        //intent from otp activity
        String s = getIntent().getStringExtra("OTP_VerifyUsers");
        MyLog.e(TAG, "number>>84>>" + s);
        if (s == null) {
        } else {
            String[] str = (s).split("!");
            s_email = str[0];
            s_user_name = str[1];
            s_phone_number = str[2];
            email.setText(s_email);
            phone_number.setText(s_phone_number);
            user_name.setText(s_user_name);
        }


        //lottie
        /*lottie_loading.setFailureListener(new LottieListener<Throwable>() {
            @Override
            public void onResult(Throwable result) {
                MyLog.e(TAG, "Error:Failure:" + result.getMessage());
            }
        });*/


        //focus on user_name
        user_name.requestFocus();


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
                    //send_otp.setVisibility(View.VISIBLE);
                    request_SMS.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn));
                    request_SMS.setClickable(true);
                } else {
                    request_SMS.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_silver));

                    //send_otp.setVisibility(View.GONE);

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

        /*getViewModel.getOtp_verifiesLive().observe(this, new Observer<List<OTP_VerifyUsers>>() {
            @Override
            public void onChanged(List<OTP_VerifyUsers> otp_verifyUsers) {
                MyLog.e(TAG,"number>>164>>"+new GsonBuilder().setPrettyPrinting().create().toJson(otp_verifyUsers));

                email.setText(otp_verifyUsers.get(0).getEmail());
                phone_number.setText(otp_verifyUsers.get(0).getPhone_Number());
                user_name.setText(otp_verifyUsers.get(0).getName());
            }
        });*/

        //page to login already have a account
        already_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        //request to send sms
        request_SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_user_name = user_name.getText().toString();
                s_phone_number = phone_number.getText().toString();
                s_email = email.getText().toString();
                if (CheckDetails()) {

                    String phoneNumber = "+91" + phone_number.getText().toString();

                    if (check_phoneNumberExistsInDataBase(phone_number.getText().toString())) {
                        //alert dialog
                        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                        alert.setMessage("You have already Register.");
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
                    } else {


                        /*otp_verifies.clear();
                        OTP_VerifyUsers otp_verifyUsers = new OTP_VerifyUsers();
                        otp_verifyUsers.setEmail(s_email);
                        otp_verifyUsers.setPhone_Number(s_phone_number);
                        otp_verifyUsers.setName(s_user_name);
                        otp_verifies.add(otp_verifyUsers);*/

                        String s = s_email + "!" + s_user_name + "!" + s_phone_number;
                        Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                        intent.putExtra("OTP_VerifyUsers", s);

                        startActivity(intent);
                        //getViewModel.setOtp_verifies(otp_verifies);

                    /*sendVerificationCode(phoneNumber);
                    progress_bar.setVisibility(View.VISIBLE);
                    send_otp.setVisibility(View.GONE);
                    otp.setVisibility(View.VISIBLE);*/

                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please Enter The Details", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean check_phoneNumberExistsInDataBase(String phoneNumber) {
                //checkemail list in data base
                getViewModel.getCheckPhoneNumberMutableLiveData().observe(RegisterActivity.this, new Observer<List<CheckPhoneNumber>>() {
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

        //otp verify
        /*otp.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                MyLog.e(TAG, "otp>>afterTextChanged>>" + s);
                int counts = s.length();
                MyLog.e(TAG, "otp>>afterTextChanged>>counts>>" + counts);
                MyLog.e(TAG, "otp>>afterTextChanged>>phone_number>>" + otp.getText().toString());
                if (counts == 6) {
                    verifyCode(otp.getText().toString());
                    send_otp.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.GONE);
                } else {
                    send_otp.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.GONE);

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
        });*/


        //click on send_otp
        /*send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = "+91" + phone_number.getText().toString();

                if (check_phoneNumberExistsInDataBase(phone_number.getText().toString())) {
                    //alert dialog
                    AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                    alert.setMessage("You have already Register.");
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
                } else {
                    sendVerificationCode(phoneNumber);
                    progress_bar.setVisibility(View.VISIBLE);
                    send_otp.setVisibility(View.GONE);
                    otp.setVisibility(View.VISIBLE);

                }
            }

            private boolean check_phoneNumberExistsInDataBase(String phoneNumber) {
                //checkemail list in data base
                getViewModel.getCheckPhoneNumberMutableLiveData().observe(RegisterActivity.this, new Observer<List<CheckPhoneNumber>>() {
                    @Override
                    public void onChanged(List<CheckPhoneNumber> checkPhoneNumbers1) {
                        checkPhoneNumbers = checkPhoneNumbers1;
                        for (int i = 0; i < checkPhoneNumbers1.size(); i++) {
                            if (phoneNumber.equals(checkPhoneNumbers1.get(i).getPhone_number())) {
                                check_phone = true;
                                MyLog.e(TAG,"check_phone>>if>>"+check_phone);
                                MyLog.e(TAG,"check_phone>>if>>"+phoneNumber+"=="+checkPhoneNumbers1.get(i).getPhone_number());

                                break;

                            } else {
                                MyLog.e(TAG,"check_phone>>else>>"+check_phone);
                                MyLog.e(TAG,"check_phone>>else>>"+phoneNumber+"=="+checkPhoneNumbers1.get(i).getPhone_number());

                                check_phone = false;
                                continue;
                            }
                        }
                    }
                });
                MyLog.e(TAG,"check_phone>>"+check_phone);
                return check_phone;
            }
        });*/


        //register onclick
        /*register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get string value
                s_user_name = user_name.getText().toString();
                s_phone_number = phone_number.getText().toString();
                s_email = email.getText().toString();

                //check the details
                if (CheckDetails()) {

                    SaveDataBase();

                } else {
                    //Toast.makeText(RegisterActivity.this, "Check the Details", Toast.LENGTH_SHORT).show();
                    // Registration failed
                    Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed!!"
                                            + " Please try again later",
                                    Toast.LENGTH_LONG)
                            .show();

                    // hide the progress bar
                    //loadingDialog.dismiss();
                }
            }

            private void SaveDataBase() {
                databaseReference = firebaseDatabase.getReference("Users-Id");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MyLog.e(TAG, "snap>>" + snapshot);
                        databaseReference.child(s_phone_number).child("email").setValue(s_email);
                        databaseReference.child(s_phone_number).child("phone_number").setValue(s_phone_number);
                        databaseReference.child(s_phone_number).child("username").setValue(s_user_name);

                        new SharedPreferences_data(RegisterActivity.this).setS_user_name(s_user_name);
                        new SharedPreferences_data(RegisterActivity.this).setS_phone_number(s_phone_number);
                        //new SharedPreferences_data(RegisterActivity.this).setS_password(s_password);
                        new SharedPreferences_data(RegisterActivity.this).setS_email(s_email);

                        String msg = "New Registrations Email:" + s_email + " Name:" + s_user_name + " PhoneNumber:" + s_phone_number;
                        getViewModel.PushNotify("New Registrations", msg);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RegisterActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                        MyLog.e(TAG, "fail to get data " + error.getMessage());
                    }
                });
                //Next Screen Login
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }

        });*/


        /*getViewModel.getEmailMutable().observe(RegisterActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                //check details
                if (aBoolean)
                {
                    loadingDialog.dismiss();
                    AlertDialog.Builder alert =new AlertDialog.Builder(RegisterActivity.this);
                    alert.setMessage("You have already Register");
                    alert.setTitle("Alert");
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
                    Auth();
                }

            }
        });*/
    }

    private boolean CheckDetails() {


        //checkemail list in data base
     /*   getViewModel.getCheckEmailsMutableLiveData().observe(this, new Observer<List<CheckEmail>>() {
            @Override
            public void onChanged(List<CheckEmail> checkEmails1) {
                checkEmails = checkEmails1;
                for (int i = 0; i < checkEmails1.size(); i++) {
                    if (s_email.equals(checkEmails1.get(i).getEmail())) {
                        check_email = true;
                        break;
                    } else {
                        check_email = false;
                        continue;
                    }
                }
            }
        });*/


        //check user-name is empty
        if (s_user_name.isEmpty()) {
            user_name.setError("Item cannot be empty");
            MyLog.e(TAG, "valid>>user_name>>");
        }

        //check phone-number
        else if (!isValidPhoneNumber(s_phone_number)) {
            phone_number.setError("Enter a valid phone number");
            MyLog.e(TAG, "valid>>s_phone_number>>");


        }
        //check email-id
        else if (!isValidEmail(s_email)) {
            email.setError("Enter a valid Email id");
            MyLog.e(TAG, "valid>>s_email>>");


        } else {
            MyLog.e(TAG, "valid>>");

            //shared-preferences
            loadingDialog.show(getSupportFragmentManager(), "Loading dailog");
            MyLog.e(TAG, "errors>> continue regi");
            return true;
        }
        return false;


    }

    /*private void Auth() {
        mAuth.createUserWithEmailAndPassword(s_email, s_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                            "Registration successful!",
                                            Toast.LENGTH_LONG)
                                    .show();

                            loadingDialog.dismiss();
                            databaseReference = firebaseDatabase.getReference("Users-Id");
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    MyLog.e(TAG, "snap>>" + snapshot);
                                    databaseReference.child(s_phone_number).child("email").setValue(s_email);
                                    databaseReference.child(s_phone_number).child("phone_number").setValue(s_phone_number);
                                    databaseReference.child(s_phone_number).child("username").setValue(s_user_name);

                                    new SharedPreferences_data(RegisterActivity.this).setS_user_name(s_user_name);
                                    new SharedPreferences_data(RegisterActivity.this).setS_phone_number(s_phone_number);
                                    new SharedPreferences_data(RegisterActivity.this).setS_password(s_password);
                                    new SharedPreferences_data(RegisterActivity.this).setS_email(s_email);
                                    
                                    String msg="New Registrations Email:"+s_email+" Name:"+s_user_name+" PhoneNumber:"+s_phone_number;
                                    getViewModel.PushNotify("New Registrations",msg);
                                    
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(RegisterActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                                    MyLog.e(TAG, "fail to get data " + error.getMessage());
                                }
                            });
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
                            loadingDialog.dismiss();
                        }
                    }
                });
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


}
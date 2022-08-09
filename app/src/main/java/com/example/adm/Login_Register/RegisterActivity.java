package com.example.adm.Login_Register;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.adm.Classes.CheckPhoneNumber;
import com.example.adm.Classes.LoadingDialogs;
import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SharedPreferences_data;
import com.example.adm.MainActivity;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    //primary field
    private EditText user_name, email;
    private TextView already_login,msg;
    private AutoCompleteTextView phone_number, otp;
    private ImageView send_otp;
    private ProgressBar progress_bar;
    private String s_user_name, s_phone_number, s_email;
    private boolean verifyOTP = false;
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
    // verification ID
    private String verificationId;

    //loading
    private LoadingDialogs loadingDialog=new LoadingDialogs();

    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private GetViewModel getViewModel;
    //check mail
    private List<CheckPhoneNumber> checkPhoneNumbers =new ArrayList<>();
    private boolean check_phone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getViewModel = new ViewModelProvider(this).get(GetViewModel.class);
        //id's
        user_name = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        phone_number = findViewById(R.id.phone_number);
        otp = findViewById(R.id.otp);
        send_otp = findViewById(R.id.send_otp);
        progress_bar = findViewById(R.id.progress_bar);
        msg = findViewById(R.id.msg);
        register_btn = findViewById(R.id.register_btn);
        already_login = findViewById(R.id.already_login);
        bg_banner = findViewById(R.id.headings);
        head_layout = findViewById(R.id.head_layout);
        lottie_loading = findViewById(R.id.lottie_loading);

        firebaseDatabase = FirebaseDatabase.getInstance();



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
        });


        //click on send_otp
        send_otp.setOnClickListener(new View.OnClickListener() {
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
                }else if (check_phoneNumberExistsInUserBase(phone_number.getText().toString())) {
                    //alert dialog
                    AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                    alert.setMessage("You have already Register in Users App.");
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
                        checkPhoneNumbers=new ArrayList<>();
                        checkPhoneNumbers =checkPhoneNumbers1;
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
            private boolean check_phoneNumberExistsInUserBase(String phoneNumber) {

                //checkemail list in data base
                getViewModel.getCheckUserPhoneNumberMutableLiveData().observe(RegisterActivity.this, new Observer<List<CheckPhoneNumber>>() {
                    @Override
                    public void onChanged(List<CheckPhoneNumber> checkPhoneNumbers1) {
                        checkPhoneNumbers=new ArrayList<>();
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



        });




        //register onclick
        register_btn.setOnClickListener(new View.OnClickListener() {
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
                databaseReference = firebaseDatabase.getReference("Admin");
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

        });
        //page to login already have a account
        already_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

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
                            databaseReference = firebaseDatabase.getReference("Admin");
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
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(RegisterActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
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

    /*private boolean CheckDetails() {


        //checkemail list in data base
        getViewModel.getCheckEmailsMutableLiveData().observe(this, new Observer<List<CheckPhoneNumber>>() {
            @Override
            public void onChanged(List<CheckPhoneNumber> checkEmails1) {
                checkPhoneNumbers =checkEmails1;
                for(int i=0;i<checkEmails1.size();i++) {
                    if (s_email.equals(checkEmails1.get(i).getEmail()))
                    {
                        check_phone=true;
                        break;
                    }
                    else
                    {
                        check_phone=false;
                        continue;
                    }
                }
            }
        });

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
        else if(check_phone)
        {

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
            //shared-preferences
            loadingDialog.show(getSupportFragmentManager(),"Loading dailog");

            return true;
        }
        return false;

    }*/
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


        } /*else if (check_email) {
            AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
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
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }*/ else if (!verifyOTP) {
            MyLog.e(TAG, "valid>>verifyOTP>>");

        } else {
            MyLog.e(TAG, "valid>>");

            //shared-preferences
            loadingDialog.show(getSupportFragmentManager(), "Loading dailog");
            MyLog.e(TAG, "errors>> continue regi");
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
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            msg.setText(e.getMessage()+"!");
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
                            register_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn));
                            register_btn.setClickable(true);
                            MyLog.e(TAG, "valid>>verifyOTP>>" + verifyOTP);
                        } else {
                            verifyOTP = false;
                            SharedPreferences_data.setVerifyOTP(false);
                            register_btn.setClickable(false);
                            MyLog.e(TAG, "valid>>verifyOTP>>" + verifyOTP);
                            register_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_silver));

                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            msg.setText(String.valueOf(task.getException())+"!");
                            send_otp.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.GONE);
                            Log.e(TAG, "valid>>117>>" + task.getException());
                        }
                    }
                });
    }

}
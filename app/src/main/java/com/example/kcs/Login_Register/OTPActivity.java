package com.example.kcs.Login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.example.kcs.Classes.MyLog;
import com.example.kcs.MainActivity;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.ViewModel.SharedPreferences_data;
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
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    private PinEntryEditText pinEntry;
    private AppCompatButton verify;
    private ImageView back_btn;
    private TextView re_sendSMS;
    private String TAG = "OTPActivity";
    private GetViewModel getViewModel;
    //firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    // verification ID
    private String verificationId;
    private boolean verifyOTP = false;
    private String s_user_name, s_phone_number, s_email;

    //private List<OTP_VerifyUsers> otp_verifyUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel = new ViewModelProvider(this).get(GetViewModel.class);

        setContentView(R.layout.otp_verify);

        pinEntry = findViewById(R.id.otp);
        verify = findViewById(R.id.verify);
        back_btn = findViewById(R.id.back_btn);
        re_sendSMS = findViewById(R.id.re_sendSMS);

        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebase auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        String s=getIntent().getStringExtra("OTP_VerifyUsers");
        MyLog.e(TAG, "number>>84>>" +s);

        String[]str=(s).split("!");
        s_email=str[0];
        s_user_name=str[1];
        s_phone_number=str[2];
        if(s_phone_number==null)
        {
            Toast.makeText(this, "Phone is invalid or Empty", Toast.LENGTH_SHORT).show();
        }
        else {
            //check otp phone number
            sendVerificationCode("+91" + s_phone_number);
        }
        /*getViewModel.getOtp_verifiesLive().observe(this, new Observer<List<OTP_VerifyUsers>>() {
            @Override
            public void onChanged(List<OTP_VerifyUsers> otp_verifyUsers1) {
                otp_verifyUsers = otp_verifyUsers1;
                MyLog.e(TAG, "number>>88>>" + new GsonBuilder().setPrettyPrinting().create().toJson(otp_verifyUsers1));

                s_user_name = otp_verifyUsers.get(0).getName();
                s_email = otp_verifyUsers.get(0).getEmail();
                s_phone_number = otp_verifyUsers.get(0).getPhone_Number();
                //check otp phone number
                sendVerificationCode("+91 " + s_phone_number);
            }
        });*/

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s_user_name == null) {
                    startActivity(new Intent(OTPActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(OTPActivity.this, RegisterActivity.class));
                }
            }
        });

        re_sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode("+91" + s_phone_number);

            }
        });

        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {

                    String login_pin = pinEntry.getText().toString();
                    MyLog.e(TAG, "pin>>" + login_pin + "==" + login_pin);
                    verifyCode(login_pin);

                }
            });
        }

        //verify onclick
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check the details
                if (CheckDetails()) {

                    if (s_user_name == null) {
                        startActivity(new Intent(OTPActivity.this, MainActivity.class));


                    } else {
                        SaveDataBase();
                    }

                } else {
                    //Toast.makeText(OTPActivity.this, "Check the Details", Toast.LENGTH_SHORT).show();
                    // Registration failed
                    Toast.makeText(
                                    getApplicationContext(),
                                    "Verification failed!!"
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

                        new SharedPreferences_data(OTPActivity.this).setS_user_name(s_user_name);
                        new SharedPreferences_data(OTPActivity.this).setS_phone_number(s_phone_number);
                        //new SharedPreferences_data(OTPActivity.this).setS_password(s_password);
                        new SharedPreferences_data(OTPActivity.this).setS_email(s_email);

                        String msg = "New Registrations Email:" + s_email + " Name:" + s_user_name + " PhoneNumber:" + s_phone_number;
                        getViewModel.PushNotify("New Registrations", msg);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OTPActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                        MyLog.e(TAG, "fail to get data " + error.getMessage());
                    }
                });
                //Next Screen Login
                startActivity(new Intent(OTPActivity.this, MainActivity.class));
                finish();
            }

        });

    }


    private boolean CheckDetails() {
        if (!verifyOTP) {
            MyLog.e(TAG, "valid>>verifyOTP>>");

        } else {
            MyLog.e(TAG, "valid>>");

            //shared-preferences
            //loadingDialog.show(getSupportFragmentManager(), "Loading dailog");
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

    //OTP
    private void sendVerificationCode(String number) {
        MyLog.e(TAG, "number>>225>>" + number);
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
            Toast.makeText(OTPActivity.this, "OTP Send Successfully", Toast.LENGTH_SHORT).show();
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
                pinEntry.setError("Please enter the OTP");
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            //msg.setText(e.getMessage() + "!");
            String s = e.getMessage() + " !";
            //getAlertDialog(s);
            Log.e(TAG, "error>>186>>" + e.getMessage());
            /*send_otp.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.GONE);*/


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
            String s = (e.getMessage() + " !");
            //getAlertDialog(s);


        }
    }

    private void getAlertDialog(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(getApplication());
                alert.setMessage(s);
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
        });

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
                            /*Intent i = new Intent(OTPActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();*/
                            verifyOTP = true;
                            SharedPreferences_data.setVerifyOTP(true);
                            verify.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn));
                            verify.setClickable(true);
                            MyLog.e(TAG, "valid>>verifyOTP>>" + verifyOTP);
                        } else {
                            verifyOTP = false;
                            SharedPreferences_data.setVerifyOTP(false);
                            verify.setClickable(false);
                            MyLog.e(TAG, "valid>>verifyOTP>>" + verifyOTP);
                            verify.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_silver));

                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(OTPActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            String s = (String.valueOf(task.getException()) + " !");

                            //getAlertDialog(s);

                            /*send_otp.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.GONE);*/
                            Log.e(TAG, "valid>>117>>" + task.getException());
                        }
                    }
                });
    }
}
package com.example.adm.Login_Register;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
import com.example.adm.Classes.CheckEmail;
import com.example.adm.Classes.LoadingDialogs;
import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SharedPreferences_data;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    //loading
    private LoadingDialogs loadingDialog=new LoadingDialogs();

    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private GetViewModel getViewModel;
    //check mail
    private List<CheckEmail> checkEmails=new ArrayList<>();
    private boolean check_email = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getViewModel = new ViewModelProvider(this).get(GetViewModel.class);
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
                    Auth();
                } else {

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

    private void Auth() {
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
    }

    private boolean CheckDetails() {


        //checkemail list in data base
        getViewModel.getCheckEmailsMutableLiveData().observe(this, new Observer<List<CheckEmail>>() {
            @Override
            public void onChanged(List<CheckEmail> checkEmails1) {
                checkEmails=checkEmails1;
                for(int i=0;i<checkEmails1.size();i++) {
                    if (s_email.equals(checkEmails1.get(i).getEmail()))
                    {
                        check_email=true;
                        break;
                    }
                    else
                    {
                        check_email=false;
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
        else if(check_email)
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
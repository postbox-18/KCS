package com.example.adm.Fragments.Control_Panel.PhoneNumFrags;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm.Classes.CheckPhoneNumber;
import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SharedPreferences_data;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhoneNumberControlPanelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneNumberControlPanelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerview_phone_number;
    private List<CheckPhoneNumber> checkPhoneNumberList = new ArrayList<>();
    private AdapterPhoneNumberControlPanel adapterPhoneNumberControlPanel;

    private LinearLayout primary_layout, edit_layout;
    private EditText phone_numberEdit;
    private TextView phone_number;
    private ImageView back_btn, edit;
    private AppCompatButton edit_btn;
    private String verificationId;
    private List<CheckPhoneNumber> checkPhoneNumbers = new ArrayList<>();
    private boolean check_phone = false;
    private boolean verifyOTP = false;
    private String TAG = "PhoneNumberControlPanelFragment";
    private GetViewModel getViewModel;
    private ImageView send_otp;
    private ProgressBar progress_bar;
    private int n = 0;

    //verify otp in alertBox
    private EditText otp;
    private AppCompatButton verify_otp,cancel;


    private FirebaseAuth mAuth;


    public PhoneNumberControlPanelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhoneNumberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneNumberControlPanelFragment newInstance(String param1, String param2) {
        PhoneNumberControlPanelFragment fragment = new PhoneNumberControlPanelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phone_number, container, false);
        getViewModel = new ViewModelProvider(getActivity()).get(GetViewModel.class);


        back_btn = view.findViewById(R.id.back_btn);

        //primary layer
        edit = view.findViewById(R.id.edit);
        phone_number = view.findViewById(R.id.phone_number);

        //edit layer
        phone_numberEdit = view.findViewById(R.id.phone_numberEdit);
        edit_layout = view.findViewById(R.id.edit_layout);
        primary_layout = view.findViewById(R.id.primary_layout);
        send_otp = view.findViewById(R.id.send_otp);
        progress_bar = view.findViewById(R.id.progress_bar);
        edit_btn = view.findViewById(R.id.edit_btn);


        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        getViewModel.getAdmin_PrimaryLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                phone_number.setText(s);
            }
        });

        //click on edit btn to edit the admin primary number , send otp for check whether is valid or not
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String phoneNumber = "+91" + phone_number.getText().toString();
                if (check_phoneNumberExistsInDataBase(phone_number.getText().toString())) {
                    sendVerificationCode(phoneNumber);
                    alertBox(phone_number.getText().toString(), 1);

                } else {

                    //alert dialog
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
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


        });
        //login btn onclick
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyOTP) {
                    getViewModel.SetAdmin_primary(phone_numberEdit.getText().toString());
                } else {
                    Toast.makeText(getContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //click on send_otp

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = "+91" + phone_numberEdit.getText().toString();

                if (check_phoneNumberExistsInDataBase(phone_numberEdit.getText().toString())) {
                    sendVerificationCode(phoneNumber);
                    n = 2;
                    alertBox(phone_number.getText().toString(), 2);
                    progress_bar.setVisibility(View.VISIBLE);
                    send_otp.setVisibility(View.GONE);
                    otp.setVisibility(View.VISIBLE);

                } else {

                    //alert dialog
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
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

        });

        //check if phone number is 10 digit
        phone_numberEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                MyLog.e(TAG, "otp>>afterTextChanged>>" + s);
                int counts = s.length();
                MyLog.e(TAG, "otp>>afterTextChanged>>counts>>" + counts);
                MyLog.e(TAG, "otp>>afterTextChanged>>phone_number>>" + phone_numberEdit.getText().toString());
                if (counts == 10) {
                    isValidPhoneNumber(phone_numberEdit.getText().toString());
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



       /* recyclerview_phone_number=view.findViewById(R.id.recyclerview_phone_number);
        recyclerview_phone_number.setHasFixedSize(true);
        recyclerview_phone_number.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        checkPhoneNumberList=new ArrayList<>();


        //get phoneNumber
        getViewModel.getCheckPhoneNumberMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<CheckPhoneNumber>>() {
            @Override
            public void onChanged(List<CheckPhoneNumber> checkPhoneNumbers1) {
                checkPhoneNumberList=checkPhoneNumbers1;
                adapterPhoneNumberControlPanel=new AdapterPhoneNumberControlPanel(getContext(),getViewModel,checkPhoneNumberList);
                recyclerview_phone_number.setAdapter(adapterPhoneNumberControlPanel);


            }
        });*/


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(5);
            }
        });
        return view;
    }

    //check valid phone-number
    private boolean isValidPhoneNumber(String s_phone_number) {
        return android.util.Patterns.PHONE.matcher(s_phone_number).matches();

    }

    private boolean check_phoneNumberExistsInDataBase(String phoneNumber) {
        //checkemail list in data base
        getViewModel.getCheckPhoneNumberMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<CheckPhoneNumber>>() {
            @Override
            public void onChanged(List<CheckPhoneNumber> checkPhoneNumbers1) {
                checkPhoneNumbers = new ArrayList<>();
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

    private void alertBox(String phoneNumber, int m) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.verify_otp, null);
        otp = customLayout.findViewById(R.id.otp);
        verify_otp = customLayout.findViewById(R.id.verify_otp);
        cancel = customLayout.findViewById(R.id.cancel);






        builder.setView(customLayout);

        // add a button
           /* builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();


                }
            });*/

        // add a button
        /*builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });*/

        AlertDialog dialog
                = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                if (m == 1) {
                    primary_layout.setVisibility(View.GONE);
                    edit_layout.setVisibility(View.VISIBLE);
                } else if (m == 2) {
                    primary_layout.setVisibility(View.VISIBLE);
                    edit_layout.setVisibility(View.GONE);
                }

            }
        });
    }


    //OTP
    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)         // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
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
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            MyLog.e(TAG, "error>>186>>" + e.getMessage());
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
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            if (n == 2) {
                                edit_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn));
                                edit_btn.setClickable(true);
                            }
                            verify_otp.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn));
                            verify_otp.setClickable(true);



                            MyLog.e(TAG, "valid>>verifyOTP>>" + verifyOTP);
                        } else {
                            verifyOTP = false;
                            SharedPreferences_data.setVerifyOTP(false);
                            if (n == 2) {
                                edit_btn.setClickable(false);
                                edit_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_silver));
                            }
                            verify_otp.setClickable(false);
                            verify_otp.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_silver));


                            MyLog.e(TAG, "valid>>verifyOTP>>" + verifyOTP);
                            send_otp.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.GONE);

                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            MyLog.e(TAG, "valid>>117>>" + task.getException());
                        }
                    }
                });
    }
}
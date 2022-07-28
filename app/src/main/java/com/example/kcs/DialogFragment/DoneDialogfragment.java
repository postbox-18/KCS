package com.example.kcs.DialogFragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieListener;
import com.example.kcs.Classes.MyLog;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.ViewModel.SharedPreferences_data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoneDialogfragment extends DialogFragment {
    //Lottie anim
    private LottieAnimationView lottie_loading;
    private TextView loading_text;
    private AppCompatButton ok;
    private String TAG="DoneDialogfragment";
    private GetViewModel getViewModel;
    private String username,func_title,sess,date,time,count,phonenumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getViewModel = new ViewModelProvider(getActivity()).get(GetViewModel.class);
        View view = inflater.inflate(R.layout.dialog_done, container, false);

        lottie_loading=view.findViewById(R.id.lottie_loading);
        loading_text=view.findViewById(R.id.loading_text);
        ok=view.findViewById(R.id.ok);

        lottie_loading.setAnimation(R.raw.done);
        lottie_loading.playAnimation();
        

        //get user name
        username=new SharedPreferences_data(getContext()).getS_user_name();
        //get phone number
        phonenumber=new SharedPreferences_data(getContext()).getS_phone_number();

        //get func title
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                func_title=s;
            }
        });
        //get sess
         getViewModel.getSession_titleMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
             @Override
             public void onChanged(String s) {
                 sess=s;
             }
         });
        //get date
        getViewModel.getDate_pickerMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                date=s;
            }
        });
        //get time
        getViewModel.getTime_pickerMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                time=s;
            }
        });
        //get count
        getViewModel.getS_countLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                count=s;

            }
        });


        loading_text.setText("Successfully Saved");

        lottie_loading.setFailureListener(new LottieListener<Throwable>() {
            @Override
            public void onResult(Throwable result) {
                MyLog.e(TAG, "Error:Failure:" + result.getMessage());
            }
        });

        //btn click
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify();
                getViewModel.setI_value(0);
                getViewModel.setRefresh(0);
                dismiss();
            }
        });

        return view;
    }
    private void Notify() {

        //notify
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Notifications");

        MyLog.e(TAG, "placeorders>>date>>" + date);
        MyLog.e(TAG, "placeorders>>time>>" + time);

        String s = sess + "!" + time + "-" + count + "_true";
        date=date.replace("/","-");
        MyLog.e(TAG, "placeorders>>ses  time>>" + s);
        String msg = "Orders Placed by " + username +" (contact number: "+phonenumber+ ") and Function is " + func_title + " session is " + sess + " at " + date + " Time is " + time + " Total Count is " + count;
        MyLog.e(TAG, "notify>>" + msg);
        DatabaseReference databaseReference1=databaseReference.push();
        databaseReference1.child(date).setValue(msg);
        getViewModel.PushNotify("Orders Placed",msg);

    }

}



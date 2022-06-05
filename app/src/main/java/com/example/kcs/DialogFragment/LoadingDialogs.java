package com.example.kcs.DialogFragment;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieListener;
import com.example.kcs.Classes.MyLog;
import com.example.kcs.R;

public class LoadingDialogs extends DialogFragment {
    //Lottie anim
    private LottieAnimationView lottie_loading;
    private TextView loading_text;
    private String TAG="LoadingDialogs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, container, false);

        lottie_loading=view.findViewById(R.id.lottie_loading);
        loading_text=view.findViewById(R.id.loading_text);

        loading_text.setText("Loading..");
        lottie_loading.setAnimation(R.raw.cooks);
        lottie_loading.playAnimation();

        lottie_loading.setFailureListener(new LottieListener<Throwable>() {
            @Override
            public void onResult(Throwable result) {
                MyLog.e(TAG, "Error:Failure:" + result.getMessage());
            }
        });

        return view;
    }



}

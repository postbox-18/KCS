package com.example.kcs.Classes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieListener;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

public class DoneDialogfragment extends DialogFragment {
    //Lottie anim
    private LottieAnimationView lottie_loading;
    private TextView loading_text;
    private String TAG="DoneDialogfragment";
    private GetViewModel getViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getViewModel = new ViewModelProvider(getActivity()).get(GetViewModel.class);
        View view = inflater.inflate(R.layout.dialog_done, container, false);

        lottie_loading=view.findViewById(R.id.lottie_loading);
        loading_text=view.findViewById(R.id.loading_text);

        lottie_loading.setAnimation(R.raw.done);
        lottie_loading.playAnimation();
        

        loading_text.setText("Successfully Saved");

        lottie_loading.setFailureListener(new LottieListener<Throwable>() {
            @Override
            public void onResult(Throwable result) {
                MyLog.e(TAG, "Error:Failure:" + result.getMessage());
            }
        });

        return view;
    }

}



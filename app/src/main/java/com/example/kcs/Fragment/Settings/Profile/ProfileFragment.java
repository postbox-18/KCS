package com.example.kcs.Fragment.Settings.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Login_Register.LoginActivity;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.ViewModel.SharedPreferences_data;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String TAG="ProfileFragment";

    //primary field's
    private ImageView pic;
    private TextView user_name,email;
    private CardView log_out;
    private ImageView back_btn;
    private CardView my_orders_card;
    private GetViewModel getViewModel;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel = new ViewModelProvider(getActivity()).get(GetViewModel.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        //id's
        pic=view.findViewById(R.id.pic);
        user_name=view.findViewById(R.id.user_name);
        email=view.findViewById(R.id.email);
        log_out=view.findViewById(R.id.logout);
        back_btn=view.findViewById(R.id.back_btn);
        my_orders_card=view.findViewById(R.id.my_orders_card);

        //setText
        user_name.setText(new SharedPreferences_data(getContext()).getS_user_name());
        email.setText(new SharedPreferences_data(getContext()).getS_email());

        //set func title null
        getViewModel.setFunc_title("");

        //onclick
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.e(TAG,"logout>> btn clicked");
                SharedPreferences_data.logout_User();
                new SharedPreferences_data(getContext()).setBoolen_check("false");
                SharedPreferences_data.setVerifyOTP(false);
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);


            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(0);
            }
        });
        my_orders_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(4);
            }
        });

        return view;
    }
}
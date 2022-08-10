package com.example.adm.Fragments.Control_Panel.PhoneNumFrags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adm.Classes.CheckPhoneNumber;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

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
    private List<CheckPhoneNumber> checkPhoneNumberList=new ArrayList<>();
    private AdapterPhoneNumberControlPanel adapterPhoneNumberControlPanel;
    private GetViewModel getViewModel;
    private ImageView back_btn;


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
        View view= inflater.inflate(R.layout.fragment_phone_number, container, false);
        getViewModel = new ViewModelProvider(getActivity()).get(GetViewModel.class);

        back_btn=view.findViewById(R.id.back_btn);
        recyclerview_phone_number=view.findViewById(R.id.recyclerview_phone_number);


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
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(5);
            }
        });
        return view;
    }
}
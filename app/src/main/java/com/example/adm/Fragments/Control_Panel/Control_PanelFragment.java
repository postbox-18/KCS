package com.example.adm.Fragments.Control_Panel;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Control_PanelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Control_PanelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private GetViewModel getViewModel;
    private CardView phone_number_Cardview,header_Cardview;



    private String TAG="Control_PanelFragment";
    public Control_PanelFragment() {
        // Required empty public constructor
    }
    public static Control_PanelFragment newInstance(String param1, String param2) {
        Control_PanelFragment fragment = new Control_PanelFragment();
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
        View view= inflater.inflate(R.layout.fragment_control__panel, container, false);
        getViewModel = new ViewModelProvider(getActivity()).get(GetViewModel.class);

        header_Cardview=view.findViewById(R.id.header_Cardview);
        phone_number_Cardview=view.findViewById(R.id.phone_number_Cardview);

        header_Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(1);
            }
        });

        phone_number_Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(6);
            }
        });

        return view;
    }

}
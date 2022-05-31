package com.example.kcs.Fragment.Session;

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
import android.widget.TextView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Header.HeaderAdapter;
import com.example.kcs.Fragment.Header.HeaderList;
import com.example.kcs.Fragment.Items.ItemList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SessionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String funList_title;
    private TextView fun_title;
    private String TAG="SessionFragment";
    //Header
    private RecyclerView recyclerview_session;
    private SessionAdapter sessionAdapter;
    private ImageView back_btn;
    private List<SessionList> sessionList=new ArrayList<>();
    //private MyViewModel myViewModel;
    private GetViewModel getViewModel;

    public SessionFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SessionFragment newInstance(String param1, String param2) {
        SessionFragment fragment = new SessionFragment();
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
        View view= inflater.inflate(R.layout.fragment_session, container, false);
        recyclerview_session=view.findViewById(R.id.recyclerview_session);
        fun_title=view.findViewById(R.id.fun_title);
        back_btn=view.findViewById(R.id.back_btn);

        //get view model
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                MyLog.e(TAG,"sessionlist>>fun>"+s);
                funList_title=s;
                fun_title.setText(funList_title);
            }
        });


        //get session list
        getViewModel.getSessionListMutable().observe(getViewLifecycleOwner(), new Observer<List<SessionList>>() {
            @Override
            public void onChanged(List<SessionList> sessionLists1) {
                sessionList=sessionLists1;
                recyclerview_session.setHasFixedSize(true);
                recyclerview_session.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                sessionAdapter=new SessionAdapter(getContext(),sessionList,getViewModel);
                recyclerview_session.setAdapter(sessionAdapter);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(6);
            }
        });

        return view;

    }
}
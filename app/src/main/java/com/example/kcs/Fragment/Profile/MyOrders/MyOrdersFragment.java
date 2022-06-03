package com.example.kcs.Fragment.Profile.MyOrders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
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
import com.example.kcs.Classes.SharedPreferences_data;

import com.example.kcs.Fragment.Profile.MyOrders.BottomSheet.ViewCartAdapterSession;
import com.example.kcs.Fragment.Profile.MyOrders.MyOrdersItems.MyOrdersAdapter;
import com.example.kcs.Fragment.Profile.MyOrders.MyOrdersItems.MyOrdersList;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.Classes.MyOrderFuncList;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView back_btn;
    private GetViewModel getViewModel;
    private RecyclerView recyclerview_my_orders;
    private List<MyOrdersList> myOrdersList = new ArrayList<>();
    private List<MyOrderFuncList> myOrderFuncLists = new ArrayList<>();
    private MyOrdersAdapter myOrdersAdapter;
    private String header, func_title, s_user_name;
    private String item = "";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String TAG = "MyOrdersFragment";
    private LinkedHashMap<String, List<MyOrdersList>> myordersHashMap = new LinkedHashMap<>();

    //bottom sheet view
    private RecyclerView recyclerview_order_session_deatils;
    private List<SessionList> sessionLists=new ArrayList<>();
    private TextView func;
    private LinkedHashMap<String, List<SessionList>> stringListLinkedHashMap=new LinkedHashMap<>();

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        back_btn = view.findViewById(R.id.back_btn);
        recyclerview_my_orders = view.findViewById(R.id.recyclerview_my_orders);


        recyclerview_my_orders.setHasFixedSize(true);
        recyclerview_my_orders.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myOrdersList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        s_user_name = new SharedPreferences_data(getContext()).getS_user_name();

        //to load data in my order details
        getViewModel.GetMyOrdersDetails(s_user_name);

        //get Func name list
        getViewModel.getMyOrderFuncListsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<MyOrderFuncList>>() {
            @Override
            public void onChanged(List<MyOrderFuncList> myOrderFuncLists1) {
                myOrderFuncLists = myOrderFuncLists1;
                myOrdersAdapter = new MyOrdersAdapter(getContext(), myOrderFuncLists, getViewModel);
                recyclerview_my_orders.setAdapter(myOrdersAdapter);
            }
        });

        //Bottom sheet
        BottomSheetDialog bottomSheet = new BottomSheetDialog(requireContext());
        View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_order_details, null);
        recyclerview_order_session_deatils = bottom_view.findViewById(R.id.recyclerview_order_session_deatils);
        func = bottom_view.findViewById(R.id.func_title);


        //get session hash map  List
        //get session list
        getViewModel.getSs_f_mapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, List<SessionList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SessionList>> stringListLinkedHashMap1) {
                stringListLinkedHashMap=stringListLinkedHashMap1;


            }
        });


        //get func_title  to view item list
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null && !s.isEmpty()) {
                    func_title=s;
                    func.setText(s);
                    MyLog.e(TAG, "func_title>>string>>" + s);
                    bottomSheet.setContentView(bottom_view);
                    bottomSheet.show();

                    //get session list
                    MyLog.e(TAG,"SessionList>>stringListLinkedHashMap>>>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(stringListLinkedHashMap));
                    MyLog.e(TAG,"SessionList>>deatils>>"+s_user_name+"\t\t"+func_title);
                    sessionLists=stringListLinkedHashMap.get(s_user_name+"-"+func_title);
                    MyLog.e(TAG,"SessionList>>sessionLists>>>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(sessionLists));


                    recyclerview_order_session_deatils.setHasFixedSize(true);
                    recyclerview_order_session_deatils.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    ViewCartAdapterSession viewCartAdapter = new ViewCartAdapterSession(getContext(), getViewModel,s,sessionLists);
                    recyclerview_order_session_deatils.setAdapter(viewCartAdapter);
                } else {
                    MyLog.e(TAG, "func_title>> orderItemView list null");
                }

            }
        });



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(3);
            }
        });
        return view;
    }
}
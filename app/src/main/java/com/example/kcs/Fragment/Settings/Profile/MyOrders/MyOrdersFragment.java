package com.example.kcs.Fragment.Settings.Profile.MyOrders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
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

import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.OrderItemLists;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.ViewCartAdapterSession;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrdersItems.MyOrdersAdapter;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrdersItems.MyOrdersList;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

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
    private List<SelectedHeader> selectedHeaders = new ArrayList<>();
    private List<MyOrderFuncList> myOrderFuncLists = new ArrayList<>();
    private MyOrdersAdapter myOrdersAdapter;
    private String header, func_title, s_user_name,func_session_title;
    private String item = "";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String TAG = "MyOrdersFragment";

    private LinkedHashMap<String, List<MyOrdersList>> myordersHashMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<SelectedHeader>> selectedHeaderMap=new LinkedHashMap<>();

    //bottom sheet view
    private RecyclerView recyclerview_order_session_deatils;
    private List<SelectedSessionList> selectedSessionLists=new ArrayList<>();
    private TextView func;
    //private LinkedHashMap<String, List<SelectedSessionList>> stringListLinkedHashMap=new LinkedHashMap<>();

    //order hashmap
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> orderFunc_Map = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> s_orderFunc_Map = new LinkedHashMap<>();
    //header map
    private LinkedHashMap<String, List<OrderItemLists>> orderHeaderMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap = new LinkedHashMap<>();




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

        getViewModel.setFunc_Session(null);

        //get order func hash map
        getViewModel.getOrderFunc_MapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> stringLinkedHashMapLinkedHashMap) {
                orderFunc_Map=new LinkedHashMap<>(stringLinkedHashMapLinkedHashMap);
                s_orderFunc_Map=new LinkedHashMap<>(stringLinkedHashMapLinkedHashMap);

                //get session title
                Set<String> set = orderFunc_Map.keySet();
                List<String> aList1 = new ArrayList<String>(set.size());
                for (String x1 : set)
                    aList1.add(x1);
                myOrderFuncLists.clear();
                for(int i=0;i<aList1.size();i++)
                {
                    MyOrderFuncList list=new MyOrderFuncList(
                      aList1.get(i)
                    );
                    myOrderFuncLists.add(list);
                }
                myOrdersAdapter = new MyOrdersAdapter(getContext(), myOrderFuncLists, getViewModel,orderFunc_Map);
                MyLog.e(TAG,"orders>>map main source>>"+new GsonBuilder().setPrettyPrinting().create().toJson(orderFunc_Map));

                recyclerview_my_orders.setAdapter(myOrdersAdapter);
            }
        });


        ///////////////////////*************BOTTOMSHEET DIALOG**************************///////////////////

        BottomSheetDialog bottomSheet = new BottomSheetDialog(requireContext());
        View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_order_details, null);
        recyclerview_order_session_deatils = bottom_view.findViewById(R.id.recyclerview_order_session_deatils);
        func = bottom_view.findViewById(R.id.func_title);


        //get func session title to show item list
        getViewModel.getFunc_SessionMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                MyLog.e(TAG,"orders>>map bottom sheet>>"+new GsonBuilder().setPrettyPrinting().create().toJson(orderFunc_Map));
                func_session_title=s;
                //click on session adapter cardview
                if (func_session_title != null && !func_session_title.isEmpty()) {
                    MyLog.e(TAG,"myord>>func>>"+func_session_title);
                    //String s = func_title + "/" + sessionLists1.getSession_title()+"!"+sessionLists1.getDate_time()+"_"+sessionLists1.getBolen();
                    String[] str=func_session_title.split("/");
                    func_title=str[0];
                    func.setText(func_title);

                    bottomSheet.setContentView(bottom_view);
                    bottomSheet.show();
                    MyLog.e(TAG,"orders>>change orderFunc_Map session funcv uif>>"+new GsonBuilder().setPrettyPrinting().create().toJson(s_orderFunc_Map));


                    //get order sessionMap
                    orderSessionMap=new LinkedHashMap<>(s_orderFunc_Map.get(func_title));
                    recyclerview_order_session_deatils.setHasFixedSize(true);
                    recyclerview_order_session_deatils.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    ViewCartAdapterSession viewCartAdapter = new ViewCartAdapterSession(getContext(), getViewModel,str[0],null,str[1], bottomSheet,orderSessionMap);
                    recyclerview_order_session_deatils.setAdapter(viewCartAdapter);
                } else {
                    MyLog.e(TAG, "myord>> func_session_title null");
                }
            }
        });



        //get func_title  to view item list
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //click on full adapter cardview
                if (s != null && !s.isEmpty()) {
                    func_title=s;
                    func.setText(s);

                    MyLog.e(TAG, "func_title>>string>>" + s);
                    bottomSheet.setContentView(bottom_view);
                    bottomSheet.show();

                    //get session list
                    MyLog.e(TAG,"SessionList>>deatils>>"+s_user_name+"\t\t"+func_title);
                    //sessionLists.clear();
                    //selectedSessionLists=stringListLinkedHashMap.get(s_user_name+"-"+func_title);
                    MyLog.e(TAG,"orders>>change orderFunc_Map session funcv else >>"+new GsonBuilder().setPrettyPrinting().create().toJson(s_orderFunc_Map));

                    orderSessionMap=new LinkedHashMap<>(s_orderFunc_Map.get(func_title));
                    //get session title
                    Set<String> set = orderSessionMap.keySet();
                    List<String> aList1 = new ArrayList<String>(set.size());
                    for (String x1 : set)
                        aList1.add(x1);
                    selectedSessionLists.clear();
                    for(int i=0;i<aList1.size();i++)
                    {
                        String[] arr=(aList1.get(i)).split("_");
                        String bolen=arr[1];
                        String[]str=(arr[0]).split("!");
                        String sess=str[0];
                        String dateTime=str[1];
                        SelectedSessionList sessionList=new SelectedSessionList();
                        sessionList.setSession_title(sess);
                        sessionList.setDate_time(dateTime);
                        sessionList.setBolen(bolen);
                        selectedSessionLists.add(sessionList);
                    }





                    recyclerview_order_session_deatils.setHasFixedSize(true);
                    recyclerview_order_session_deatils.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    ViewCartAdapterSession viewCartAdapter = new ViewCartAdapterSession(getContext(), getViewModel,s,selectedSessionLists, null,bottomSheet,orderSessionMap);
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
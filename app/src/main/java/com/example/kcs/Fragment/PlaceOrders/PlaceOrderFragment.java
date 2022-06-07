package com.example.kcs.Fragment.PlaceOrders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.Toast;

import com.example.kcs.DialogFragment.DoneDialogfragment;
import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Header.SessionDateTime;
import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.Fragment.Items.ItemSelectedList.UserItemList;
import com.example.kcs.Fragment.PlaceOrders.Header.PlaceOrderViewCartAdapterHeader;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.PlaceOrderViewCartAdapterSession;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceOrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerview_session;
    private PlaceOrderViewCartAdapterSession viewCartAdapter;
    /*private RecyclerView recyclerview_order_list;
    private PlaceOrderViewCartAdapterHeader viewCartAdapter;*/
    private AppCompatButton order_btn;

    private List<FunList> funLists = new ArrayList<>();
    private List<UserItemList> userItemLists = new ArrayList<>();
    private List<CheckedList> checkedLists = new ArrayList<>();
    private List<SelectedHeader> selectedHeadersList = new ArrayList<>();
    private GetViewModel getViewModel;
    private String func_title, header_title, user_name, session_title;
    private TextView func_title_view;
    private String TAG = "PlaceOrderFragment";
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ImageView back_btn;
    private DoneDialogfragment doneDialogfragment = new DoneDialogfragment();
    private LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap = new LinkedHashMap<>();
    //hash map to get header session func;
    //header map
    private LinkedHashMap<String, List<CheckedList>> headerMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> sessionMap = new LinkedHashMap<>();
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> funcMap = new LinkedHashMap<>();
    private List<SelectedSessionList> selectedSessionLists = new ArrayList<>();
    //get date and time
    private LinkedHashMap<String, List<SessionDateTime>> date_timeMap = new LinkedHashMap<>();
    //get date and time
    private List<SessionDateTime> sessionDateTimes = new ArrayList<>();
    private String date_time;

    public PlaceOrderFragment() {
        // Required empty public constructor
    }

    public static PlaceOrderFragment newInstance(String param1, String param2) {
        PlaceOrderFragment fragment = new PlaceOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);
        MyLog.e(TAG, "placeorder>> PlaceOrder Fragment");
        recyclerview_session = view.findViewById(R.id.recyclerview_session);
        back_btn = view.findViewById(R.id.back_btn);
        order_btn = view.findViewById(R.id.order_btn);
        func_title_view = view.findViewById(R.id.func_title);


        recyclerview_session.setHasFixedSize(true);
        recyclerview_session.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //get Func_title
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                func_title = s;
                func_title_view.setText(func_title);

            }
        });

        //get selected session
        //get Session Date Time
        getViewModel.getF_mapsdtMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, List<SessionDateTime>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SessionDateTime>> stringListLinkedHashMap) {
                MyLog.e(TAG, "placeorder>>date_time stringListLinkedHashMap>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(stringListLinkedHashMap));
                MyLog.e(TAG, "placeorder>>date_time stringListLinkedHashMap>>" + func_title + "-" + session_title);
                sessionDateTimes = stringListLinkedHashMap.get(func_title + "-" + session_title);
                MyLog.e(TAG, "placeorder>>date_time sessionDateTimes>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(sessionDateTimes));

            }
        });

        // fun hash map of checked list
        getViewModel.getFuncMapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> stringLinkedHashMapLinkedHashMap) {
                funcMap = stringLinkedHashMapLinkedHashMap;
                sessionMap = funcMap.get(func_title);
                date_time=session_title+"-"+(sessionDateTimes.get(0).getDate()+" "+sessionDateTimes.get(0).getTime());
                headerMap = sessionMap.get(date_time);

                //set session list
                Set<String> stringSet = sessionMap.keySet();
                List<String> aList = new ArrayList<String>(stringSet.size());
                for (String x : stringSet)
                    aList.add(x);

                //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                selectedSessionLists.clear();
                for (int i = 0; i < aList.size(); i++) {
                    MyLog.e(TAG, "chs>>list header>> " + aList.get(i));
                    SelectedSessionList list = new SelectedSessionList(
                            aList.get(i)
                    );
                    selectedSessionLists.add(list);
                }
                //set selected session
                getViewModel.setSelectedSessionLists(selectedSessionLists);

                date_time="";
                if (selectedSessionLists != null) {
                    viewCartAdapter = new PlaceOrderViewCartAdapterSession(getContext(), getViewModel, func_title, selectedSessionLists,date_time);
                    recyclerview_session.setAdapter(viewCartAdapter);
                } else {
                    MyLog.e(TAG, "selected session list is empty");
                }




            }
        });


        //get selected header list
        /*getViewModel.getSelectedHeadersListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<SelectedHeader>>() {
            @Override
            public void onChanged(List<SelectedHeader> selectedHeaders) {
                selectedHeadersList=selectedHeaders;
                viewCartAdapter=new PlaceOrderViewCartAdapterHeader(getContext(),getViewModel,selectedHeaders);
                recyclerview_order_list.setAdapter(viewCartAdapter);
            }
        });*/

        //order btn click
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //get headertitle
                getViewModel.getHeader_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        header_title = s;
                    }
                });

                //get username
                user_name = new SharedPreferences_data(getContext()).getS_user_name();

                //get session list
                getViewModel.getSession_titleMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        session_title = s;
                    }
                });


                MyLog.e(TAG, "placeorder>>get funcMap>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(funcMap));
                MyLog.e(TAG, "placeorder>>get funcMap>>" + func_title);
                sessionMap = funcMap.get(func_title);
                MyLog.e(TAG, "placeorder>>get sessionMap>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(sessionMap));
                MyLog.e(TAG, "placeorder>>get sessionMap>>" + session_title);
                headerMap = sessionMap.get(session_title);
                //set selected header
                MyLog.e(TAG, "placeorder>>get headerMap>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(headerMap));
                if(headerMap!=null) {
                    Set<String> stringSet1 = headerMap.keySet();
                    List<String> aList1 = new ArrayList<String>(stringSet1.size());
                    for (String x1 : stringSet1)
                        aList1.add(x1);

                    //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                    selectedHeadersList.clear();
                    for (int i = 0; i < aList1.size(); i++) {
                        MyLog.e(TAG, "chs>>list header>> " + aList1.get(i));
                        SelectedHeader list1 = new SelectedHeader(
                                aList1.get(i)
                        );
                        selectedHeadersList.add(list1);
                    }
                    getViewModel.setSelectedHeadersList(selectedHeadersList);
                    MyLog.e(TAG, "placeorder>>set selectedHeadersList>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(selectedHeadersList));
                }
                else
                {
                    MyLog.e(TAG,"Header map is empty");
                }

                MyLog.e(TAG, "placeorder>>get selectedHeadersList>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(selectedHeadersList));
                doneDialogfragment.show(getParentFragmentManager(), "DoneDialogfragment");


                    date_time="";
                        MyLog.e(TAG, "placeorder>>get date_time>>" + date_time);

                for(int i=0;i<selectedHeadersList.size();i++) {
                    checkedLists = headerMap.get(selectedHeadersList.get(i).getHeader());
                    SaveOrders(func_title, user_name, selectedHeadersList.get(i).getHeader(), session_title, checkedLists,date_time);
                }
                //get func map

                /*getViewModel.getF_mapMutable().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, List<CheckedList>>>() {
                    @Override
                    public void onChanged(LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap1) {
                        for (int i = 0; i < selectedHeadersList.size(); i++) {
                            stringListLinkedHashMap = stringListLinkedHashMap1;

                            checkedLists = stringListLinkedHashMap.get(selectedHeadersList.get(i).getHeader());
                            SaveOrders(func_title, user_name, selectedHeadersList.get(i).getHeader(), session_title, checkedLists);

                        }


                    }
                });*/


            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(0);

            }
        });

        return view;
    }


    private void SaveOrders(String func_title, String user_name, String headerList_title, String session_title, List<CheckedList> checkedLists1, String date_time) {
        String session_str = "";
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (int i = 0; i < checkedLists1.size(); i++) {

                    //set session-dateTime
                    //session_str=session_title+"-"+
                    databaseReference.child(user_name).child(func_title).child(session_title).child(headerList_title).child(String.valueOf(i)).setValue(checkedLists1.get(i).getItemList());
                }
                MyLog.e(TAG, "comit");

                stringListLinkedHashMap.clear();

                //clear all data checked list
                 /*stringListLinkedHashMap.clear();
                getViewModel.setF_map(stringListLinkedHashMap);
                userItemLists.clear();
                getViewModel.setUserItemLists(userItemLists);
                //Toast.makeText(getContext(), "Data Added", Toast.LENGTH_SHORT).show();
                doneDialogfragment.dismiss();*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(getContext(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
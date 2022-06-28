package com.example.kcs.Fragment.PlaceOrders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.Toast;

import com.example.kcs.DialogFragment.DoneDialogfragment;
import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Header.SessionDateTime;
import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.Fragment.Items.ItemSelectedList.UserItemList;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.PlaceOrderViewCartAdapterSession;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.Fragment.Session.SessionList;
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
    private String func_title, header_title, user_name, session_title,date_time,date,s_count;
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
    //edit hash map list
    private List<SessionList> e_sessionLists=new ArrayList<>();
    private List<SelectedHeader> e_selectedHeaders=new ArrayList<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> editFunc_Map = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editDateMap = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> editSessionMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<SelectedHeader>> editHeaderMap = new LinkedHashMap<>();

    private int n=0;


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



        //get date picker
        getViewModel.getDate_pickerMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                date=s;
            }
        });
        //get Func_title
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                func_title = s;
                func_title_view.setText(func_title);

            }
        });
        //get header title
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
        getViewModel.getS_countLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                s_count=s;
            }
        });

        //get edit func map
        getViewModel.getEditFuncMapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>>>() {
                    @Override
                    public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> stringLinkedHashMapLinkedHashMap) {
                        editFunc_Map=stringLinkedHashMapLinkedHashMap;
                        MyLog.e(TAG, "chs>>list edit func map " );
                        editDateMap=editFunc_Map.get(func_title);
                        MyLog.e(TAG,"orders>>date>>"+date);
                        date=date.replace("/","-");


                        if (editDateMap == null) {
                            editDateMap = new LinkedHashMap<>();
                            MyLog.e(TAG, "edit date map is null");

                        }
                        else {
                            editSessionMap = editDateMap.get(date);

                            //set session list
                            Set<String> stringSet = editSessionMap.keySet();
                            List<String> aList = new ArrayList<String>(stringSet.size());
                            for (String x : stringSet)
                                aList.add(x);

                            //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                            selectedSessionLists.clear();
                            for (int i = 0; i < aList.size(); i++) {
                                String[] arr = (aList.get(i)).split("!");

                                //set selected session list and session date and time
                                MyLog.e(TAG, "chs>>list sess>> " + arr[0]);

                                MyLog.e(TAG, "chs>>list time>> " + arr[1]);
                                SelectedSessionList list = new SelectedSessionList();
                                list.setBolen(null);
                                list.setSession_title(arr[0]);
                                list.setS_count(s_count);
                                list.setTime(date + " " + arr[1]);
                                selectedSessionLists.add(list);
                            }

                            //set selected session
                            getViewModel.setSelectedSessionLists(selectedSessionLists);

                            if (editSessionMap == null) {
                                editHeaderMap = new LinkedHashMap<>();
                                // headerMap=sessionMap.get(date_time);
                            } else {

                                viewCartAdapter = new PlaceOrderViewCartAdapterSession(getContext(), getViewModel, func_title, selectedSessionLists, date_time, null, editSessionMap);
                                recyclerview_session.setAdapter(viewCartAdapter);
                            }
                        }
                    }
                });

                // fun hash map of checked list
                getViewModel.getFuncMapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>>() {
                    @Override
                    public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> stringLinkedHashMapLinkedHashMap) {
                        funcMap = stringLinkedHashMapLinkedHashMap;
                        MyLog.e(TAG, "chs>>list func map " );
                        sessionMap = funcMap.get(func_title);
                        //set session list
                        Set<String> stringSet = sessionMap.keySet();
                        List<String> aList = new ArrayList<String>(stringSet.size());
                        for (String x : stringSet)
                            aList.add(x);

                        //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                        selectedSessionLists.clear();
                        for (int i = 0; i < aList.size(); i++) {
                            MyLog.e(TAG, "chs>>list in map>> " + aList.get(i));
                            String[] arr = (aList.get(i)).split("!");

                            //set selected session list and session date and time
                            MyLog.e(TAG, "chs>>list session in map>> " + arr[0]);
                            MyLog.e(TAG, "chs>>list time in map>> " + arr[1]);
                            SelectedSessionList list = new SelectedSessionList();
                            list.setBolen(null);
                            list.setSession_title(arr[0]);
                            list.setTime(arr[1]);
                            selectedSessionLists.add(list);
                        }

                        //set selected session
                        getViewModel.setSelectedSessionLists(selectedSessionLists);

                        if (sessionMap == null) {
                            headerMap = new LinkedHashMap<>();
                            // headerMap=sessionMap.get(date_time);
                        } else {

                            viewCartAdapter = new PlaceOrderViewCartAdapterSession(getContext(), getViewModel, func_title, selectedSessionLists, date_time, sessionMap, null);
                            recyclerview_session.setAdapter(viewCartAdapter);
                        }


                    }
                });


        //order btn click
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.e(TAG, "placeorder>>get funcMap>>" + func_title);
                sessionMap = funcMap.get(func_title);
                MyLog.e(TAG, "placeorder>>get sessionMap>>" + session_title);


                for (int k = 0; k < selectedSessionLists.size(); k++) {
                    date_time=selectedSessionLists.get(k).getSession_title() + "!" + selectedSessionLists.get(k).getTime();
                    headerMap = sessionMap.get(date_time);
                    //set selected header
                    if (headerMap != null) {
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
                    } else {
                        MyLog.e(TAG, "Header map is empty");
                    }




                    MyLog.e(TAG, "placeorder>>get date_time>>" + date_time);

                    for (int i = 0; i < selectedHeadersList.size(); i++) {
                        checkedLists = headerMap.get(selectedHeadersList.get(i).getHeader());
                        SaveOrders(func_title, user_name, selectedHeadersList.get(i).getHeader(), selectedSessionLists.get(k).getSession_title(), checkedLists, date_time);
                    }
                }
                doneDialogfragment.show(getParentFragmentManager(), "DoneDialogfragment");


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
        n++;
        MyLog.e(TAG, "placeorders>>date_time session_str n value>>"+n);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (int i = 0; i < checkedLists1.size(); i++) {
                    //set session-dateTime
                    MyLog.e(TAG, "placeorders>>date_time value>>"+date_time);
                    String[] str=date_time.split("!");
                    String  sess=str[0];
                    String[]dt=(str[1]).split(" ");
                    String date=dt[0];
                    String time=dt[1]+" "+dt[2];

                    MyLog.e(TAG, "placeorders>>date>>"+date);
                    MyLog.e(TAG, "placeorders>>time>>"+time);

                    String s=sess+"!"+time+"_true";
                    MyLog.e(TAG, "placeorders>>ses  time>>"+s);
                    databaseReference.child(user_name).child(func_title).child(date).child(s).child(headerList_title).child(String.valueOf(i)).setValue(checkedLists1.get(i).getItemList());
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
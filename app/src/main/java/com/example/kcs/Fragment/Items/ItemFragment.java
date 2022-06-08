package com.example.kcs.Fragment.Items;

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
import com.example.kcs.Fragment.Func.FunList;

import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String HEADER_LIST = "HEADER_LIST";
    private static final String ITEM_LIST = "ITEM_LIST";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextView header_title;
    private ImageView back_btn;

    private String headerList_title, func_title,session_title;
    private RecyclerView recyclerview_item;


    private List<ItemList> itemLists = new ArrayList<>();
    private List<CheckedList> checkedLists = new ArrayList<>();
    private List<FunList> funLists = new ArrayList<>();
    private ItemListAdapater itemListAdapater;
    //private MyViewModel myViewModel;
    private GetViewModel getViewModel;
    private List<LinkedHashMap<String, List<CheckedList>>> linkedHashMaps=new ArrayList<>();
    //private  LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap=new LinkedHashMap<>();
    private   List<SelectedHeader> selectedHeadersList = new ArrayList<>();
    private   List<SelectedSessionList> selectedSessionLists = new ArrayList<>();
    private  LinkedHashMap<String, List<CheckedList>> headerMap=new LinkedHashMap<>();
    private  LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> sessionMap=new LinkedHashMap<>();
    private  LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> funcMap=new LinkedHashMap<>();

    private String date_time;


    private String TAG = "ItemFragment";



    public ItemFragment() {
        /*this.headerList = headerList;
        this.itemLists = itemLists;*/
        // Required empty public constructor
    }


    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //myViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
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
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        header_title = view.findViewById(R.id.header_title);
        recyclerview_item = view.findViewById(R.id.recyclerview_item);
        back_btn = view.findViewById(R.id.back_btn);

        //get Checked list hash map
        getViewModel.getCheck_s_mapMutable().observe(getViewLifecycleOwner(), new Observer<List<LinkedHashMap<String, List<CheckedList>>>>() {
            @Override
            public void onChanged(List<LinkedHashMap<String, List<CheckedList>>> linkedHashMaps1) {
                linkedHashMaps=linkedHashMaps1;

            }
        });

        //linked hash map of checked list
        /*getViewModel.getF_mapMutable().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, List<CheckedList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap1) {

                stringListLinkedHashMap=stringListLinkedHashMap1;
            }
        });*/
        //func title
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                func_title = s;
                MyLog.e(TAG, "placeorder>>set functitle" + func_title);
            }
        });


        //get session title
        getViewModel.getSession_titleMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                session_title=s;
            }
        });

        //get selected session list
        getViewModel.getSelectedSessionListsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<SelectedSessionList>>() {
            @Override
            public void onChanged(List<SelectedSessionList> selectedSessionLists1) {
                selectedSessionLists = selectedSessionLists1;

            }
        });

        //get fun map
        getViewModel.getFuncMapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> stringLinkedHashMapLinkedHashMap) {
                funcMap = stringLinkedHashMapLinkedHashMap;
                sessionMap = funcMap.get(func_title);
                MyLog.e(TAG, "placeorders>>date_time sessionMap>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(sessionMap));
                MyLog.e(TAG, "placeorders>>get sessionMap>>" + session_title);

                for (int k = 0; k < selectedSessionLists.size(); k++) {
                    date_time = selectedSessionLists.get(k).getSession_title() + "-" + (selectedSessionLists.get(k).getDate_time());
                    MyLog.e(TAG, "placeorders>>get date_time>>" + date_time);
                    if (sessionMap == null) {
                        sessionMap=new LinkedHashMap<>();
                        MyLog.e(TAG, "placeorders>>date_time headerMap null");
                        // headerMap=sessionMap.get(date_time);
                    } else {
                        headerMap = sessionMap.get(date_time);
                        MyLog.e(TAG, "placeorders>>date_time headerMap>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(headerMap));
                    }

                }

            }
        });


        //get view model data
        getViewModel.getHeader_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                headerList_title = s;
                header_title.setText(headerList_title);
            }
        });

        //item lsit
        getViewModel.getItemHeaderMutable().observe(getViewLifecycleOwner(), new Observer<List<ItemList>>() {
            @Override
            public void onChanged(List<ItemList> itemLists1) {
                MyLog.e(TAG, "placeorders>> ItemListAdapater" );
                MyLog.e(TAG, "placeorders>>date_time setAdapter headerMap>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(headerMap));
                itemLists = itemLists1;
                recyclerview_item.setHasFixedSize(true);
                recyclerview_item.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                itemListAdapater = new ItemListAdapater(getContext(), itemLists, headerList_title, getViewModel,linkedHashMaps,headerMap);
                recyclerview_item.setAdapter(itemListAdapater);
                itemListAdapater.notifyDataSetChanged();
            }
        });




        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.e(TAG, "int>>btn clicked");
                //fragment
                getViewModel.getI_fragmentMutable().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        MyLog.e(TAG, "int>>" + integer);
                        if (integer == 1) {
                            getViewModel.setI_value(1);
                        } else {
                            getViewModel.setI_value(0);
                        }

                    }
                });

            }
        });

        return view;
    }
}


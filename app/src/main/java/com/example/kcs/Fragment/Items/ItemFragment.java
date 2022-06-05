package com.example.kcs.Fragment.Items;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Header.HeaderList;

import com.example.kcs.Fragment.Items.ItemSelectedList.UserItemList;
import com.example.kcs.Fragment.Items.ItemSelectedList.UserItemListAdapters;
import com.example.kcs.Fragment.PlaceOrders.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.ViewCartAdapterHeader;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
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

    private String headerList_title, func_title;
    private RecyclerView recyclerview_item;


    private List<ItemList> itemLists = new ArrayList<>();
    private List<CheckedList> checkedLists = new ArrayList<>();
    private List<FunList> funLists = new ArrayList<>();
    private ItemListAdapater itemListAdapater;
    //private MyViewModel myViewModel;
    private GetViewModel getViewModel;
    private List<LinkedHashMap<String, List<CheckedList>>> linkedHashMaps=new ArrayList<>();
    private  LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap=new LinkedHashMap<>();
    private   List<SelectedHeader> selectedHeadersList = new ArrayList<>();

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
        getViewModel.getF_mapMutable().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, List<CheckedList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap1) {

                stringListLinkedHashMap=stringListLinkedHashMap1;
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
                itemLists = itemLists1;
                recyclerview_item.setHasFixedSize(true);
                recyclerview_item.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                itemListAdapater = new ItemListAdapater(getContext(), itemLists, headerList_title, getViewModel,linkedHashMaps,stringListLinkedHashMap);
                recyclerview_item.setAdapter(itemListAdapater);
                itemListAdapater.notifyDataSetChanged();
            }
        });

        //func title
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                func_title = s;
                MyLog.e(TAG, "title>>out" + func_title);
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


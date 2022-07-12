package com.example.kcs.Fragment.Dish;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DishFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextView itemText;
    private String func_title,sessionDateTimeCount,item_title, header_title;

    //Dish Adapter
    private DishAdapter dishAdapter;
    private RecyclerView recyclerview_dish;

    private GetViewModel getViewModel;

    //dish map in list
    private LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> d_ItemMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<DishList>> d_DishMap = new LinkedHashMap<>();
    private String TAG = "DishFragment";
    private List<DishList> dishLists = new ArrayList<>();
    private List<LinkedHashMap<String, List<CheckedList>>> selected_s_map = new ArrayList<>();
    //item map
    private LinkedHashMap<String, List<CheckedList>> itemMap = new LinkedHashMap<>();
    //header map
    private LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> headerMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> sessionMap = new LinkedHashMap<>();
    //fun map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>> funcMap = new LinkedHashMap<>();

    public DishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DishFragment newInstance(String param1, String param2) {
        DishFragment fragment = new DishFragment();
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
        View view = inflater.inflate(R.layout.fragment_dish, container, false);

        itemText = view.findViewById(R.id.item_title);
        recyclerview_dish = view.findViewById(R.id.recyclerview_dish);
        recyclerview_dish.setHasFixedSize(true);
        recyclerview_dish.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //get func title
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                func_title=s;
            }
        });
        //get session title
        getViewModel.getSessionDateTimeCountMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                sessionDateTimeCount=s;
            }
        });

        //get header title
        getViewModel.getHeader_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                header_title = s;
                MyLog.e(TAG, "dish>> header>>"+s);
            }
        });


        //get item map in list database
        getViewModel.getD_ItemMapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, LinkedHashMap<String, List<DishList>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> stringLinkedHashMapLinkedHashMap) {
                d_ItemMap = stringLinkedHashMapLinkedHashMap;
                MyLog.e(TAG, "dish>> map>>"+d_ItemMap.size());

            }
        });
        //get selected dish list
        getViewModel.getCheck_s_mapMutable().observe(getViewLifecycleOwner(), new Observer<List<LinkedHashMap<String, List<CheckedList>>>>() {
            @Override
            public void onChanged(List<LinkedHashMap<String, List<CheckedList>>> linkedHashMaps) {
                selected_s_map=new ArrayList<>(linkedHashMaps);
            }
        });

        //get func map
        getViewModel.getFuncMapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>> stringLinkedHashMapLinkedHashMap) {
                funcMap=stringLinkedHashMapLinkedHashMap;
                MyLog.e(TAG, "dish>>func>>" + func_title);
                MyLog.e(TAG, "dish>>sess>>" + sessionDateTimeCount);
                MyLog.e(TAG, "dish>>header>>" + header_title);
                sessionMap=funcMap.get(func_title);
                headerMap=sessionMap.get(sessionDateTimeCount);



            }
        });

        //get item title
        getViewModel.getItem_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                item_title = s;
                String[] str=s.split("_");
                itemText.setText(str[0]);
                MyLog.e(TAG, "dish>> item>>"+item_title);

                //get dish list
                if (header_title == null) {
                    MyLog.e(TAG, "item title is null");
                } else {
                    d_DishMap = new LinkedHashMap<>(d_ItemMap).get(header_title);
                    if (d_DishMap == null) {
                        d_DishMap = new LinkedHashMap<>();
                        MyLog.e(TAG, "dish map is null");
                    } else {
                        if (item_title == null) {
                            MyLog.e(TAG, "item title is null");
                        } else {
                            MyLog.e(TAG, "dish>>d_DishMap header>>" + header_title);
                            MyLog.e(TAG, "dish>>d_DishMap item_title>>" + item_title);
                            dishLists = d_DishMap.get(item_title);
                            itemMap=headerMap.get(header_title);
                            dishAdapter = new DishAdapter(getContext(), dishLists, getViewModel,selected_s_map,itemMap);
                            recyclerview_dish.setAdapter(dishAdapter);
                        }
                    }

                }


            }
        });
        return view;
    }
}
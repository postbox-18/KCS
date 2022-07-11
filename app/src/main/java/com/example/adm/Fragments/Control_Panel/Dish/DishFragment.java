package com.example.adm.Fragments.Control_Panel.Dish;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
    private LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> itemArrayMap = new LinkedHashMap<>();
    //dish map
    private LinkedHashMap<String, List<DishList>> dishListMap = new LinkedHashMap<>();
    private List<DishList> dishLists = new ArrayList<>();

    private String header_title,item;
    private TextView itemText;
    private GetViewModel getViewModel;
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
        View view= inflater.inflate(R.layout.fragment_dish, container, false);
        RecyclerView recyclerview_dish_list=view.findViewById(R.id.recyclerview_dish_list);
        itemText=view.findViewById(R.id.item_title_set);

        //get item title
        getViewModel.getItem_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                item=s;
                String []str=s.split("_");
                itemText.setText(str[0]);
            }
        });

        //get dish list
        getViewModel.getDishListsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<DishList>>() {
            @Override
            public void onChanged(List<DishList> dishLists1) {
                dishLists=dishLists1;
            }
        });
        //get header title
        getViewModel.getHeader_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                header_title=s;
            }
        });
        DishAdapter dishAdapter = new DishAdapter(getContext(), dishLists, getViewModel, header_title, item);
        recyclerview_dish_list.setAdapter(dishAdapter);

        return view;
    }
}
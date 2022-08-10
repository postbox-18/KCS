package com.example.adm.Fragments.Control_Panel.HeaderFrags.Dish;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm.Classes.MyLog;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

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
    private FloatingActionButton add;

    private String TAG="DishFragment";

    //alert
    private String header_titles,s_dish,selected;
    private EditText dishEditText;
    private TextView head;
    private TextInputLayout dish_inputLayout;
    private GetViewModel  getViewModel1;
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
        add = view.findViewById(R.id.add);

        recyclerview_dish_list.setHasFixedSize(true);
        recyclerview_dish_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

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
                MyLog.e(TAG,"panel>>header_title>>"+header_title);
            }
        });


        //get item title
        getViewModel.getItem_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                item=s;
                MyLog.e(TAG,"panel>>item title>>"+item);
                String []str=s.split("_");
                itemText.setText(str[0]);
                DishAdapter dishAdapter = new DishAdapter(getContext(), dishLists, getViewModel, header_title, item);
                recyclerview_dish_list.setAdapter(dishAdapter);
            }
        });
        //on click
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addItemBox.show(getParentFragmentManager(),"AddItemBox");
                alertBox(view,item);

            }
        });


        return view;
    }
    private void alertBox(View view, String item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.add_itembox, null);
        getViewModel1 = new ViewModelProvider(getActivity()).get(GetViewModel.class);
        dishEditText = customLayout.findViewById(R.id.item);
        head = customLayout.findViewById(R.id.head);
        dishEditText.setHint("Dish");
        head.setText("Dish To Add..");
        //dish_inputLayout=customLayout.findViewById(R.id.itemTextInputLayout);
        //ok=customLayout.findViewById(R.id.ok);
        //get header title;
        getViewModel1.getHeader_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                header_titles = s;
            }
        });
   

        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                s_dish = dishEditText.getText().toString();
                selected = "true";
                getViewModel1.updateItem(header_titles,item,s_dish,selected,null);
                Toast.makeText(getContext(), "Dish Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        AlertDialog dialog
                = builder.create();
        dialog.show();
    }
}
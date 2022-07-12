package com.example.adm.Fragments.Control_Panel.Item;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Control_Panel.Dish.DishList;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2, TAG = "ItemFragment";
    private RecyclerView recyclerView_item;
    private ItemAdapter itemAdapter;
    private List<ItemArrayList> itemList = new ArrayList<>();
    private TextView header_title;
    private FloatingActionButton add;
    private GetViewModel getViewModel;
    //dish map
    private LinkedHashMap<String, List<DishList>> dishListMap = new LinkedHashMap<>();

    //alert
    private String header_titles,s_item,s_dish,selected;
    private EditText item,dish;
    private TextView head;
    private LinearLayout layout;
    private TextInputLayout item_inputLayout;
    private GetViewModel  getViewModel1;

    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getViewModel = new ViewModelProvider(getActivity()).get(GetViewModel.class);
        add = view.findViewById(R.id.add);
        recyclerView_item = view.findViewById(R.id.recyclerview_item_list);
        header_title = view.findViewById(R.id.header_title_set);

        recyclerView_item.setHasFixedSize(true);
        recyclerView_item.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //get item list
        getViewModel.getItemListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<ItemArrayList>>() {
            @Override
            public void onChanged(List<ItemArrayList> itemArrayLists) {
                MyLog.e(TAG,"panel>>itemList>>"+itemList);
                itemList = itemArrayLists;
            }
        });

        //get dish map
        getViewModel.getDishListMapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, List<DishList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<DishList>> stringListLinkedHashMap) {
                dishListMap = stringListLinkedHashMap;

            }
        });

        //get header title
        getViewModel.getHeader_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                header_title.setText(s);

                MyLog.e(TAG,"panel>>header_title>>"+header_title);
                ItemAdapter itemAdapter = new ItemAdapter(getContext(), getViewModel, itemList, dishListMap,s);
                recyclerView_item.setAdapter(itemAdapter);
            }
        });



        //on click
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addItemBox.show(getParentFragmentManager(),"AddItemBox");
                alertBox(view);

            }
        });


        return view;
    }

    private void alertBox(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.add_itembox, null);
        getViewModel1 = new ViewModelProvider(getActivity()).get(GetViewModel.class);
        item = customLayout.findViewById(R.id.item);
        head = customLayout.findViewById(R.id.head);
        layout = customLayout.findViewById(R.id.layout);
        dish = customLayout.findViewById(R.id.dish);

        layout.setVisibility(View.VISIBLE);

        item.setHint("Item");
        dish.setHint("Dish");

        head.setText("Item To Add..");
        //item_inputLayout=customLayout.findViewById(R.id.itemTextInputLayout);
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
                s_item = item.getText().toString();
                s_dish = dish.getText().toString();
                if(s_item.isEmpty())
                {
                    item.setError("Please enter the item");
                }
                else if(s_dish.isEmpty())
                {
                    dish.setError("Please enter the dish");
                }
                else {
                    selected = "true";
                    String items=s_item+"_true";
                    //getViewModel1.updateItem(header_titles,item,s_dish,selected,null);
                    getViewModel1.updateItem(header_titles, items, s_dish, selected, null);
                    Toast.makeText(getContext(), "Item Added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

}
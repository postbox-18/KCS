package com.example.adm.Fragments.Control_Panel.Item;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Control_Panel.Selected_UnSelected_List.ItemArrayList;
import com.example.adm.Login_Register.LoginActivity;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2, TAG = "ItemFragment";
    private RecyclerView recyclerView_item;
    private ItemAdapter itemAdapter;
    private List<ItemArrayList> itemList = new ArrayList<>();
    private TextView header_title;
    private FloatingActionButton add;
    private GetViewModel getViewModel;

    //alert
    private String header_titles,s_item,selected;
    private EditText item;
    private TextInputLayout item_inputLayout;
    //private AppCompatButton ok;
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
        recyclerView_item = view.findViewById(R.id.recyclerview_item_list);
        header_title = view.findViewById(R.id.header_title);
        add = view.findViewById(R.id.add);

        recyclerView_item.setHasFixedSize(true);
        recyclerView_item.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //get header title
        getViewModel.getHeader_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                header_title.setText(s);
            }
        });
        //get item list
        getViewModel.getItemListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<ItemArrayList>>() {
            @Override
            public void onChanged(List<ItemArrayList> itemArrayLists) {
                itemAdapter = new ItemAdapter(getContext(), itemArrayLists, getViewModel);
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
        item=customLayout.findViewById(R.id.item);
        //item_inputLayout=customLayout.findViewById(R.id.itemTextInputLayout);
        //ok=customLayout.findViewById(R.id.ok);
        //get header title;
        getViewModel1.getHeader_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                header_titles=s;
            }
        });
        /*ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_item=item.getText().toString();
                selected="true";
                getViewModel1.updateItem(header_titles,s_item,selected);
                getViewModel1.setI_value(1);
                Toast.makeText(getContext(), "Item Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });*/

        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                s_item=item.getText().toString();
                selected="true";
                getViewModel1.updateItem(header_titles,s_item,selected);
                getViewModel1.setI_value(1);
                Toast.makeText(getContext(), "Item Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

}
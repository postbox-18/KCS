package com.example.adm.Fragments.Control_Panel;

import android.os.Bundle;

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

import com.example.adm.Fragments.Control_Panel.Func.FuncAdapter;
import com.example.adm.Fragments.Control_Panel.Selected_UnSelected_List.FuncList;
import com.example.adm.Fragments.Control_Panel.Header.HeaderAdapter;
import com.example.adm.Fragments.Control_Panel.Selected_UnSelected_List.HeaderList;
import com.example.adm.Fragments.Control_Panel.Item.ItemAdapter;
import com.example.adm.Fragments.Control_Panel.Selected_UnSelected_List.ItemArrayList;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Control_PanelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Control_PanelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerview_header;
    private List<HeaderList> headerList=new ArrayList<>();
    private HeaderAdapter headerAdapter;

    /*private RecyclerView recyclerView_func;
    private List<FuncList> funcList=new ArrayList<>();
    private FuncAdapter funcAdapter;

    private RecyclerView recyclerView_item;
    private ItemAdapter itemAdapter;
    private List<ItemArrayList> itemList=new ArrayList<>();*/

    private ImageView back_btn;
    private GetViewModel getViewModel;

    //Update Data in firebase
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private AppCompatButton update_btn;

    private String TAG="Control_PanelFragment";
    public Control_PanelFragment() {
        // Required empty public constructor
    }
    public static Control_PanelFragment newInstance(String param1, String param2) {
        Control_PanelFragment fragment = new Control_PanelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view= inflater.inflate(R.layout.fragment_control__panel, container, false);
        getViewModel = new ViewModelProvider(getActivity()).get(GetViewModel.class);

        back_btn=view.findViewById(R.id.back_btn);
        update_btn=view.findViewById(R.id.update_btn);
        recyclerview_header=view.findViewById(R.id.recyclerview_header);


        recyclerview_header.setHasFixedSize(true);
        recyclerview_header.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        headerList=new ArrayList<>();
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();


        //get header list
        getViewModel.getHeaderListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<HeaderList>>() {
            @Override
            public void onChanged(List<HeaderList> headerLists) {
                headerAdapter=new HeaderAdapter(getContext(),headerLists,getViewModel);
                recyclerview_header.setAdapter(headerAdapter);
            }
        });

       /* //get item list
        getViewModel.getItemListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<ItemArrayList>>() {
            @Override
            public void onChanged(List<ItemArrayList> itemArrayLists) {

                itemAdapter=new ItemAdapter(getContext(),itemArrayLists,getViewModel);
                recyclerView_item.setAdapter(itemAdapter);
            }
        });

        //get func list
        getViewModel.getFuncListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<FuncList>>() {
            @Override
            public void onChanged(List<FuncList> funcLists) {
                funcAdapter=new FuncAdapter(getContext(),funcLists,getViewModel);
                recyclerView_func.setAdapter(funcAdapter);
            }
        });*/


        //click update btn
        /*update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.UpdateListBase();
            }
        });*/


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getViewModel.setI_value(0);
            }
        });



        return view;
    }

}
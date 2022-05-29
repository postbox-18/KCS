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

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Items.CheckedList;
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

    private RecyclerView recyclerview_order_list;
    private ViewCartAdapterHeader viewCartAdapter;
    private AppCompatButton order_btn;

    private List<FunList> funLists=new ArrayList<>();
    private List<CheckedList> checkedLists=new ArrayList<>();
    private   List<SelectedHeader> selectedHeadersList = new ArrayList<>();
    private GetViewModel getViewModel;
    private String func_title,header_title,user_name;
    private TextView func_title_view;
    private String TAG="PlaceOrderFragment";
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ImageView back_btn;


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
        View view= inflater.inflate(R.layout.fragment_place_order, container, false);

        recyclerview_order_list=view.findViewById(R.id.recyclerview_order_list);
        back_btn=view.findViewById(R.id.back_btn);
        order_btn=view.findViewById(R.id.order_btn);
        func_title_view=view.findViewById(R.id.func_title);


        recyclerview_order_list.setHasFixedSize(true);
        recyclerview_order_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //get Func_title
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                func_title=s;
                func_title_view.setText(func_title);
            }
        });


        //get selected header list
        getViewModel.getSelectedHeadersListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<SelectedHeader>>() {
            @Override
            public void onChanged(List<SelectedHeader> selectedHeaders) {
                selectedHeadersList=selectedHeaders;
                viewCartAdapter=new ViewCartAdapterHeader(getContext(),getViewModel,selectedHeaders);
                recyclerview_order_list.setAdapter(viewCartAdapter);
            }
        });

        //order btn click
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               
            //get headertitle
                getViewModel.getHeader_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        header_title=s;
                    }
                });
                
                //get username
                user_name=new SharedPreferences_data(getContext()).getS_user_name();

                //get linked hash map checked list
                getViewModel.getF_mapMutable().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, List<CheckedList>>>() {
                    @Override
                    public void onChanged(LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap) {
                        for(int i=0;i<selectedHeadersList.size();i++)
                        {
                            checkedLists=stringListLinkedHashMap.get(selectedHeadersList.get(i).getHeader());
                            SaveOrders(func_title,user_name,selectedHeadersList.get(i).getHeader(),checkedLists);

                        }
                    }
                });
              
                
                //get selected checked list
                

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


    private void SaveOrders(String func_title, String user_name, String headerList_title, List<CheckedList> checkedLists1) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (int i = 0; i < checkedLists1.size(); i++) {
                    //getFunc
                    databaseReference.child(user_name).child(func_title).child(headerList_title).child(String.valueOf(i)).setValue(checkedLists1.get(i).getItemList());
                }


                Toast.makeText(getContext(), "data added", Toast.LENGTH_SHORT).show();
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
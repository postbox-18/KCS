package com.example.adm.Fragments.Control_Panel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Control_Panel.Func.FuncAdapter;
import com.example.adm.Fragments.Control_Panel.Func.FuncList;
import com.example.adm.Fragments.Control_Panel.Header.HeaderAdapter;
import com.example.adm.Fragments.Control_Panel.Header.HeaderList;
import com.example.adm.Fragments.Control_Panel.Item.ItemAdapter;
import com.example.adm.Fragments.Control_Panel.Item.ItemArrayList;
import com.example.adm.Fragments.HomeFragment;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.GsonBuilder;

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
    private RecyclerView recyclerView_header;
    private List<HeaderList> headerList=new ArrayList<>();
    private HeaderAdapter headerAdapter;
    private RecyclerView recyclerView_func;
    private List<FuncList> funcList=new ArrayList<>();
    private FuncAdapter funcAdapter;
    private RecyclerView recyclerView_item;
    private ItemAdapter itemAdapter;
    private List<ItemArrayList> itemList=new ArrayList<>();
    private ImageView back_btn;
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private GetViewModel getViewModel;
    private String TAG="Control_PanelFragment";
    public Control_PanelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Control_PanelFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        recyclerView_header=view.findViewById(R.id.recyclerview_header);
        recyclerView_header.setHasFixedSize(true);
        recyclerView_header.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView_func=view.findViewById(R.id.recyclerview_func);
        recyclerView_func.setHasFixedSize(true);
        recyclerView_func.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView_item=view.findViewById(R.id.recyclerview_item);
        recyclerView_item.setHasFixedSize(true);
        recyclerView_item.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        headerList=new ArrayList<>();
        funcList=new ArrayList<>();
        itemList=new ArrayList<>();
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        GetHeader();
        GetFun();
        GetItem();


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getViewModel.setI_value(0);
            }
        });
        return view;
    }
    private void GetItem() {
        databaseReference = firebaseDatabase.getReference("Items").child("List");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "list>>snap>>" + snapshot);
                int size=0;


               for(DataSnapshot dataSnapshot: snapshot.getChildren())
               {
                   itemList=new ArrayList<>();
                   ItemArrayList itemList1=new ItemArrayList(
                           dataSnapshot.getValue().toString()
                   );
                   itemList.add(itemList1);
               }
               itemAdapter=new ItemAdapter(getContext(),itemList);
               recyclerView_item.setAdapter(itemAdapter);
                //MyLog.e(TAG, "data>>item>>" + new GsonBuilder().setPrettyPrinting().create().toJson(itemList));
                size++;



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "list>>snap>>fun>>Fail to get data.");
            }
        });
    }

    private void GetFun() {
        databaseReference = firebaseDatabase.getReference("Items").child("Function");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "home>>snap>>fun>>" + snapshot);
                int size=0;
                funcList=new ArrayList<>();

                for (DataSnapshot datas : snapshot.getChildren()) {
                    String path=""+size;
                    MyLog.e(TAG, "home>>snap>>fun>>" +  path);
                    //MyLog.e(TAG, "home>>snap>>fun>>" +  datas.child("0").getValue().toString());
                    FuncList funList1 = new FuncList(
                            datas.getValue().toString());
                    funcList.add(funList1);
                    size++;

                }
                funcAdapter=new FuncAdapter(getContext(),funcList);
                recyclerView_func.setAdapter(funcAdapter);
                //MyLog.e(TAG, "data>>func>>" + new GsonBuilder().setPrettyPrinting().create().toJson(funcList));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "home>>snap>>fun>>Fail to get data.");
            }
        });
    }
    private void GetHeader() {
        databaseReference = firebaseDatabase.getReference("Items").child("Category");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "home>>snap>>" + snapshot);
                int size=0;
                headerList=new ArrayList<>();
                for (DataSnapshot datas : snapshot.getChildren()) {

                    String path=""+size;

                    //MyLog.e(TAG, "home>>snap>>" +  datas.child("a").getValue().toString());

                    HeaderList headerList1 = new HeaderList(
                            datas.getValue().toString()
                    );
                    headerList.add(headerList1);
                    size++;

                }

                headerAdapter=new HeaderAdapter(getContext(),headerList);
                recyclerView_header.setAdapter(headerAdapter);
               // MyLog.e(TAG, "data>>header>>" + new GsonBuilder().setPrettyPrinting().create().toJson(headerList));

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "home>>snap>>Fail to get data.");
            }
        });
    }
}
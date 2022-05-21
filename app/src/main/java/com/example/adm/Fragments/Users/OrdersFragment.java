package com.example.adm.Fragments.Users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SharedPreferences_data;
import com.example.adm.Fragments.Orders.OrderAdapters;
import com.example.adm.Fragments.Orders.OrderLists;
import com.example.adm.R;
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
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String TAG = "OrdersFragment";

    private List<OrderLists> orderLists;
    private OrderAdapters orderAdapters;
    private RecyclerView recyclerView_order_list;
    private String header,func,s_user_name;
    private String item="";

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        recyclerView_order_list = view.findViewById(R.id.recyclerview_order_list);
        recyclerView_order_list.setHasFixedSize(true);
        recyclerView_order_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        orderLists = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "snap>>" + snapshot);
                int size=0;
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "snap>>snap childern>>" + snapshot.getValue().toString());
                    MyLog.e(TAG, "snap>>snap childern>>" + snapshot.getKey());
                    MyLog.e(TAG, "snap>>datas>>" + datas);
                    s_user_name=datas.getKey().toString();
                    for (DataSnapshot dataSnapshot : datas.getChildren()) {
                        func=dataSnapshot.getKey().toString();
                        MyLog.e(TAG, "snap>>datasnap>>" + dataSnapshot);
                        for (DataSnapshot shot : dataSnapshot.getChildren()) {
                            header=shot.getKey().toString();
                            MyLog.e(TAG, "snap>>shots>>" + shot);
                            for (DataSnapshot data : shot.getChildren()) {
                                MyLog.e(TAG, "snap>>data>>" + data);
                                item+=data.getValue().toString()+" ";
                                size++;
                            }
                            OrderLists orderLists1 = new OrderLists(
                                    s_user_name,
                                    func,
                                    header,
                                    item,
                                    size
                            );
                            size=0;
                            orderLists.add(orderLists1);
                        }
                    }

                }
                MyLog.e(TAG,"deta>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(orderLists));
                orderAdapters=new OrderAdapters(getContext(),orderLists);
                recyclerView_order_list.setAdapter(orderAdapters);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
package com.example.adm.Fragments.Orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SessionList;
import com.example.adm.Fragments.Orders.BottomSheet.OrderLists;
import com.example.adm.Fragments.Orders.BottomSheet.SelectedHeader;
import com.example.adm.Fragments.Orders.BottomSheet.ViewCartAdapterHeader;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    private String TAG = "OrdersFragment";

    private OrderAdapters orderAdapters;
    private RecyclerView recyclerView_order_list;

    //bottom sheet view
    private RecyclerView recyclerview_session_view;
    private List<SessionList> sessionLists=new ArrayList<>();
    private LinkedHashMap<String, List<SessionList>> stringListLinkedHashMap=new LinkedHashMap<>();

    private GetViewModel getViewModel;

    public OrdersFragment() {
        // Required empty public constructor
    }


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
        getViewModel=new ViewModelProvider(getActivity()).get(GetViewModel.class);
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

        getViewModel.getOrderListsMutable().observe(getViewLifecycleOwner(), new Observer<List<OrderLists>>() {
            @Override
            public void onChanged(List<OrderLists> orderLists) {
                recyclerView_order_list.setHasFixedSize(true);
                recyclerView_order_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                orderAdapters=new OrderAdapters(getContext(),orderLists,getViewModel);
                recyclerView_order_list.setAdapter(orderAdapters);
            }
        });

        //Bottom sheet
        BottomSheetDialog bottomSheet = new BottomSheetDialog(requireContext());
        View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_order_details, null);
        recyclerview_session_view = bottom_view.findViewById(R.id.recyclerview_session_view);
        recyclerview_session_view.setHasFixedSize(true);
        recyclerview_session_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        ///get session hash map  List
        //get session list
        getViewModel.getSs_f_mapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, List<SessionList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SessionList>> stringListLinkedHashMap1) {
                stringListLinkedHashMap=stringListLinkedHashMap1;


            }
        });

        //get orderItem list to view item list
        getViewModel.getOrderListsViewMutableLiveData().observe(getViewLifecycleOwner(), new Observer<OrderLists>() {
            @Override
            public void onChanged(OrderLists orderLists) {
                if(orderLists!=null) {
                    //get session list
                    MyLog.e(TAG,"SessionList>>deatils>>"+orderLists.getS_user_name()+"\t\t"+orderLists.getFunc());
                    sessionLists=stringListLinkedHashMap.get(orderLists.getS_user_name()+"-"+orderLists.getFunc());

                    ViewCartAdapterSession viewCartAdapter=new ViewCartAdapterSession(getContext(),getViewModel,sessionLists,orderLists);
                    recyclerview_session_view.setAdapter(viewCartAdapter);
                    bottomSheet.setContentView(bottom_view);
                    bottomSheet.show();
                }
                else
                {
                    MyLog.e(TAG,"itemAd>> orderItemView list null");
                }
            }
        });




        return view;
    }
}
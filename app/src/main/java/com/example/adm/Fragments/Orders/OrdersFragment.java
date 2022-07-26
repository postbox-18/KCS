package com.example.adm.Fragments.Orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderDishLists;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderLists;
import com.example.adm.Fragments.Orders.BottomSheet.ViewCartAdapterDate;
import com.example.adm.Fragments.Orders.Classes.SelectedDateList;
import com.example.adm.Fragments.Orders.Classes.Username;
import com.example.adm.Fragments.Orders.OrdersAdapters.OrderAdapters;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

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
    private RecyclerView recyclerview_date_view;
    private TextView func_title,user_name;

    private GetViewModel getViewModel;
    //order hash map
    //order map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>> orderMap = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>> b_orderMap = new LinkedHashMap<>();
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>> orderFunc_Map = new LinkedHashMap<>();
    //Date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>> orderDateMap = new LinkedHashMap<>();
    //Session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>> orderSessionMap = new LinkedHashMap<>();
    //Header map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>> orderHeaderMap = new LinkedHashMap<>();
    //Item map
    private LinkedHashMap<String, List<OrderDishLists>> orderItemMap = new LinkedHashMap<>();
    //order list
    private List<OrderLists> o_orderLists=new ArrayList<>();
    //user name list
    private List<Username> o_usernames=new ArrayList<>();
    //date list
    private List<SelectedDateList> o_dateLists=new ArrayList<>();
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
        recyclerView_order_list.setHasFixedSize(true);
        recyclerView_order_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //get order map
        getViewModel.getOrderMapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>> stringLinkedHashMapLinkedHashMap) {
                orderMap=new LinkedHashMap<>(stringLinkedHashMapLinkedHashMap);
                b_orderMap=new LinkedHashMap<>(stringLinkedHashMapLinkedHashMap);

                //get user name to set list
                Set<String> stringSet = (orderMap).keySet();
                List<String> aList = new ArrayList<String>(stringSet.size());
                for (String x : stringSet)
                    aList.add(x);
                o_usernames.clear();
                for(int i=0;i<aList.size();i++)
                {
                    String[]str=(aList.get(i)).split("-");
                    Username username=new Username(
                            str[0],
                            str[1]
                    );
                    o_usernames.add(username);
                }
                //set order item list
                o_orderLists=new ArrayList<>();
                for(int l=0;l<o_usernames.size();l++)
                {
                    String username=o_usernames.get(l).getUsername();
                    String phone_number=o_usernames.get(l).getPhone_number();
                    String s=username+"-"+phone_number;
                    orderFunc_Map=new LinkedHashMap<>(orderMap).get(s);
                    //get func
                    Set<String> stringSet1 = orderFunc_Map.keySet();
                    List<String> aList1 = new ArrayList<String>(stringSet1.size());
                    for (String x1 : stringSet1)
                        aList1.add(x1);

                    for(int k=0;k<aList1.size();k++)
                    {
                        OrderLists orderLists=new OrderLists(
                                username,
                                aList1.get(k), phone_number);
                        o_orderLists.add(orderLists);
                        orderAdapters=new OrderAdapters(getContext(),o_orderLists,getViewModel);
                        recyclerView_order_list.setAdapter(orderAdapters);
                    }

                }



            }
        });
          //Bottom sheet
        BottomSheetDialog bottomSheet = new BottomSheetDialog(requireContext());
        View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_order_details, null);
        func_title = bottom_view.findViewById(R.id.func_title);
        user_name = bottom_view.findViewById(R.id.user_name);
        recyclerview_date_view = bottom_view.findViewById(R.id.recyclerview_date_view);
        recyclerview_date_view.setHasFixedSize(true);
        recyclerview_date_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        //get date map string click on date map
        getViewModel.getDateStringLive().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                MyLog.e(TAG, "bottom>>dateString>" +s);
                String[] str=s.split("/");
                String name=str[0];
                String func=str[1];
                String date=str[2];

                orderFunc_Map=new LinkedHashMap<>(b_orderMap).get(name);
                func_title.setText(func);
                user_name.setText(name);

                orderDateMap=new LinkedHashMap<>(orderFunc_Map).get(func);

                o_dateLists=new ArrayList<>();
                SelectedDateList list=new SelectedDateList(
                        date
                );
                o_dateLists.add(list);

                ViewCartAdapterDate viewCartAdapterDate=new ViewCartAdapterDate(getContext(),getViewModel,orderDateMap,o_dateLists,name,func);
                recyclerview_date_view.setAdapter(viewCartAdapterDate);
                bottomSheet.setContentView(bottom_view);
                bottomSheet.show();
            }
        });

        //get orderItem list to view item list
        getViewModel.getOrderListsViewMutableLiveData().observe(getViewLifecycleOwner(), new Observer<OrderLists>() {
            @Override
            public void onChanged(OrderLists orderLists) {
                if(orderLists!=null) {
                    //get session list
                    MyLog.e(TAG,"SessionList>>deatils>>"+orderLists.getS_user_name()+"\t\t"+orderLists.getFunc());
                    orderFunc_Map=b_orderMap.get(orderLists.getS_user_name()+"-"+orderLists.getPhone_number());
                    func_title.setText(orderLists.getFunc());
                    user_name.setText(orderLists.getS_user_name());
                    orderDateMap=new LinkedHashMap<>(orderFunc_Map).get(orderLists.getFunc());

                    //get date list
                    Set<String> stringSet = orderDateMap.keySet();
                    List<String> aList = new ArrayList<String>(stringSet.size());
                    for (String x : stringSet)
                        aList.add(x);
                    o_dateLists=new ArrayList<>();
                    for(int i=0;i<aList.size();i++)
                    {
                        SelectedDateList list=new SelectedDateList(
                                aList.get(i)
                        );
                        o_dateLists.add(list);
                    }


                    ViewCartAdapterDate viewCartAdapterDate=new ViewCartAdapterDate(getContext(),getViewModel,orderDateMap,o_dateLists, orderLists.getS_user_name(), orderLists.getFunc());
                    recyclerview_date_view.setAdapter(viewCartAdapterDate);
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
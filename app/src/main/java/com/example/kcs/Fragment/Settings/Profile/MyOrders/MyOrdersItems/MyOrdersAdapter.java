package com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrdersItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.OrderItemLists;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrderFuncList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {
    private Context context;
    private String TAG="MyOrdersAdapter";
    private List<MyOrderFuncList> myOrderFuncLists=new ArrayList<>();
    private List<SelectedSessionList> sessionLists=new ArrayList<>();
    private List<MyOrdersList> myOrdersList;
    private GetViewModel getViewModel;
    //edit hash map list
    private List<SelectedSessionList> e_sessionLists=new ArrayList<>();
    private List<SelectedHeader> e_selectedHeaders=new ArrayList<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editFunc_Map = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> editSessionMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<SelectedHeader>> editHeaderMap = new LinkedHashMap<>();
    //order hashmap
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>>> orderFunc_Map = new LinkedHashMap<>();
    //date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> orderDateMap = new LinkedHashMap<>();
    //header map
    private LinkedHashMap<String, List<OrderItemLists>> orderHeaderMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap = new LinkedHashMap<>();
    //date list
    private List<SelectedDateList> dateLists=new ArrayList<>();

    public MyOrdersAdapter(Context context, List<MyOrderFuncList> myOrderFuncLists, GetViewModel getViewModel, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>>> orderFunc_Map) {
        this.myOrderFuncLists = myOrderFuncLists;
        this.context = context;
        this.getViewModel = getViewModel;
        this.orderFunc_Map = orderFunc_Map;
    }

    @NonNull
    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.myorder_cardview, parent, false);
        return new MyOrdersAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersAdapter.ViewHolder holder, int position) {
        final MyOrderFuncList myOrderFuncLists1 = myOrderFuncLists.get(position);
        String username=new SharedPreferences_data(context).getS_user_name();
        //get data func,header,list item size from hash map
        holder.func.setText(myOrderFuncLists1.getFunc());
        ///////////***************************clear list in live data model****************************//////////////////////

        //get func map
        getViewModel.getEditFuncMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> stringLinkedHashMapLinkedHashMap) {
                editFunc_Map=stringLinkedHashMapLinkedHashMap;

            }
        });


        //get session map
        getViewModel.getEditSessionMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> stringLinkedHashMapLinkedHashMap) {
                editSessionMap=stringLinkedHashMapLinkedHashMap;
            }
        });


        //get header map
        getViewModel.getEditHeaderMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SelectedHeader>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SelectedHeader>> stringListLinkedHashMap) {
                editHeaderMap=stringListLinkedHashMap;
            }
        });

        ///////////***************************clear list in live data model****************************//////////////////////

        //get selected date
        orderDateMap=new LinkedHashMap<>(orderFunc_Map.get(myOrderFuncLists1.getFunc()));
        //get date list
        Set<String> set = orderDateMap.keySet();
        List<String> aList1 = new ArrayList<String>(set.size());
        for (String x1 : set)
            aList1.add(x1);
        dateLists.clear();
        for(int i=0;i<aList1.size();i++)
        {
            SelectedDateList sessionList=new SelectedDateList(
                    aList1.get(i)
            );

            dateLists.add(sessionList);

        }

        holder.recyclerview_date.setHasFixedSize(true);
        holder.recyclerview_date.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        MyOrdersAdapterDate itemListAdapters = new MyOrdersAdapterDate(context, getViewModel,orderDateMap,dateLists);
        holder.recyclerview_date.setAdapter(itemListAdapters);



        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFunc_Map=new LinkedHashMap<>();
                getViewModel.setEditFuncMap(editFunc_Map);
                editSessionMap=new LinkedHashMap<>();
                getViewModel.setEditSessionMap(editSessionMap);
                editHeaderMap=new LinkedHashMap<>();
                getViewModel.setEditHeaderMap(editHeaderMap);
                getViewModel.setFunc_title(myOrderFuncLists1.getFunc());
                //getViewModel.SetBreadCrumsList(myOrderFuncLists1.getFunc(), 0);
            }
        });

    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "myOrderFuncLists>>49>>" + myOrderFuncLists.size());
        return myOrderFuncLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView   func;
        private CardView card_view;
        private RecyclerView recyclerview_date;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            recyclerview_date = view.findViewById(R.id.recyclerview_date);
            func = view.findViewById(R.id.func);
            card_view = view.findViewById(R.id.card_view);


        }
    }
}


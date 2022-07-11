package com.example.adm.Fragments.Orders.BottomSheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderDishLists;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderHeaderLists;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderItemList;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ViewCartAdapterHeader extends RecyclerView.Adapter<ViewCartAdapterHeader.ViewHolder> {
    private Context context;
    private String TAG = "ViewCartAdapterHeader";
    private GetViewModel getViewModel;
    private List<OrderHeaderLists> o_Order_HeaderLists = new ArrayList<>();
    //order hash map
    //Header map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>> orderHeaderMap = new LinkedHashMap<>();
    //Item map
    private LinkedHashMap<String, List<OrderDishLists>> orderItemMap = new LinkedHashMap<>();
    //user item list
    private List<OrderDishLists> o_orderDishLists = new ArrayList<>();
    private List<OrderItemList> orderItemLists = new ArrayList<>();
    private String name, func, date, session;


    public ViewCartAdapterHeader(Context context, GetViewModel getViewModel, List<OrderHeaderLists> o_Order_HeaderLists, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>> orderHeaderMap, String name, String func, String date, String s) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.name = name;
        this.func = func;
        this.date = date;
        this.session = s;
        this.o_Order_HeaderLists = new ArrayList<>(o_Order_HeaderLists);
        this.orderHeaderMap = new LinkedHashMap<>(orderHeaderMap);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.bottom_sheet_order_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderHeaderLists list = o_Order_HeaderLists.get(position);
        holder.header.setText(list.getHeader());

        //get item list

        String header = list.getHeader();
        MyLog.e(TAG, "bottom>>header>>" + header);
        o_orderDishLists = new ArrayList<>();
        orderItemMap = orderHeaderMap.get(list.getHeader());
        Set<String> stringSet = orderItemMap.keySet();
        List<String> aList = new ArrayList<String>(stringSet.size());
        for (String x : stringSet)
            aList.add(x);
        orderItemLists = new ArrayList<>();
        for (int i = 0; i < aList.size(); i++) {
            String[] arr = (aList.get(i)).split("_");
            OrderItemList itemList = new OrderItemList();
            itemList.setItem(arr[0]);
            itemList.setSelected(arr[1]);
            orderItemLists.add(itemList);
        }

        holder.recyclerview_item_list.setHasFixedSize(true);
        holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        MyLog.e(TAG, "s_count>>sess>>" + session);
        ViewCartAdapterItem viewCartAdapterItem = new ViewCartAdapterItem(context, getViewModel, orderItemLists, orderItemMap, name,func, list.getHeader(), session, date);
        holder.recyclerview_item_list.setAdapter(viewCartAdapterItem);


    }


    @Override
    public int getItemCount() {
        return o_Order_HeaderLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView header;
        private RecyclerView recyclerview_item_list;


        public ViewHolder(View view) {
            super(view);
            recyclerview_item_list = view.findViewById(R.id.recyclerview_item_list);
            header = view.findViewById(R.id.header);

        }
    }
}


package com.example.adm.Fragments.Orders.OrdersAdapters;

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
import com.example.adm.Fragments.Orders.Classes.SelectedSessionList;
import com.example.adm.Fragments.Orders.Classes.UserItemList;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class UserSessionListAdapter extends RecyclerView.Adapter<UserSessionListAdapter.ViewHolder> {
    private Context context;
    private String TAG = "UserSessionListAdapter";
    private String s_session_title,bolen,date,time;
    private GetViewModel getViewModel;
    //order hash map
    //order map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>> orderMap = new LinkedHashMap<>();
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
    //selected session list
    private List<SelectedSessionList> o_selectedSessionLists=new ArrayList<>();
    //selected header list
    private List<OrderHeaderLists> o_Order_HeaderLists =new ArrayList<>();
    //order header title and item size
    private List<UserItemList>  o_userItemLists=new ArrayList<>();
    //order item list
    private List<OrderDishLists> o_orderDishLists =new ArrayList<>();

    public UserSessionListAdapter(Context context, GetViewModel getViewModel, List<SelectedSessionList> o_selectedSessionLists, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>> orderSessionMap) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.orderSessionMap = orderSessionMap;
        this.o_selectedSessionLists = o_selectedSessionLists;

    }

    @NonNull
    @Override
    public UserSessionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.session_cardview, parent, false);
        return new UserSessionListAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull UserSessionListAdapter.ViewHolder holder, int position) {

        final SelectedSessionList sessionLists1 = o_selectedSessionLists.get(position);

        holder.session_title.setText(sessionLists1.getSession_title());
        holder.date_time.setText(sessionLists1.getTime());
        holder.count.setText(sessionLists1.getCount());
        MyLog.e(TAG,"orders>> date time "+sessionLists1.getTime());

        String s=sessionLists1.getSession_title()+"!"+sessionLists1.getTime()+"-"+sessionLists1.getCount()+"_"+sessionLists1.getBolen();
        orderHeaderMap=orderSessionMap.get(s);
        //get header list
        Set<String> stringSet1 = orderHeaderMap.keySet();
        List<String> aList1 = new ArrayList<String>(stringSet1.size());
        for (String x1 : stringSet1)
            aList1.add(x1);
        o_Order_HeaderLists =new ArrayList<>();
        for(int k=0;k<aList1.size();k++) {
            OrderHeaderLists orderHeaderLists =new OrderHeaderLists(
                    aList1.get(k)
            );
            o_Order_HeaderLists.add(orderHeaderLists);
        }
        o_userItemLists=new ArrayList<>();
        for(int i = 0; i< o_Order_HeaderLists.size(); i++) {
            String header = o_Order_HeaderLists.get(i).getHeader();
            orderItemMap = orderHeaderMap.get(header);
            Set<String> stringSet = orderItemMap.keySet();
            List<String> aList = new ArrayList<String>(stringSet.size());
            for (String x : stringSet)
                aList.add(x);
            //user item list
            UserItemList itemList = new UserItemList(
                    header,
                    aList.size()
            );
            o_userItemLists.add(itemList);

            holder.itemList.setHasFixedSize(true);
            holder.itemList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            UserItemListAdapters itemListAdapters = new UserItemListAdapters(context, getViewModel, o_userItemLists);
            holder.itemList.setAdapter(itemListAdapters);
        }

    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "sessionLists>>49>>" + o_selectedSessionLists.size());
        return o_selectedSessionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView session_title,date_time,count;
            private RecyclerView itemList;
        public ViewHolder(View view) {
            super(view);
            session_title = view.findViewById(R.id.session_title);
            date_time = view.findViewById(R.id.date_time);
            itemList = view.findViewById(R.id.itemList);
            count = view.findViewById(R.id.count);


        }
    }
}


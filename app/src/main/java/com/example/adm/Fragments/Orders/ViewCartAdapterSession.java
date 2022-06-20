package com.example.adm.Fragments.Orders;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SessionList;
import com.example.adm.Fragments.Orders.BottomSheet.OrderItemLists;
import com.example.adm.Fragments.Orders.BottomSheet.OrderLists;
import com.example.adm.Fragments.Orders.BottomSheet.SelectedHeader;
import com.example.adm.Fragments.Orders.BottomSheet.ViewCartAdapter;

import com.example.adm.Fragments.Orders.BottomSheet.ViewCartAdapterHeader;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ViewCartAdapterSession extends RecyclerView.Adapter<ViewCartAdapterSession.ViewHolder> {
    private Context context;
    private String TAG = "ViewCartAdapterSession";
    private List<SelectedHeader> o_selectedHeaders = new ArrayList<>();
    private List<SelectedSessionList> o_selectedSessionLists = new ArrayList<>();
    private GetViewModel getViewModel;
    private OrderLists orderLists;

    //header map
    private LinkedHashMap<String, List<OrderItemLists>> orderHeaderMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap = new LinkedHashMap<>();
    //order item list
    private List<OrderItemLists> o_orderItemLists = new ArrayList<>();


    public ViewCartAdapterSession(Context context, GetViewModel getViewModel, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap, String date, List<SelectedSessionList> o_selectedSessionLists) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.orderSessionMap = new LinkedHashMap<>(orderSessionMap);
        this.o_selectedSessionLists = new ArrayList<>(o_selectedSessionLists);
    }


    @NonNull
    @Override
    public ViewCartAdapterSession.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.bottom_sheet_order_session, parent, false);
        return new ViewCartAdapterSession.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCartAdapterSession.ViewHolder holder, int position) {
        final SelectedSessionList list = o_selectedSessionLists.get(position);
        holder.session_title.setText(list.getSession_title());

        String s = list.getSession_title() + "!" + list.getTime() + "_" + list.getBolen();
        MyLog.e(TAG,"bottom>>sess>"+s);

        orderHeaderMap = new LinkedHashMap<>(orderSessionMap).get(s);
        //get header list
        Set<String> stringSet = orderHeaderMap.keySet();

        List<String> aList = new ArrayList<String>(stringSet.size());
        for (String x : stringSet)
            aList.add(x);
        o_selectedHeaders=new ArrayList<>();
        for (int k = 0; k < aList.size(); k++) {
            SelectedHeader selectedHeader = new SelectedHeader(
                    aList.get(k)
            );
            o_selectedHeaders.add(selectedHeader);
        }

        holder.recyclerview_order_item_details.setHasFixedSize(true);
        holder.recyclerview_order_item_details.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ViewCartAdapterHeader itemListAdapters = new ViewCartAdapterHeader(context, getViewModel, o_selectedHeaders, orderHeaderMap);
        holder.recyclerview_order_item_details.setAdapter(itemListAdapters);


    }


    @Override
    public int getItemCount() {
        return o_selectedSessionLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView session_title;
        private RecyclerView recyclerview_order_item_details;


        public ViewHolder(View view) {
            super(view);
            recyclerview_order_item_details = view.findViewById(R.id.recyclerview_order_item_details);
            session_title = view.findViewById(R.id.session_title);

        }
    }
}


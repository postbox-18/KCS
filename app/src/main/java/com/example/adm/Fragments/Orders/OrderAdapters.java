package com.example.adm.Fragments.Orders;

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

import com.example.adm.Classes.MyLog;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class OrderAdapters extends RecyclerView.Adapter<OrderAdapters.ViewHolder> {
    private List<OrderLists> orderLists;
    private Context context;
    private GetViewModel getViewModel;
    private String TAG = "OrderAdapters";
    private List<UserItemList> userItemLists=new ArrayList<>();
    private LinkedHashMap<String, List<UserItemList>> stringListLinkedHashMap=new LinkedHashMap<>();

    public OrderAdapters(Context context, List<OrderLists> orderLists, GetViewModel getViewModel) {
        this.orderLists = orderLists;
        this.context = context;
        this.getViewModel = getViewModel;
    }

    @NonNull
    @Override
    public OrderAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_cardview, parent, false);
        return new OrderAdapters.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapters.ViewHolder holder, int position) {
        final OrderLists orderLists1 = orderLists.get(position);
        holder.user_name.setText(orderLists1.getS_user_name());
        holder.func.setText(orderLists1.getFunc());
        MyLog.e(TAG,"item>>name outside>"+orderLists1.getS_user_name());

        //get checked list in hash map
        getViewModel.getS_mapMutable().observe((LifecycleOwner) context, new Observer<List<LinkedHashMap<String, List<UserItemList>>>>() {
            @Override
            public void onChanged(List<LinkedHashMap<String, List<UserItemList>>> linkedHashMaps) {

                userItemLists=new ArrayList<>();

                for(int i=0;i<linkedHashMaps.size();i++) {

                    userItemLists = linkedHashMaps.get(i).get(orderLists1.getS_user_name()+" "+ orderLists1.getFunc());
                    MyLog.e(TAG,"f_map>>"+orderLists1.getS_user_name());
                    MyLog.e(TAG,"f_map>>after"+new GsonBuilder().setPrettyPrinting().create().toJson(userItemLists));
                    holder.itemList.setHasFixedSize(true);
                    holder.itemList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    UserItemListAdapters itemListAdapters = new UserItemListAdapters(context, getViewModel, userItemLists);
                    holder.itemList.setAdapter(itemListAdapters);
                }
            }
        });

        //view click
        holder.item_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setFunc_title(orderLists1.getFunc());
                getViewModel.setOrderListsView(orderLists1);
            }
        });

        //get linked hash map checked
        getViewModel.getF_mapMutable().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<UserItemList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<UserItemList>> stringListLinkedHashMap1) {
                stringListLinkedHashMap=stringListLinkedHashMap1;

            }
        });


    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "orderLists>>49>>" + orderLists.size());
        return orderLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView user_name, func;
        private RecyclerView itemList;
        private CardView item_cardView;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            user_name = view.findViewById(R.id.user_name);
            func = view.findViewById(R.id.func);
            itemList = view.findViewById(R.id.itemList);
            item_cardView = view.findViewById(R.id.item_cardView);


        }
    }
}

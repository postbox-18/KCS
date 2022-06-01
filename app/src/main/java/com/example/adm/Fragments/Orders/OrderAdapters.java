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
import com.example.adm.Classes.SessionList;
import com.example.adm.Fragments.Orders.BottomSheet.OrderLists;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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

        //get session list
        getViewModel.getSessionListsMutableLiveData().observe((LifecycleOwner) context, new Observer<List<SessionList>>() {
            @Override
            public void onChanged(List<SessionList> sessionLists) {
                holder.recyclerview_session.setHasFixedSize(true);
                holder.recyclerview_session.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                UserSessionListAdapter userSessionListAdapter=new UserSessionListAdapter(context,getViewModel,orderLists1,sessionLists);
                holder.recyclerview_session.setAdapter(userSessionListAdapter);

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
        private RecyclerView recyclerview_session;
        private CardView item_cardView;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            user_name = view.findViewById(R.id.user_name);
            func = view.findViewById(R.id.func);
            recyclerview_session = view.findViewById(R.id.recyclerview_session);
            item_cardView = view.findViewById(R.id.item_cardView);


        }
    }
}

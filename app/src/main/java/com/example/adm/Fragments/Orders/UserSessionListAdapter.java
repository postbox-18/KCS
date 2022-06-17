package com.example.adm.Fragments.Orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class UserSessionListAdapter extends RecyclerView.Adapter<UserSessionListAdapter.ViewHolder> {
    private Context context;
    private String TAG = "UserSessionListAdapter";
    private String s_session_title,bolen,s_date_time;
    private GetViewModel getViewModel;
    private List<SessionList> sessionLists=new ArrayList<>();
    private OrderLists orderLists1;
    private List<UserItemList> userItemLists=new ArrayList<>();

    public UserSessionListAdapter(Context context, GetViewModel getViewModel, OrderLists orderLists1, List<SessionList> sessionLists) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.orderLists1 = orderLists1;
        this.sessionLists = sessionLists;
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

        final SessionList sessionLists1 = sessionLists.get(position);
        String[] str=(sessionLists1.getSession_title()).split("_");
        bolen=str[1];
        String[]s=(str[0]).split("!");
        s_session_title=s[0];
        s_date_time=s[1];
        holder.session_title.setText(s_session_title);
        holder.date_time.setText(s_date_time);
        //get item checked list in hash map
        getViewModel.getS_mapMutable().observe((LifecycleOwner) context, new Observer<List<LinkedHashMap<String, List<UserItemList>>>>() {
            @Override
            public void onChanged(List<LinkedHashMap<String, List<UserItemList>>> linkedHashMaps) {

                MyLog.e(TAG,"ada>>sessionLists>>>>"+ linkedHashMaps.size());
                userItemLists=new ArrayList<>();
                for(int i=0;i<linkedHashMaps.size();i++) {
                    MyLog.e(TAG,"tasl>>"+orderLists1.getS_user_name()+"-"+ orderLists1.getFunc()+"-"+sessionLists1.getSession_title());
                    userItemLists = linkedHashMaps.get(i).get(orderLists1.getS_user_name()+"-"+ orderLists1.getFunc()+"-"+sessionLists1.getSession_title());
                    holder.itemList.setHasFixedSize(true);
                    holder.itemList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    UserItemListAdapters itemListAdapters = new UserItemListAdapters(context, getViewModel, userItemLists);
                    holder.itemList.setAdapter(itemListAdapters);
                }

            }
        });



    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "sessionLists>>49>>" + sessionLists.size());
        return sessionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView session_title,date_time;
            private RecyclerView itemList;
        public ViewHolder(View view) {
            super(view);
            session_title = view.findViewById(R.id.session_title);
            date_time = view.findViewById(R.id.date_time);
            itemList = view.findViewById(R.id.itemList);


        }
    }
}


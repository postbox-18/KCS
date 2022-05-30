package com.example.kcs.Fragment.Items.ItemSelectedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kcs.Classes.MyLog;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.List;

public class UserItemListAdapters extends RecyclerView.Adapter<UserItemListAdapters.ViewHolder> {
    private Context context;
    private String TAG = "ItemListAdapters";
    private GetViewModel getViewModel;
    private List<UserItemList> userItemLists;

    public UserItemListAdapters(Context context, GetViewModel getViewModel, List<UserItemList> userItemLists) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.userItemLists = userItemLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_cardview, parent, false);
        return new ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //MyLog.e(TAG, "items>>userList adapter>>" + new GsonBuilder().setPrettyPrinting().create().toJson(userItemLists));
        final UserItemList userItemList1 = userItemLists.get(position);
        MyLog.e(TAG, "items>>header>" + userItemList1.getHeader());
        MyLog.e(TAG, "items>>size>>" + userItemList1.getList_size());
        holder.item_size.setText(String.valueOf(userItemList1.getList_size()));
        holder.header.setText(userItemList1.getHeader());


    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "userItemList>>49>>" + userItemLists.size());
        return userItemLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView header, item_size;


        public ViewHolder(View view) {
            super(view);
            header = view.findViewById(R.id.header);
            item_size = view.findViewById(R.id.item_size);


        }
    }
}


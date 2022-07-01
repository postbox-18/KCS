package com.example.adm.Fragments.Orders.OrdersAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Orders.Classes.UserItemList;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserItemListAdapters extends RecyclerView.Adapter<UserItemListAdapters.ViewHolder> {
    private Context context;
    private String TAG = "ItemListAdapters";
    private GetViewModel getViewModel;

    //order header title and item size
    private List<UserItemList>  o_userItemLists=new ArrayList<>();



    public UserItemListAdapters(Context context, GetViewModel getViewModel, List<UserItemList> o_userItemLists) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.o_userItemLists = o_userItemLists;
    }

    @NonNull
    @Override
    public UserItemListAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_cardview, parent, false);
        return new UserItemListAdapters.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemListAdapters.ViewHolder holder, int position) {


        final UserItemList userItemList1 = o_userItemLists.get(position);
        MyLog.e(TAG, "item>>header>" + userItemList1.getHeader());
        MyLog.e(TAG, "item>>size>>" + userItemList1.getList_size());
        holder.item_size.setText(String.valueOf(userItemList1.getList_size()));
        holder.header.setText(userItemList1.getHeader());


    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "userItemList>>49>>" + o_userItemLists.size());
        return o_userItemLists.size();
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


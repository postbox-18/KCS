package com.example.kcs.Fragment.Profile.MyOrders.MyOrdersItems;

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

import java.util.List;

public class MyorderItemListAdapters extends RecyclerView.Adapter<MyorderItemListAdapters.ViewHolder> {
    private Context context;
    private String TAG = "ItemListAdapters";
    private GetViewModel getViewModel;
    private List<MyOrdersList> myOrdersLists;

    public MyorderItemListAdapters(Context context, GetViewModel getViewModel, List<MyOrdersList> myOrdersLists) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.myOrdersLists = myOrdersLists;
    }

    @NonNull
    @Override
    public MyorderItemListAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.myorderlist_cardview, parent, false);
        return new MyorderItemListAdapters.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull MyorderItemListAdapters.ViewHolder holder, int position) {

        final MyOrdersList myOrdersList1 = myOrdersLists.get(position);
        MyLog.e(TAG, "items>>header>" + myOrdersList1.getHeader());
        MyLog.e(TAG, "items>>size>>" + myOrdersList1.getSize());
        holder.item_size.setText(String.valueOf(myOrdersList1.getSize()));
        holder.header.setText(myOrdersList1.getHeader());


    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "myOrdersList>>49>>" + myOrdersLists.size());
        return myOrdersLists.size();
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


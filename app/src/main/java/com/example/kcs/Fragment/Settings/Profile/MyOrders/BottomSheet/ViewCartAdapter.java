package com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewCartAdapter extends RecyclerView.Adapter<ViewCartAdapter.ViewHolder> {
    private Context context;
    private List<OrderDishLists> orderDishListsses = new ArrayList<>();
    private String TAG = "ViewCartAdapter";
    private GetViewModel getViewModel;
    private String func_title, header,username,date,sess;
    private int n;


    public ViewCartAdapter(Context context, GetViewModel getViewModel, List<OrderDishLists> orderDishListsses, String func_title, String header, String sess, String date) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.orderDishListsses = orderDishListsses;
        this.func_title = func_title;
        this.header = header;
        this.sess = sess;
        this.date = date;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.bottom_sheet_order_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderDishLists orderDishLists1 = orderDishListsses.get(position);
        holder.list.setText(orderDishLists1.getItemList());


        //get username
        username=new SharedPreferences_data(context).getS_user_name();

        //get Edit Cancel Delete
        getViewModel.getEcdLive().observe((LifecycleOwner) context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                n=integer;
            }
        });
        MyLog.e(TAG, "s_count>>sess>>" + sess);

        //set edit hash map
        getViewModel.EditMap(func_title, header, orderDishLists1.getItemList(), position,n,username,sess,date);

    }


    @Override
    public int getItemCount() {
        return orderDishListsses.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView list;

        public ViewHolder(View view) {
            super(view);
            list = view.findViewById(R.id.list);

        }
    }
}


package com.example.kcs.Fragment.Dish.DishSelectedList;

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

public class UserDishListAdapters extends RecyclerView.Adapter<UserDishListAdapters.ViewHolder> {
    private Context context;
    private String TAG = "ItemListAdapters";
    private GetViewModel getViewModel;
    private List<UserDishList> userDishLists;

    public UserDishListAdapters(Context context, GetViewModel getViewModel, List<UserDishList> userDishLists) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.userDishLists = userDishLists;
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

        final UserDishList userDishList1 = userDishLists.get(position);
        MyLog.e(TAG, "items>>header>" + userDishList1.getHeader());
        MyLog.e(TAG, "items>>size>>" + userDishList1.getList_size());
        holder.item_size.setText(String.valueOf(userDishList1.getList_size()));
        holder.header.setText(userDishList1.getHeader());


    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "userItemList>>49>>" + userDishLists.size());
        return userDishLists.size();
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


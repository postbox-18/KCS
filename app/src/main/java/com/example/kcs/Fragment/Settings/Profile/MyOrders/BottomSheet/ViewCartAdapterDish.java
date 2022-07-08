package com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ViewCartAdapterDish  extends RecyclerView.Adapter<ViewCartAdapterDish.ViewHolder> {
    private Context context;
    private List<OrderDishLists> orderDishListsses = new ArrayList<>();
    private String TAG = "ViewCartAdapterDish";
    private GetViewModel getViewModel;
    private String func_title, header,username,date,sess,item_title;
    private int n;


    public ViewCartAdapterDish(Context context, GetViewModel getViewModel, List<OrderDishLists> orderDishListsses) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.orderDishListsses = orderDishListsses;
    }


    @NonNull
    @Override
    public ViewCartAdapterDish.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.bottom_sheet_order_dish_list, parent, false);
        return new ViewCartAdapterDish.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCartAdapterDish.ViewHolder holder, int position) {
        final OrderDishLists orderDishLists1 = orderDishListsses.get(position);
        holder.dish.setText(orderDishLists1.getItemList());


        //get username
        username=new SharedPreferences_data(context).getS_user_name();

        //get func title
        getViewModel.getFunc_title_Mutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                func_title=s;
            }
        });
        //get date
        getViewModel.getSelected_dateMutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                date=s;
            }
        });
        //get session title
        getViewModel.getSessionDateTimeCountMutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                sess=s;
            }
        });
        //get header title
        getViewModel.getHeader_title_Mutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                header=s;
            }

        });
        //get item title
        getViewModel.getItem_title_Mutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                item_title=s;
            }
        });


        //get Edit Cancel Delete
        getViewModel.getEcdLive().observe((LifecycleOwner) context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                n=integer;
            }
        });
        MyLog.e(TAG, "s_count>>sess>>" + sess);

        //set edit hash map
        //getViewModel.EditMap(func_title, header, orderDishLists1.getItemList(),item_title, position,n,username,sess,date);

    }


    @Override
    public int getItemCount() {
        return orderDishListsses.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dish;

        public ViewHolder(View view) {
            super(view);
            dish = view.findViewById(R.id.dish);

        }
    }
}


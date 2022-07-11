package com.example.adm.Fragments.Orders.BottomSheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SharedPreferences_data;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderDishLists;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewCartAdapterDish extends RecyclerView.Adapter<ViewCartAdapterDish.ViewHolder> {
    private Context context;
    private List<OrderDishLists> orderDishListsses = new ArrayList<>();
    private String TAG = "ViewCartAdapterDish";
    private GetViewModel getViewModel;
    private String func_title, header, username, date, sess, item_title;
    private int n;


    public ViewCartAdapterDish(Context context, GetViewModel getViewModel, List<OrderDishLists> orderDishListsses, String func_title, String date, String sess, String header, String item) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.orderDishListsses = orderDishListsses;
        this.func_title = func_title;
        this.date = date;
        this.sess = sess;
        this.header = header;
        this.item_title = item;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.bottom_sheet_order_dish_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderDishLists orderDishLists1 = orderDishListsses.get(position);
        holder.dish.setText(orderDishLists1.getItemList());

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


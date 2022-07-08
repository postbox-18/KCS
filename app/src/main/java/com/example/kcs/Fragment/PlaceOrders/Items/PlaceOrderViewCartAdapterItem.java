package com.example.kcs.Fragment.PlaceOrders.Items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.Fragment.Items.ItemList;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.PlaceOrderViewAdapterDish;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PlaceOrderViewCartAdapterItem extends RecyclerView.Adapter<PlaceOrderViewCartAdapterItem.ViewHolder> {
    private Context context;
    private List<CheckedList> checkedLists = new ArrayList<>();
    private List<ItemList> itemLists=new ArrayList<>();
    private String TAG = "PlaceOrderViewCartAdapterItem";
    private GetViewModel getViewModel;
    private List<SelectedHeader> e_selectedHeaders = new ArrayList<>();
    //item map
    private LinkedHashMap<String, List<CheckedList>> itemMap = new LinkedHashMap<>();


    public PlaceOrderViewCartAdapterItem(Context context, GetViewModel getViewModel, LinkedHashMap<String, List<CheckedList>> itemMap, List<ItemList> itemLists) {
        this.context=context;
        this.getViewModel=getViewModel;
        this.itemMap=itemMap;
        this.itemLists=itemLists;
    }



    @NonNull
    @Override
    public PlaceOrderViewCartAdapterItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.place_view_item, parent, false);
        return new PlaceOrderViewCartAdapterItem.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOrderViewCartAdapterItem.ViewHolder holder, int position) {
        MyLog.e(TAG, "dish>>PlaceOrderViewCartAdapterItem");
        final ItemList itemLists1=itemLists.get(position);
        holder.list.setText(itemLists1.getItem());
        if((itemLists1.getSelected()).equals("true"))
        {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.btn_gradient_light));
        } else if((itemLists1.getSelected()).equals("false"))
        {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.text_silver));
        }
        checkedLists=itemMap.get(itemLists1.getItem()+"_"+itemLists1.getSelected());
        holder.recyclerview_dish.setHasFixedSize(true);
        holder.recyclerview_dish.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        PlaceOrderViewAdapterDish placeOrderViewAdapterDish = new PlaceOrderViewAdapterDish(context, getViewModel, checkedLists);
        holder.recyclerview_dish.setAdapter(placeOrderViewAdapterDish);


    }


    @Override
    public int getItemCount() {

            return itemLists.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView list;
        private LinearLayout layout;
        private RecyclerView recyclerview_dish;

        public ViewHolder(View view) {
            super(view);
            list = view.findViewById(R.id.list);
            recyclerview_dish = view.findViewById(R.id.recyclerview_dish);
            layout = view.findViewById(R.id.layout);

        }
    }
}


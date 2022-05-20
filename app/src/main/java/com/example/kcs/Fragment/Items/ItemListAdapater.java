package com.example.kcs.Fragment.Items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.ItemList;
import com.example.kcs.R;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ItemListAdapater extends RecyclerView.Adapter<ItemListAdapater.ViewHolder>  {
    private Context context;
    private List<ItemList> itemLists;
    private String TAG="ItemListAdapater";
    public ItemListAdapater(Context context, List<ItemList> itemLists) {
        this.context=context;
        this.itemLists=itemLists;

    }

    @NonNull
    @Override
    public ItemListAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_checkbox_list, parent, false);
        return new ItemListAdapater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapater.ViewHolder holder, int position) {
        final ItemList itemList1 = itemLists.get(position);
        //img update soon
        //holder.header_img.setText(funList1.getUsername());
        MyLog.e(TAG, "Data>>header itemadapter>>" + new GsonBuilder().setPrettyPrinting().create().toJson(itemList1));
        holder.item_check.setText(itemList1.getItem());


    }

    @Override
    public int getItemCount() {
        return itemLists.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox item_check;
        private CardView item_check_card;
        public ViewHolder(View view) {
            super(view);
            item_check=view.findViewById(R.id.item_check);
            item_check_card=view.findViewById(R.id.item_check_card);

        }
    }
}

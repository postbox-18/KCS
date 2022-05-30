package com.example.adm.Fragments.Orders.BottomSheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewCartAdapter extends RecyclerView.Adapter<ViewCartAdapter.ViewHolder> {
    private Context context;
    private List<OrderItemLists> orderItemListss = new ArrayList<>();
    private String TAG = "ViewCartAdapter";
    private GetViewModel getViewModel;


    public ViewCartAdapter(Context context, GetViewModel getViewModel, List<OrderItemLists> orderItemListss) {
        this.context=context;
        this.getViewModel=getViewModel;
        this.orderItemListss=orderItemListss;
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
        final OrderItemLists orderItemLists1=orderItemListss.get(position);
            holder.list.setText(orderItemLists1.getItemList());


    }


    @Override
    public int getItemCount() {
        return orderItemListss.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView list;

        public ViewHolder(View view) {
            super(view);
            list = view.findViewById(R.id.list);

        }
    }
}


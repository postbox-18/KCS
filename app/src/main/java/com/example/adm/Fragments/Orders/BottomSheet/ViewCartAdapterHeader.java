package com.example.adm.Fragments.Orders.BottomSheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderItemLists;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.SelectedHeader;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ViewCartAdapterHeader extends RecyclerView.Adapter<ViewCartAdapterHeader.ViewHolder> {
    private Context context;
    private String TAG = "ViewCartAdapterHeader";
    private GetViewModel getViewModel;
    private List<SelectedHeader> o_selectedHeaders = new ArrayList<>();
    //header map
    private LinkedHashMap<String, List<OrderItemLists>> orderHeaderMap = new LinkedHashMap<>();
    //user item list
    private List<OrderItemLists> o_orderItemLists = new ArrayList<>();


    public ViewCartAdapterHeader(Context context, GetViewModel getViewModel, List<SelectedHeader> o_selectedHeaders, LinkedHashMap<String, List<OrderItemLists>> orderHeaderMap) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.o_selectedHeaders = new ArrayList<>(o_selectedHeaders);
        this.orderHeaderMap = new LinkedHashMap<>(orderHeaderMap);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.bottom_sheet_order_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SelectedHeader list = o_selectedHeaders.get(position);
        holder.header.setText(list.getHeader());

        //get item list

        String header = list.getHeader();
        MyLog.e(TAG, "bottom>>header>>" +header );
        o_orderItemLists=new ArrayList<>();
        o_orderItemLists=orderHeaderMap.get(header);

        holder.recyclerview_item_list.setHasFixedSize(true);
        holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ViewCartAdapter itemListAdapters = new ViewCartAdapter(context, getViewModel, o_orderItemLists);
        holder.recyclerview_item_list.setAdapter(itemListAdapters);


    }


    @Override
    public int getItemCount() {
        return o_selectedHeaders.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView header;
        private RecyclerView recyclerview_item_list;


        public ViewHolder(View view) {
            super(view);
            recyclerview_item_list = view.findViewById(R.id.recyclerview_item_list);
            header = view.findViewById(R.id.header);

        }
    }
}


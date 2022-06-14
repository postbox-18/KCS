package com.example.kcs.Fragment.Profile.MyOrders.BottomSheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ViewCartAdapterHeader extends RecyclerView.Adapter<ViewCartAdapterHeader.ViewHolder> {
    private Context context;
    private List<OrderItemLists> orderItemListss = new ArrayList<>();
    private ViewCartAdapter viewCartAdapter;
    private String TAG = "ViewCartAdapterHeader";
    private String session_title,func_title;

    private GetViewModel getViewModel;
    private List<SelectedHeader> header=new ArrayList<>();


    public ViewCartAdapterHeader(Context context, GetViewModel getViewModel, List<SelectedHeader> selectedHeadersList, String session_title, String func_title) {
        this.context=context;
        this.getViewModel=getViewModel;
        this.header=selectedHeadersList;
        this.session_title=session_title;
        this.func_title=func_title;
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
        final SelectedHeader list=header.get(position);
        holder.header.setText(list.getHeader());
        //get order item view list hash map
        getViewModel.getOrderItemList_f_mapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<OrderItemLists>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<OrderItemLists>> stringListLinkedHashMap) {
                orderItemListss=new ArrayList<>();
                MyLog.e(TAG,"myord>> header>>"+func_title+"-"+session_title+"-"+list.getHeader());
                orderItemListss=stringListLinkedHashMap.get(func_title+"-"+session_title+"-"+list.getHeader());
                holder.recyclerview_item_list.setHasFixedSize(true);
                holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                if(orderItemListss!=null) {
                    viewCartAdapter = new ViewCartAdapter(context, getViewModel, orderItemListss);
                    holder.recyclerview_item_list.setAdapter(viewCartAdapter);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return header.size();
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


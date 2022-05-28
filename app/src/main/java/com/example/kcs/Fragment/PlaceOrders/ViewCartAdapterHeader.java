package com.example.kcs.Fragment.PlaceOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Fragment.Items.CheckedList;

import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewCartAdapterHeader extends RecyclerView.Adapter<ViewCartAdapterHeader.ViewHolder> {
    private Context context;
    private List<CheckedList> checkedLists = new ArrayList<>();
    private ViewCartAdapter viewCartAdapter;
    private String TAG = "ViewCartAdapter";

    private GetViewModel getViewModel;
    private List<String> header;


    public ViewCartAdapterHeader(Context context, GetViewModel getViewModel, List<String> header, List<CheckedList> checkedLists) {
        this.context=context;
        this.getViewModel=getViewModel;
        this.header=header;
        this.checkedLists=checkedLists;
    }



    @NonNull
    @Override
    public ViewCartAdapterHeader.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.view_cart_card_view_header, parent, false);
        return new ViewCartAdapterHeader.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCartAdapterHeader.ViewHolder holder, int position) {
        final String list=header.get(position);
        holder.header.setText(list);
        holder.recyclerview_item_list.setHasFixedSize(true);
        holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        viewCartAdapter=new ViewCartAdapter(context,getViewModel,checkedLists);
        holder.recyclerview_item_list.setAdapter(viewCartAdapter);


    }


    @Override
    public int getItemCount() {
        return 1;
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


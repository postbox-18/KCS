package com.example.kcs.Fragment.PlaceOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlaceOrderViewAdapterDish extends RecyclerView.Adapter<PlaceOrderViewAdapterDish.ViewHolder> {
    private Context context;
    private List<CheckedList> checkedLists = new ArrayList<>();
    private String TAG = "PlaceOrderViewAdapterDish";
    private GetViewModel getViewModel;


    public PlaceOrderViewAdapterDish(Context context, GetViewModel getViewModel, List<CheckedList> checkedLists) {
        this.context=context;
        this.getViewModel=getViewModel;
        this.checkedLists=checkedLists;
    }



    @NonNull
    @Override
    public PlaceOrderViewAdapterDish.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.place_view_dish, parent, false);
        return new PlaceOrderViewAdapterDish.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOrderViewAdapterDish.ViewHolder holder, int position) {
       /* if(e_selectedHeaders==null) {*/
            final CheckedList checkedList1 = checkedLists.get(position);
            holder.dish.setText(checkedList1.getItemList());
        /*}
        else {
            final SelectedHeader checkedList1 = e_selectedHeaders.get(position);
            holder.dish.setText(checkedList1.getHeader());
        }*/
    }


    @Override
    public int getItemCount() {
        /*if(e_selectedHeaders==null) {*/
            return checkedLists.size();
        /*}
        else
        {
            return e_selectedHeaders.size();
        }*/
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dish;

        public ViewHolder(View view) {
            super(view);
            dish = view.findViewById(R.id.dish);

        }
    }
}

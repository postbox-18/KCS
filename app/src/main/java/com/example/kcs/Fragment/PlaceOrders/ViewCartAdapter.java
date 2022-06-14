package com.example.kcs.Fragment.PlaceOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewCartAdapter extends RecyclerView.Adapter<ViewCartAdapter.ViewHolder> {
    private Context context;
    private List<CheckedList> checkedLists = new ArrayList<>();
    private String TAG = "ViewCartAdapter";
    private GetViewModel getViewModel;


    public ViewCartAdapter(Context context, GetViewModel getViewModel, List<CheckedList> checkedLists) {
        this.context=context;
        this.getViewModel=getViewModel;
        this.checkedLists=checkedLists;
    }



    @NonNull
    @Override
    public ViewCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.view_cart_card_view, parent, false);
        return new ViewCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCartAdapter.ViewHolder holder, int position) {
        final CheckedList checkedList1=checkedLists.get(position);
            holder.list.setText(checkedList1.getItemList());
    }


    @Override
    public int getItemCount() {
        return checkedLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView list;

        public ViewHolder(View view) {
            super(view);
            list = view.findViewById(R.id.list);

        }
    }
}


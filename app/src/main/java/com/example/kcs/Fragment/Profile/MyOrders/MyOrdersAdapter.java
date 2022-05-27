package com.example.kcs.Fragment.Profile.MyOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.R;

import java.util.LinkedHashMap;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {
    private List<MyOrdersList> myOrdersList;
    private Context context;
    private String TAG="MyOrdersAdapter";
    public MyOrdersAdapter(Context context, List<MyOrdersList> myOrdersList) {
        this.myOrdersList = myOrdersList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.myorder_cardview, parent, false);
        return new MyOrdersAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersAdapter.ViewHolder holder, int position) {
        final MyOrdersList myOrdersList1 = myOrdersList.get(position);

        //get data func,header,list item size from hash map
        holder.header.setText(myOrdersList1.getHeader());
        /*holder.func.setText(myOrdersList1.getFunc());*/

        holder.item_size.setText(String.valueOf(myOrdersList1.getSize()));

    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "myOrdersList>>49>>" + myOrdersList.size());
        return myOrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView user_name, header, func,item_size;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            user_name = view.findViewById(R.id.user_name);
            header = view.findViewById(R.id.header);
            func = view.findViewById(R.id.func);
            item_size = view.findViewById(R.id.item_size);


        }
    }
}


package com.example.kcs.Fragment.Profile.MyOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.ViewModel.MyOrderFuncList;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {
    private Context context;
    private String TAG="MyOrdersAdapter";
    private List<MyOrderFuncList> myOrderFuncLists=new ArrayList<>();
    private List<MyOrdersList> myOrdersList;
    private GetViewModel getViewModel;
    public MyOrdersAdapter(Context context, List<MyOrderFuncList> myOrderFuncLists, GetViewModel getViewModel) {
        this.myOrderFuncLists = myOrderFuncLists;
        this.context = context;
        this.getViewModel = getViewModel;
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
        final MyOrderFuncList myOrderFuncLists1 = myOrderFuncLists.get(position);

        //get data func,header,list item size from hash map
        holder.func.setText(myOrderFuncLists1.getFunc());

        //get list hash map of my orders list
        getViewModel.getS_mapMyordersMutableLiveData().observe((LifecycleOwner) context, new Observer<List<LinkedHashMap<String, List<MyOrdersList>>>>() {
            @Override
            public void onChanged(List<LinkedHashMap<String, List<MyOrdersList>>> linkedHashMaps) {
                myOrdersList=new ArrayList<>();
                for (int i=0;i<linkedHashMaps.size();i++)
                {
                    myOrdersList=linkedHashMaps.get(i).get(myOrderFuncLists1.getFunc());
                    getViewModel.setMyOrdersList(myOrdersList);
                    MyLog.e(TAG,"myorder>>myOrdersList>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(myOrdersList));
                    holder.recyclerview_item_list.setHasFixedSize(true);
                    holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    MyorderItemListAdapters itemListAdapters = new MyorderItemListAdapters(context, getViewModel, myOrdersList);
                    holder.recyclerview_item_list.setAdapter(itemListAdapters);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "myOrderFuncLists>>49>>" + myOrderFuncLists.size());
        return myOrderFuncLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView   func;
        private RecyclerView recyclerview_item_list;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            recyclerview_item_list = view.findViewById(R.id.recyclerview_item_list);
            func = view.findViewById(R.id.func);


        }
    }
}


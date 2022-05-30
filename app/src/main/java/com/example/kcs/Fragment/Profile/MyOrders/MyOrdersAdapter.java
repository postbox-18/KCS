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

        //get linked hashmap
        //get hash map value to pass myorderlist
        getViewModel.getMyordersHashMapMutable().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<MyOrdersList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<MyOrdersList>> stringListLinkedHashMap) {
                //MyLog.e(TAG,"chs>>hahmap>> "+ new GsonBuilder().setPrettyPrinting().create().toJson(stringListLinkedHashMap));
                Set<String> stringSet=stringListLinkedHashMap.keySet();
                myOrdersList=new ArrayList<>();
                List<String> aList = new ArrayList<String>(stringSet.size());
                for (String x : stringSet)
                    aList.add(x);
                //MyLog.e(TAG,"chs>>list "+ new GsonBuilder().setPrettyPrinting().create().toJson(aList));
                for(int i=0;i<aList.size();i++) {
                    MyLog.e(TAG,"chs>>list header>> "+ aList.get(i));
                    MyLog.e(TAG,"chs>>list size "+ stringListLinkedHashMap.get(aList.get(i)).size());
                    MyOrdersList userItemList = new MyOrdersList(
                            aList.get(i),
                            stringListLinkedHashMap.get(aList.get(i)).size()
                    );
                    myOrdersList.add(userItemList);
                }

                //MyLog.e(TAG,"chs>>myorders>> "+ new GsonBuilder().setPrettyPrinting().create().toJson(myOrdersList));
                getViewModel.setMyOrdersList(myOrdersList);
                holder.recyclerview_item_list.setHasFixedSize(true);
                holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                MyorderItemListAdapters itemListAdapters = new MyorderItemListAdapters(context, getViewModel, myOrdersList);
                holder.recyclerview_item_list.setAdapter(itemListAdapters);

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


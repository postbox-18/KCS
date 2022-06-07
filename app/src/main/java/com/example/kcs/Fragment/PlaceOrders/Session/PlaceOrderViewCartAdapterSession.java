package com.example.kcs.Fragment.PlaceOrders.Session;

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
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.Fragment.PlaceOrders.Header.PlaceOrderViewCartAdapterHeader;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.Profile.MyOrders.BottomSheet.OrderItemLists;
import com.example.kcs.Fragment.Profile.MyOrders.BottomSheet.ViewCartAdapter;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class PlaceOrderViewCartAdapterSession extends RecyclerView.Adapter<PlaceOrderViewCartAdapterSession.ViewHolder> {
    private Context context;
    private List<OrderItemLists> orderItemListss = new ArrayList<>();
    private ViewCartAdapter viewCartAdapter;
    private String TAG = "PlaceOrderViewCartAdapterSession";
    private String func_title,s_user_name,date_time;
    private List<SelectedSessionList> sessionLists=new ArrayList<>();
    private GetViewModel getViewModel;
    private List<SelectedHeader> selectedHeaders=new ArrayList<>();
    private  LinkedHashMap<String, List<CheckedList>> headerMap=new LinkedHashMap<>();
    private  LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> sessionMap=new LinkedHashMap<>();
    private  LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> funcMap=new LinkedHashMap<>();



    public PlaceOrderViewCartAdapterSession(Context context, GetViewModel getViewModel, String s, List<SelectedSessionList> sessionLists, String date_time) {
        this.context=context;
        this.getViewModel=getViewModel;
        this.func_title=s;
        this.sessionLists=sessionLists;
        this.date_time=date_time;
    }



    @NonNull
    @Override
    public PlaceOrderViewCartAdapterSession.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.bottom_sheet_order_session, parent, false);
        return new PlaceOrderViewCartAdapterSession.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOrderViewCartAdapterSession.ViewHolder holder, int position) {
        //get user name shared prefernces
        s_user_name=new SharedPreferences_data(context).getS_user_name();

        final SelectedSessionList list=sessionLists.get(position);
        holder.session_title.setText(list.getSession_title());


        //get func hash map
        getViewModel.getFuncMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> stringLinkedHashMapLinkedHashMap) {
                funcMap=stringLinkedHashMapLinkedHashMap;
                sessionMap=funcMap.get(func_title);
                headerMap=sessionMap.get(list.getSession_title());
                Set<String> stringSet = headerMap.keySet();
                List<String> aList = new ArrayList<String>(stringSet.size());
                for (String x : stringSet)
                    aList.add(x);

                //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                selectedHeaders.clear();
                for (int i = 0; i < aList.size(); i++) {
                    MyLog.e(TAG, "chs>>list header>> " + aList.get(i));
                    SelectedHeader list = new SelectedHeader(
                            aList.get(i)

                    );
                    selectedHeaders.add(list);
                }
                getViewModel.setSelectedHeadersList(selectedHeaders);
                holder.recyclerview_order_item_details.setHasFixedSize(true);
                holder.recyclerview_order_item_details.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                PlaceOrderViewCartAdapterHeader viewCartAdapter=new PlaceOrderViewCartAdapterHeader(context,getViewModel,selectedHeaders,headerMap);
                holder.recyclerview_order_item_details.setAdapter(viewCartAdapter);
            }
        });





    }


    @Override
    public int getItemCount() {
        return sessionLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView session_title;
        private RecyclerView recyclerview_order_item_details;


        public ViewHolder(View view) {
            super(view);
            recyclerview_order_item_details = view.findViewById(R.id.recyclerview_order_item_details);
            session_title = view.findViewById(R.id.session_title);

        }
    }
}



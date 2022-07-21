package com.example.adm.Fragments.Orders.OrdersAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SessionList;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderDishLists;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderLists;
import com.example.adm.Fragments.Orders.Classes.SelectedDateList;
import com.example.adm.Fragments.Orders.Classes.UserItemList;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class OrderAdapters extends RecyclerView.Adapter<OrderAdapters.ViewHolder> {
    private List<OrderLists> orderLists;
    private Context context;
    private GetViewModel getViewModel;
    private String TAG = "OrderAdapters";
    private List<UserItemList> userItemLists=new ArrayList<>();
    private List<SessionList> sessionLists=new ArrayList<>();
    //order hash map
    //order map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>> orderMap = new LinkedHashMap<>();
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>> orderFunc_Map = new LinkedHashMap<>();
    //Date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>> orderDateMap = new LinkedHashMap<>();
    //Session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>> orderSessionMap = new LinkedHashMap<>();
    //Header map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>> orderHeaderMap = new LinkedHashMap<>();
    //Item map
    private LinkedHashMap<String, List<OrderDishLists>> orderItemMap = new LinkedHashMap<>();
    //date list
    private List<SelectedDateList> o_dateLists=new ArrayList<>();

    public OrderAdapters(Context context, List<OrderLists> orderLists, GetViewModel getViewModel) {
        this.orderLists = orderLists;
        this.context = context;
        this.getViewModel = getViewModel;
    }

    @NonNull
    @Override
    public OrderAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_cardview, parent, false);
        return new OrderAdapters.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapters.ViewHolder holder, int position) {
        final OrderLists orderLists1 = orderLists.get(position);
        holder.user_name.setText(orderLists1.getS_user_name());
        holder.phone_number.setText(orderLists1.getPhone_number());
        holder.func.setText(orderLists1.getFunc());
        MyLog.e(TAG,"item>>func >"+orderLists1.getFunc());
        getViewModel.getOrderMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>> stringLinkedHashMapLinkedHashMap) {
                orderMap=new LinkedHashMap<>(stringLinkedHashMapLinkedHashMap);
                String s=orderLists1.getS_user_name()+"-"+orderLists1.getPhone_number();
                orderFunc_Map=new LinkedHashMap<>(orderMap).get(s);
                orderDateMap=new LinkedHashMap<>(orderFunc_Map).get(orderLists1.getFunc());
                //get date list
                Set<String> stringSet = orderDateMap.keySet();
                List<String> aList = new ArrayList<String>(stringSet.size());
                for (String x : stringSet)
                    aList.add(x);
                o_dateLists=new ArrayList<>();
                for (int k=0;k<aList.size();k++)
                {
                    SelectedDateList list=new SelectedDateList(
                            aList.get(k)
                    );
                    o_dateLists.add(list);
                }
                holder.recyclerview_date.setHasFixedSize(true);
                holder.recyclerview_date.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                UserDateListAdapter userDateListAdapter=new UserDateListAdapter(context,getViewModel,o_dateLists,orderDateMap,orderLists1.getS_user_name(),orderLists1.getFunc());
                holder.recyclerview_date.setAdapter(userDateListAdapter);

            }
        });



        //view click
        holder.item_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setFunc_title(orderLists1.getFunc());
                getViewModel.setOrderListsView(orderLists1);

            }
        });



    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "orderLists>>49>>" + orderLists.size());
        return orderLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView user_name, func,phone_number;
        private RecyclerView recyclerview_date;
        private CardView item_cardView;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            user_name = view.findViewById(R.id.user_name);
            phone_number = view.findViewById(R.id.phone_number);
            func = view.findViewById(R.id.func);
            recyclerview_date = view.findViewById(R.id.recyclerview_date);
            item_cardView = view.findViewById(R.id.item_cardView);


        }
    }
}

package com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import java.util.Set;

public class ViewCartAdapterHeader extends RecyclerView.Adapter<ViewCartAdapterHeader.ViewHolder> {
    private Context context;
    private List<OrderDishLists> o_orderDishListsses = new ArrayList<>();
    private ViewCartAdapterItem viewCartAdapterItem;
    private String TAG = "ViewCartAdapterHeader";
    private String session_title, func_title, sess,date,bolen;

    private GetViewModel getViewModel;
    private List<SelectedHeader> header = new ArrayList<>();
    //edit hash map list
    private List<SelectedHeader> e_selectedHeaders = new ArrayList<>();
    //order hash map
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>> orderFunc_Map = new LinkedHashMap<>();
    //Date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>> orderDate_Map = new LinkedHashMap<>();
    //Session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>> orderSessionMap = new LinkedHashMap<>();
    //Header map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>> orderHeaderMap = new LinkedHashMap<>();
    //Item map
    private LinkedHashMap<String, List<OrderDishLists>> orderItemMap = new LinkedHashMap<>();
    private List<SelectedItemList> selectedItemLists =new ArrayList<>();


    public ViewCartAdapterHeader(Context context, GetViewModel getViewModel, List<SelectedHeader> selectedHeadersList, String session_title, String func_title, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>> orderHeaderMap, String date) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.header = selectedHeadersList;
        this.sess = session_title;
        this.func_title = func_title;
        this.date = date;
        this.orderHeaderMap = orderHeaderMap;
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
        final SelectedHeader list = header.get(position);


        //set header title
        getViewModel.setHeader_title(list.getHeader());


        holder.header.setText(list.getHeader());
        String[] str = sess.split("_");
        session_title = str[0];
        bolen=str[1];
        MyLog.e(TAG,"bolen>>"+bolen);
        if (bolen.equals("true")) {
            holder.header_layout.setBackgroundColor(context.getResources().getColor(R.color.btn_gradient_light));
        } else if (bolen.equals("false")) {
            holder.header_layout.setBackgroundColor(context.getResources().getColor(R.color.text_silver));
        }


        //o_orderDishListsses = orderHeaderMap.get(list.getHeader());
        orderItemMap=orderHeaderMap.get(list.getHeader());
        Set<String> stringSet = orderItemMap.keySet();
        List<String> aList = new ArrayList<String>(stringSet.size());
        for (String x : stringSet)
            aList.add(x);
        selectedItemLists =new ArrayList<>();
        for(int i=0;i<aList.size();i++)
        {
            String [] arr=(aList.get(i)).split("_");
            SelectedItemList itemList=new SelectedItemList();
            itemList.setItem(arr[0]);
            itemList.setSelected(arr[1]);
            selectedItemLists.add(itemList);
        }

        holder.recyclerview_item_list.setHasFixedSize(true);
        holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        MyLog.e(TAG, "s_count>>sess>>" + sess);
        viewCartAdapterItem = new ViewCartAdapterItem(context, getViewModel, selectedItemLists, orderItemMap,func_title, list.getHeader(), sess,date);
        holder.recyclerview_item_list.setAdapter(viewCartAdapterItem);





    }


    @Override
    public int getItemCount() {
        return header.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView header;
        private RecyclerView recyclerview_item_list;
        private LinearLayout header_layout;


        public ViewHolder(View view) {
            super(view);
            recyclerview_item_list = view.findViewById(R.id.recyclerview_item_list);
            header = view.findViewById(R.id.header);
            header_layout = view.findViewById(R.id.header_layout);

        }
    }
}


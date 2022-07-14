package com.example.kcs.Fragment.PlaceOrders.Header;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Items.CheckedList;

import com.example.kcs.Fragment.Items.ItemList;
import com.example.kcs.Fragment.PlaceOrders.Items.PlaceOrderViewCartAdapterItem;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.ViewModel.SelectedDishList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class PlaceOrderViewCartAdapterHeader extends RecyclerView.Adapter<PlaceOrderViewCartAdapterHeader.ViewHolder> {
    private Context context;
    private PlaceOrderViewCartAdapterItem placeOrderViewCartAdapterItem;
    private String TAG = "PlaceOrderViewCartAdapterHeader";
    private List<ItemList> itemLists=new ArrayList<>();
    private GetViewModel getViewModel;
    private List<SelectedHeader> header = new ArrayList<>();
    //item map
    private LinkedHashMap<String, List<CheckedList>> itemMap = new LinkedHashMap<>();
    //header map
    private LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> headerMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> sessionMap = new LinkedHashMap<>();
    //fun map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>> funcMap = new LinkedHashMap<>();
    //edit hash map
    //edit hash map list
    private List<SessionList> e_sessionLists = new ArrayList<>();
    private List<SelectedHeader> e_selectedHeaders = new ArrayList<>();
    //Edit HashMap
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>> editFunc_Map = new LinkedHashMap<>();
    //Date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>> editDateMap = new LinkedHashMap<>();
    //Session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>> editSessionMap = new LinkedHashMap<>();
    //Header map
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>> editHeaderMap = new LinkedHashMap<>();
    //Item map
    private LinkedHashMap<String, List<SelectedDishList>> editItemMap = new LinkedHashMap<>();

    public PlaceOrderViewCartAdapterHeader(Context context, GetViewModel getViewModel, List<SelectedHeader> selectedHeadersList, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> headerMap, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>> editHeaderMap) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.header = selectedHeadersList;
        this.headerMap = headerMap;
        this.editHeaderMap = editHeaderMap;
    }


    @NonNull
    @Override
    public PlaceOrderViewCartAdapterHeader.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.place_view_header, parent, false);
        return new PlaceOrderViewCartAdapterHeader.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOrderViewCartAdapterHeader.ViewHolder holder, int position) {
        holder.recyclerview_item_list.setHasFixedSize(true);
        holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        if (editHeaderMap == null) {
            MyLog.e(TAG, "dish>>editHeaderMap is null");

            final SelectedHeader list = header.get(position);
            holder.header.setText(list.getHeader());
            //get Checked list hash map

            itemMap = headerMap.get(list.getHeader());
            Set<String> stringSet = itemMap.keySet();
            List<String> aList = new ArrayList<String>(stringSet.size());
            for (String x : stringSet)
                aList.add(x);
            itemLists=new ArrayList<>();
            for(int i=0;i<aList.size();i++)
            {
                ItemList itemList=new ItemList();
                String[] str=(aList.get(i)).split("_");
                itemList.setItem(str[0]);
                itemList.setSelected(str[1]);
                itemLists.add(itemList);
            }



            PlaceOrderViewCartAdapterItem placeOrderViewCartAdapterItem = new PlaceOrderViewCartAdapterItem(context, getViewModel, itemMap,itemLists,null);
            //placeOrderViewCartAdapterItem = new PlaceOrderViewCartAdapterItem(context, getViewModel, itemMap,null);
            holder.recyclerview_item_list.setAdapter(placeOrderViewCartAdapterItem);
        } else {
            MyLog.e(TAG, "dish>>editHeaderMap is not  null");


            final SelectedHeader list = header.get(position);
            holder.header.setText(list.getHeader());
            //get Checked list hash map

            editItemMap = editHeaderMap.get(list.getHeader());
            Set<String> stringSet = editItemMap.keySet();
            List<String> aList = new ArrayList<String>(stringSet.size());
            for (String x : stringSet)
                aList.add(x);
            itemLists=new ArrayList<>();
            for(int i=0;i<aList.size();i++)
            {
                ItemList itemList=new ItemList();
                String[] str=(aList.get(i)).split("_");
                itemList.setItem(str[0]);
                itemList.setSelected(str[1]);
                itemLists.add(itemList);
            }


            holder.recyclerview_item_list.setHasFixedSize(true);
            holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            //placeOrderViewCartAdapterItem = new PlaceOrderViewCartAdapterItem(context, getViewModel, null,null);
            PlaceOrderViewCartAdapterItem placeOrderViewCartAdapterItem = new PlaceOrderViewCartAdapterItem(context, getViewModel, null,itemLists,editItemMap);
            //placeOrderViewCartAdapterItem = new PlaceOrderViewCartAdapterItem(context, getViewModel, null,e_selectedHeaders);
            holder.recyclerview_item_list.setAdapter(placeOrderViewCartAdapterItem);
        }
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


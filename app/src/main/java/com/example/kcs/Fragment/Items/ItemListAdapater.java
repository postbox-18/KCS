package com.example.kcs.Fragment.Items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;

import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemListAdapater extends RecyclerView.Adapter<ItemListAdapater.ViewHolder> {
    private Context context;
    private List<ItemList> itemLists;
    private List<CheckedList> checkedLists = new ArrayList<>();
    private List<CheckedList> selected_checkedLists = new ArrayList<>();
    private String TAG = "ItemListAdapater";
    //private MyViewModel myViewModel;
    private GetViewModel getViewModel;
    //private LinkedHashMap<String, List<CheckedList>> f_map = new LinkedHashMap<>();
    //private List<LinkedHashMap<String,List<CheckedList>>> s_map=new ArrayList<>();
    private List<LinkedHashMap<String, List<CheckedList>>> selected_s_map = new ArrayList<>();
    private  LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap=new LinkedHashMap<>();
    private String header;
    ItemListAdapater.Unchecked unchecked;

    public interface Unchecked {
        void getUnchecked(String item);
    }

    public ItemListAdapater(Context context, List<ItemList> itemLists, String header, GetViewModel getViewModel, List<LinkedHashMap<String, List<CheckedList>>> linkedHashMaps, LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap) {
        this.context = context;
        this.itemLists = itemLists;
        this.getViewModel = getViewModel;
        this.header = header;
        this.selected_s_map = linkedHashMaps;
        this.stringListLinkedHashMap = stringListLinkedHashMap;
        //cartViewModel = ViewModelProviders.of((FragmentActivity) context).get(CartViewModel.class);
        //myViewModel= new ViewModelProvider((FragmentActivity)context).get(MyViewModel.class);

    }

    @NonNull
    @Override
    public ItemListAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.item_checkbox_list, parent, false);
        return new ItemListAdapater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapater.ViewHolder holder, int position) {

        final ItemList itemList1 = itemLists.get(position);

        //img update soon
        //holder.header_img.setText(funList1.getImg());


        //check if checked list item selected
        if (selected_s_map.size() > 0) {
            for (int k = 0; k < selected_s_map.size(); k++) {
                selected_checkedLists = selected_s_map.get(k).get(header);
                if (selected_checkedLists != null) {
                    for (int i = 0; i < selected_checkedLists.size(); i++) {
                        final CheckedList selected_checkedLists1 = selected_checkedLists.get(i);
                   /* MyLog.e(TAG, "checked>>" + selected_checkedLists1.getPosition());
                    MyLog.e(TAG, "checked>>" + position);*/
                        if (selected_checkedLists1.getPosition() == position) {
                            //MyLog.e(TAG, "checked>>" + selected_checkedLists1.getItemList());
                            holder.item_check.setChecked(true);

                        }
                    }
                } else {
                    MyLog.e(TAG, "checked>> selected size is null>>");
                }
            }
        } else {
            MyLog.e(TAG, "checked>>selected  map size is null>>");
        }
        MyLog.e(TAG, "hashmap>>size>>" + selected_s_map.size());

        holder.item_check.setText(itemList1.getItem());
        holder.item_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (itemList1.getItem() != null) {
                    if (holder.item_check.isChecked()) {
                        CheckedList checkedLists1 = new CheckedList(
                                itemList1.getItem(),
                                position
                        );
                        checkedLists.add(checkedLists1);

                        stringListLinkedHashMap.put(header, checkedLists);

                        getViewModel.setCheckedLists(checkedLists);
                        //notifyDataSetChanged();
                    } else {
                        //unchecked.getUnchecked(itemList1.getItem());
                        GetUncheckList(itemList1.getItem());

                    }
                    getViewModel.setF_map(stringListLinkedHashMap);

                    MyLog.e(TAG, "selected_s_map>>size>>" + selected_s_map.size());
                    selected_s_map.add(stringListLinkedHashMap);
                    getViewModel.setCheck_s_map(selected_s_map);

                    Gson gson = new Gson();
                    String json = gson.toJson(checkedLists);
                    new SharedPreferences_data(context).setChecked_item_list(json);


                }
            }
        });


    }

    private void GetUncheckList(String item) {
        for (int i = 0; i < checkedLists.size(); i++) {
            if (item.equals(checkedLists.get(i).getItemList())) {
                checkedLists.remove(i);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox item_check;
        private CardView item_check_card;

        public ViewHolder(View view) {
            super(view);
            item_check = view.findViewById(R.id.item_check);
            item_check_card = view.findViewById(R.id.item_check_card);

        }
    }
}

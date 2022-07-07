package com.example.kcs.Fragment.PlaceOrders.Header;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Fragment.Items.CheckedList;

import com.example.kcs.Fragment.PlaceOrders.ViewCartAdapter;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PlaceOrderViewCartAdapterHeader extends RecyclerView.Adapter<PlaceOrderViewCartAdapterHeader.ViewHolder> {
    private Context context;
    private List<CheckedList> checkedLists = new ArrayList<>();
    private ViewCartAdapter viewCartAdapter;
    private String TAG = "ViewCartAdapterHeader";

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
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editFunc_Map = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> editSessionMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<SelectedHeader>> editHeaderMap = new LinkedHashMap<>();

    public PlaceOrderViewCartAdapterHeader(Context context, GetViewModel getViewModel, List<SelectedHeader> selectedHeadersList, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> headerMap, LinkedHashMap<String, List<SelectedHeader>> editHeaderMap) {
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

        View view = layoutInflater.inflate(R.layout.view_cart_card_view_header, parent, false);
        return new PlaceOrderViewCartAdapterHeader.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOrderViewCartAdapterHeader.ViewHolder holder, int position) {
        if (editHeaderMap == null) {
            final SelectedHeader list = header.get(position);
            holder.header.setText(list.getHeader());
            //get Checked list hash map

            //checkedLists = headerMap.get(list.getHeader());
            holder.recyclerview_item_list.setHasFixedSize(true);
            holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            viewCartAdapter = new ViewCartAdapter(context, getViewModel, checkedLists,null);
            holder.recyclerview_item_list.setAdapter(viewCartAdapter);
        } else {
            final SelectedHeader list = header.get(position);
            holder.header.setText(list.getHeader());
            //get Checked list hash map

            e_selectedHeaders = editHeaderMap.get(list.getHeader());
            holder.recyclerview_item_list.setHasFixedSize(true);
            holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            viewCartAdapter = new ViewCartAdapter(context, getViewModel, null,e_selectedHeaders);
            holder.recyclerview_item_list.setAdapter(viewCartAdapter);
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


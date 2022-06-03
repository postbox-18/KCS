package com.example.kcs.Fragment.PlaceOrders;

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
import com.example.kcs.Fragment.Items.CheckedList;

import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ViewCartAdapterHeader extends RecyclerView.Adapter<ViewCartAdapterHeader.ViewHolder> {
    private Context context;
    private List<CheckedList> checkedLists = new ArrayList<>();
    private ViewCartAdapter viewCartAdapter;
    private String TAG = "ViewCartAdapterHeader";

    private GetViewModel getViewModel;
    private List<SelectedHeader> header=new ArrayList<>();


    public ViewCartAdapterHeader(Context context, GetViewModel getViewModel, List<SelectedHeader> selectedHeadersList) {
        this.context=context;
        this.getViewModel=getViewModel;
        this.header=selectedHeadersList;
    }



    @NonNull
    @Override
    public ViewCartAdapterHeader.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.view_cart_card_view_header, parent, false);
        return new ViewCartAdapterHeader.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCartAdapterHeader.ViewHolder holder, int position) {
        final SelectedHeader list=header.get(position);
        holder.header.setText(list.getHeader());
        //get Checked list hash map
        getViewModel.getF_mapMutable().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<CheckedList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap) {
                    checkedLists=stringListLinkedHashMap.get(list.getHeader());
                    holder.recyclerview_item_list.setHasFixedSize(true);
                    holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    viewCartAdapter=new ViewCartAdapter(context,getViewModel,checkedLists);
                    holder.recyclerview_item_list.setAdapter(viewCartAdapter);




            }
        });


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


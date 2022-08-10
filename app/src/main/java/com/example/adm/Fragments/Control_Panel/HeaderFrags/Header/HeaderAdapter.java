package com.example.adm.Fragments.Control_Panel.HeaderFrags.Header;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;

import com.example.adm.Fragments.Control_Panel.HeaderFrags.Dish.DishList;
import com.example.adm.Fragments.Control_Panel.HeaderFrags.Item.ItemArrayList;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
    private List<HeaderList> headerLists;
    private Context context;
    private String TAG = "HeaderAdapter";
    private GetViewModel getViewModel;
    private int n = 1;
    //header map
    private LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> itemArrayMap = new LinkedHashMap<>();
    //dish map
    private LinkedHashMap<String, List<DishList>> dishListMap = new LinkedHashMap<>();
    private List<ItemArrayList> itemList = new ArrayList<>();

    public HeaderAdapter(Context context, List<HeaderList> headerLists, GetViewModel getViewModel, LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> itemArrayListMap) {
        this.headerLists = headerLists;
        this.context = context;
        this.itemArrayMap = itemArrayListMap;
        this.getViewModel = getViewModel;
    }

    @NonNull
    @Override
    public HeaderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.header_cardview, parent, false);
        return new HeaderAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderAdapter.ViewHolder holder, int position) {
        final HeaderList item1 = headerLists.get(position);
        holder.header_title.setText(item1.getHeader());
        /*holder.recyclerview_item_list.setHasFixedSize(true);
        holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));*/


        //onclick
        holder.header_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //holder.header_title_set.setText(item1.getHeader());
//                holder.item_cardView.setVisibility(View.VISIBLE);

                dishListMap = itemArrayMap.get(item1.getHeader());
                Set<String> stringSet = dishListMap.keySet();
                List<String> aList = new ArrayList<String>(stringSet.size());
                for (String x : stringSet)
                    aList.add(x);
                itemList = new ArrayList<>();
                for (int i = 0; i < aList.size(); i++) {
                    String[] str = (aList.get(i)).split("_");
                    ItemArrayList list = new ItemArrayList();
                    list.setItem(str[0]);
                    list.setSelected(str[1]);
                    itemList.add(list);
                }

                /*ItemAdapter itemAdapter = new ItemAdapter(context, getViewModel, itemList, dishListMap);
                holder.recyclerview_item_list.setAdapter(itemAdapter);*/
                getViewModel.setItemArrayList(itemList);
                getViewModel.setDishListMap(dishListMap);
                getViewModel.setHeader_title(item1.getHeader());
                getViewModel.setI_value(3);
                /*//get item list
                //itemList=itemArrayMap.get(item1.getHeader());
                getViewModel.setHeader_title(item1.getHeader());
                getViewModel.setItemArrayList(itemList);
                getViewModel.setI_value(3);*/

            }
        });
    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "item>>49>>" + headerLists.size());
        return headerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView header_title;
        private CardView header_cardView;

        public ViewHolder(View view) {
            super(view);
            header_title = view.findViewById(R.id.header_title);
            //header_title_set = view.findViewById(R.id.header_title_set);
            header_cardView = view.findViewById(R.id.header_cardView);
            //recyclerview_item_list = view.findViewById(R.id.recyclerview_item_list);
            //item_cardView = view.findViewById(R.id.item_cardView);
        }
    }
}



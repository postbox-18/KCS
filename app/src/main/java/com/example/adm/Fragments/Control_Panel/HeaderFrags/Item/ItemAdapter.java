package com.example.adm.Fragments.Control_Panel.HeaderFrags.Item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Control_Panel.HeaderFrags.Dish.DishList;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderHeaderLists;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<ItemArrayList> itemArrayLists = new ArrayList<>();
    private List<OrderHeaderLists> orderHeaderLists = new ArrayList<>();
    private Context context;
    private String TAG = "ItemAdapter", header_title;
    private GetViewModel getViewModel;
    //header map
    private LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> itemArrayMap = new LinkedHashMap<>();
    //dish map
    private LinkedHashMap<String, List<DishList>> dishListMap = new LinkedHashMap<>();
    private List<DishList> dishLists = new ArrayList<>();
    private int n = 1;


    public ItemAdapter(Context context, GetViewModel getViewModel, List<ItemArrayList> itemList, LinkedHashMap<String, List<DishList>> dishListMap, String header_title) {
        this.itemArrayLists = itemList;
        this.context = context;
        this.getViewModel = getViewModel;
        this.dishListMap = dishListMap;
        this.header_title = header_title;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_cardview, parent, false);
        return new ItemAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ItemArrayList item1 = itemArrayLists.get(position);
        holder.item_title.setText(item1.getItem());


        if ((item1.getSelected()).equals("true")) {
            holder.switchView.setChecked(true);
            //holder.item_cardView.setCardBackgroundColor(context.getResources().getColor(R.color.light_blue_color));
            holder.item_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
        } else {
            holder.switchView.setChecked(false);
            // holder.item_cardView.setCardBackgroundColor(context.getResources().getColor(R.color.lightGray));
            holder.item_title.setTextColor(context.getResources().getColor(R.color.light_gray));
        }

        /*holder.recyclerview_dish_list.setHasFixedSize(true);
        holder.recyclerview_dish_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));*/
        //onclick
        holder.item_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //holder.item_title_set.setText(item1.getItem());
                    String item = item1.getItem() + "_" + item1.getSelected();
                    dishLists = dishListMap.get(item);

                 /*   DishAdapter dishAdapter = new DishAdapter(context, dishLists, getViewModel, header_title, item);
                    holder.recyclerview_dish_list.setAdapter(dishAdapter);*/

                    getViewModel.setDishLists(dishLists);
                    getViewModel.setHeader_title(header_title);
                    getViewModel.setItem_title(item);
                    getViewModel.setI_value(4);



            }
        });


        //onclick
        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = item1.getItem() + "_" + item1.getSelected();
                dishLists = dishListMap.get(item);
                getViewModel.DeleteItem(header_title,item,null);
                //getViewModel.updateItem(header_title, item, null, null,dishLists);
            }
        });

        //onclick
        holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {
                    MyLog.e(TAG, "switch>>get enabled" + b);
                    holder.item_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
                } else {
                    MyLog.e(TAG, "switch>>get not enabled" + b);
                    holder.item_title.setTextColor(context.getResources().getColor(R.color.light_gray));

                }
                String item = item1.getItem() + "_" + item1.getSelected();
                dishLists = dishListMap.get(item);
                //selectedHeaderMap.get(header_title).get(position).setSelected(String.valueOf(b));
                getViewModel.updateItem(header_title, item, null, String.valueOf(b),dishLists);


            }
        });
    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "item>>49>>" + itemArrayLists.size());
        return itemArrayLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_title;
        private Switch switchView;
        private CardView item_cardView;
        private ImageView trash;

        public ViewHolder(View view) {
            super(view);
            item_title = view.findViewById(R.id.item_title);
            item_cardView = view.findViewById(R.id.item_cardView);
            trash = view.findViewById(R.id.trashImg);
            //item_title_set = view.findViewById(R.id.item_title_set);
            //recyclerview_dish_list = view.findViewById(R.id.recyclerview_dish_list);
            //dish_cardView = view.findViewById(R.id.dish_cardView);
            switchView = view.findViewById(R.id.switchView);

        }
    }
}


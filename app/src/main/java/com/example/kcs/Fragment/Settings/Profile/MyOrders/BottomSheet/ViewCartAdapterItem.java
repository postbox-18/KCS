package com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kcs.Classes.MyLog;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ViewCartAdapterItem extends RecyclerView.Adapter<ViewCartAdapterItem.ViewHolder> {
    private Context context;
    private List<OrderDishLists> orderDishListsses = new ArrayList<>();
    private String TAG = "ViewCartAdapterItem";
    private GetViewModel getViewModel;
    private String func_title, header, username, date, sess;
    private int n;
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
    private List<SelectedItemList> selectedItemLists = new ArrayList<>();
    private String session_title,bolen;


    public ViewCartAdapterItem(Context context, GetViewModel getViewModel, List<SelectedItemList> selectedItemLists, LinkedHashMap<String, List<OrderDishLists>> orderItemMap, String func_title, String header, String sess, String date) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.selectedItemLists = selectedItemLists;
        this.orderItemMap = orderItemMap;
        this.func_title = func_title;
        this.header = header;
        this.sess = sess;
        this.date = date;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.bottom_sheet_order_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SelectedItemList selectedItemLists1 = selectedItemLists.get(position);
        String item= selectedItemLists1.getItem()+"_"+ selectedItemLists1.getSelected();
        holder.item.setText(selectedItemLists1.getItem());

        //set item title
        getViewModel.setItem_title(item);
        String[] str = sess.split("_");
        session_title = str[0];
        bolen=str[1];
        MyLog.e(TAG,"bolen>>"+bolen);
        if (bolen.equals("true")) {
            holder.item_layout.setBackgroundColor(context.getResources().getColor(R.color.btn_gradient_light));
            if(selectedItemLists1.getSelected().equals("true"))
        {
            holder.item_layout.setBackgroundColor(context.getResources().getColor(R.color.btn_gradient_light));
        }
        else if(selectedItemLists1.getSelected().equals("false"))
        {
            holder.item_layout.setBackgroundColor(context.getResources().getColor(R.color.text_silver));

        }
        } else if (bolen.equals("false")) {
            holder.item_layout.setBackgroundColor(context.getResources().getColor(R.color.text_silver));
        }


        orderDishListsses=orderItemMap.get(item);
        holder.recyclerview_dish_list.setHasFixedSize(true);
        holder.recyclerview_dish_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ViewCartAdapterDish viewCartAdapterDish = new ViewCartAdapterDish(context, getViewModel, orderDishListsses,func_title,date,sess,header,item);
        holder.recyclerview_dish_list.setAdapter(viewCartAdapterDish);


    }


    @Override
    public int getItemCount() {
        return selectedItemLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item;
        private LinearLayout item_layout;
        private RecyclerView recyclerview_dish_list;

        public ViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.item);
            recyclerview_dish_list = view.findViewById(R.id.recyclerview_dish_list);
            item_layout = view.findViewById(R.id.item_layout);

        }
    }
}


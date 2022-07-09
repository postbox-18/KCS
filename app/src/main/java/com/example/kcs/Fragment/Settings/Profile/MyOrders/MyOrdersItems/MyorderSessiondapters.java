package com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrdersItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.OrderDishLists;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.ViewModel.SelectedDishList;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class MyorderSessiondapters extends RecyclerView.Adapter<MyorderSessiondapters.ViewHolder> {
    private Context context;
    private String TAG = "MyorderSessiondapters";
    private String func_title,date;
    private List<MyOrdersList> myOrdersList=new ArrayList<>();
    private List<SelectedSessionList> sessionLists = new ArrayList<>();
    private GetViewModel getViewModel;
    //edit hash map list
    private List<SelectedSessionList> e_sessionLists=new ArrayList<>();
    private List<SelectedHeader> e_selectedHeaders=new ArrayList<>();

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
    //get selected headers
    private List<SelectedHeader> o_selectedHeaders=new ArrayList<>();
    private List<OrderDishLists> o_orderDishLists =new ArrayList<>();


    public MyorderSessiondapters(Context context, String funcTitle, String date, GetViewModel getViewModel, List<SelectedSessionList> sessionLists, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>> orderSessionMap) {
        this.func_title = funcTitle;
        this.context = context;
        this.sessionLists = sessionLists;
        this.date = date;
        this.getViewModel = getViewModel;
        this.orderSessionMap = orderSessionMap;
    }

    @NonNull
    @Override
    public MyorderSessiondapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.select_session, parent, false);
        return new MyorderSessiondapters.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull MyorderSessiondapters.ViewHolder holder, int position) {
        final SelectedSessionList sessionLists1 = sessionLists.get(position);

        if((sessionLists1.getBolen()).equals("true")) {
            //set session title and date
            holder.session_title.setText(sessionLists1.getSession_title());
            holder.session_title.setTextColor(context.getResources().getColor(R.color.btn_gradient_light));
            holder.time.setText(sessionLists1.getTime());
            holder.time.setTextColor(context.getResources().getColor(R.color.colorSecondary));
            holder.count.setText(sessionLists1.getS_count());
            holder.count.setTextColor(context.getResources().getColor(R.color.btn_gradient_light));

        }
        else if((sessionLists1.getBolen()).equals("false"))
        {
            //set session title and date
            holder.session_title.setText(sessionLists1.getSession_title());
            holder.session_title.setTextColor(context.getResources().getColor(R.color.text_silver));
            holder.time.setText(sessionLists1.getTime());
            holder.time.setTextColor(context.getResources().getColor(R.color.text_silver));
            holder.count.setText(sessionLists1.getS_count());
            holder.count.setTextColor(context.getResources().getColor(R.color.text_silver));
        }

        String s=sessionLists1.getSession_title()+"!"+sessionLists1.getTime()+"-"+sessionLists1.getS_count()+"_"+sessionLists1.getBolen();
        if(orderSessionMap==null)
        {
            orderSessionMap=new LinkedHashMap<>();
            MyLog.e(TAG,"orderSessionMap is null");
        }
        else {

            if (orderHeaderMap == null) {
                orderHeaderMap = new LinkedHashMap<>();
                MyLog.e(TAG, "orderHeaderMap is null");
            } else {
                orderHeaderMap = new LinkedHashMap<>(orderSessionMap.get(s));
                Set<String> set = orderHeaderMap.keySet();
                List<String> aList1 = new ArrayList<String>(set.size());
                for (String x1 : set)
                    aList1.add(x1);
                o_selectedHeaders.clear();
                for (int i = 0; i < aList1.size(); i++) {
                    SelectedHeader header = new SelectedHeader(
                            aList1.get(i)
                    );

                    o_selectedHeaders.add(header);
                    //get header list and item size
                }

                for (int k = 0; k < o_selectedHeaders.size(); k++) {

                    String header = o_selectedHeaders.get(k).getHeader();
                    orderItemMap = orderHeaderMap.get(header);
                    Set<String> stringSet = orderItemMap.keySet();
                    List<String> aList = new ArrayList<String>(stringSet.size());
                    for (String x : stringSet)
                        aList.add(x);
                    o_orderDishLists=new ArrayList<>();
                    for(int l=0;l<aList.size();l++)
                    {
                        o_orderDishLists=orderItemMap.get(aList.get(l));
                        myOrdersList=new ArrayList<>();
                        MyOrdersList myOrdersList1 = new MyOrdersList(
                                header,
                                o_orderDishLists.size()
                        );
                        myOrdersList.add(myOrdersList1);
                        holder.recyclerview_item_list.setHasFixedSize(true);
                        holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        MyorderItemListAdapters itemListAdapters = new MyorderItemListAdapters(context, getViewModel, myOrdersList);
                        holder.recyclerview_item_list.setAdapter(itemListAdapters);
                    }



                }


            }
        }






    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "sessionLists>>49>>" + sessionLists.size());
        return sessionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView session_title, time,count;
        //private CardView session_card;
        private RecyclerView recyclerview_item_list;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            recyclerview_item_list = view.findViewById(R.id.recyclerview_item_list);
            session_title = view.findViewById(R.id.session_title);
            time = view.findViewById(R.id.time);
            count = view.findViewById(R.id.count);
            //session_card = view.findViewById(R.id.session_card);


        }
    }
}


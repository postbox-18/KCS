package com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrdersItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.OrderItemLists;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
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
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> editFunc_Map = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editDateMap = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> editSessionMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<SelectedHeader>> editHeaderMap = new LinkedHashMap<>();
    //order hashmap
    //header map
    private LinkedHashMap<String, List<OrderItemLists>> orderHeaderMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap = new LinkedHashMap<>();
    //get selected headers
    private List<SelectedHeader> o_selectedHeaders=new ArrayList<>();
    private List<OrderItemLists> o_orderItemLists=new ArrayList<>();


    public MyorderSessiondapters(Context context, String funcTitle, String date, GetViewModel getViewModel, List<SelectedSessionList> sessionLists, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap) {
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
            holder.session_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));

        }
        else if((sessionLists1.getBolen()).equals("false"))
        {
            //set session title and date
            holder.session_title.setText(sessionLists1.getSession_title());
            holder.session_title.setTextColor(context.getResources().getColor(R.color.text_silver));
            holder.time.setText(sessionLists1.getTime());
            holder.time.setTextColor(context.getResources().getColor(R.color.text_silver));
        }

        String s=sessionLists1.getSession_title()+"!"+sessionLists1.getTime()+"_"+sessionLists1.getBolen();
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

                myOrdersList.clear();
                for (int k = 0; k < o_selectedHeaders.size(); k++) {

                    o_orderItemLists.clear();
                    String header = o_selectedHeaders.get(k).getHeader();
                    o_orderItemLists = new ArrayList<>(orderHeaderMap.get(header));

                    MyOrdersList myOrdersList1 = new MyOrdersList(
                            header,
                            o_orderItemLists.size()
                    );
                    myOrdersList.add(myOrdersList1);


                }
                holder.recyclerview_item_list.setHasFixedSize(true);
                holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                MyorderItemListAdapters itemListAdapters = new MyorderItemListAdapters(context, getViewModel, myOrdersList);
                holder.recyclerview_item_list.setAdapter(itemListAdapters);

            }
        }
        ///////////***************************clear list in live data model****************************//////////////////////

        //get func map
        getViewModel.getEditFuncMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> stringLinkedHashMapLinkedHashMap) {
                editFunc_Map=stringLinkedHashMapLinkedHashMap;
            }
        });


        //get session map
        getViewModel.getEditSessionMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> stringLinkedHashMapLinkedHashMap) {
                editSessionMap = stringLinkedHashMapLinkedHashMap;
            }
        });


        //get header map
        getViewModel.getEditHeaderMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SelectedHeader>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SelectedHeader>> stringListLinkedHashMap) {
                editHeaderMap=stringListLinkedHashMap;
            }
        });

        ///////////***************************clear list in live data model****************************//////////////////////

       /* //on click
        holder.session_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFunc_Map=new LinkedHashMap<>();
                getViewModel.setEditFuncMap(editFunc_Map);
                editSessionMap=new LinkedHashMap<>();
                getViewModel.setEditSessionMap(editSessionMap);
                editHeaderMap=new LinkedHashMap<>();
                getViewModel.setEditHeaderMap(editHeaderMap);
                getViewModel.setFunc_title(func_title);
                String s = func_title + "/" + sessionLists1.getSession_title()+"!"+date+"#"+sessionLists1.getTime()+"_"+sessionLists1.getBolen();
                getViewModel.setFunc_Session(s);
            }
        });
*/

    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "sessionLists>>49>>" + sessionLists.size());
        return sessionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView session_title, time;
        //private CardView session_card;
        private RecyclerView recyclerview_item_list;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            recyclerview_item_list = view.findViewById(R.id.recyclerview_item_list);
            session_title = view.findViewById(R.id.session_title);
            time = view.findViewById(R.id.time);
            //session_card = view.findViewById(R.id.session_card);


        }
    }
}


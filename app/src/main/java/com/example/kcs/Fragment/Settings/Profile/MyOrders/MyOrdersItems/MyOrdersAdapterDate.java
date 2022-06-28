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
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.OrderItemLists;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrderFuncList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class MyOrdersAdapterDate extends RecyclerView.Adapter<MyOrdersAdapterDate.ViewHolder> {
    private Context context;
    private String TAG="MyOrdersAdapterDate",funcTitle;
    //private List<MyOrderFuncList> myOrderFuncLists=new ArrayList<>();
    private List<SelectedSessionList> sessionLists=new ArrayList<>();
    private List<MyOrdersList> myOrdersList;
    private GetViewModel getViewModel;
    //edit hash map list
    private List<SelectedSessionList> e_sessionLists=new ArrayList<>();
    private List<SelectedHeader> e_selectedHeaders=new ArrayList<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> editFunc_Map = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editDateMap = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> editSessionMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<SelectedHeader>> editHeaderMap = new LinkedHashMap<>();
    //order hashmap

    //date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> orderDateMap = new LinkedHashMap<>();
    //header map
    private LinkedHashMap<String, List<OrderItemLists>> orderHeaderMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap = new LinkedHashMap<>();
    //Date List
    private List<SelectedDateList> dateLists=new ArrayList<>();

    public MyOrdersAdapterDate(Context context, GetViewModel getViewModel, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> orderDateMap, List<SelectedDateList> dateLists, String funcTitle) {

        this.context = context;
        this.getViewModel = getViewModel;
        this.orderDateMap = new LinkedHashMap<>(orderDateMap);
        this.dateLists = dateLists;
        this.funcTitle = funcTitle;
    }

    @NonNull
    @Override
    public MyOrdersAdapterDate.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.select_date, parent, false);
        return new MyOrdersAdapterDate.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersAdapterDate.ViewHolder holder, int position) {
        final SelectedDateList list = dateLists.get(position);
        String username=new SharedPreferences_data(context).getS_user_name();
        //get data func,header,list item size from hash map
        holder.date.setText(list.getDate());
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

        //get selected sessions
        orderSessionMap=orderDateMap.get(list.getDate());
        if(orderSessionMap==null)
        {
            MyLog.e(TAG,"orders>>selected orderSessionMap is null");
            orderSessionMap=new LinkedHashMap<>();
        }
        else {
            //get session title
            Set<String> set = orderSessionMap.keySet();
            List<String> aList1 = new ArrayList<String>(set.size());
            for (String x1 : set)
                aList1.add(x1);
            sessionLists.clear();
            for (int i = 0; i < aList1.size(); i++) {
                String[] scb = (aList1.get(i)).split("-");
                String[] cb = (scb[1]).split("_");
                String bolen = cb[1];
                String count = cb[0];
                String[] str = (scb[0]).split("!");
                String sess = str[0];
                String dateTime = str[1];
                SelectedSessionList sessionList = new SelectedSessionList();
                sessionList.setSession_title(sess);
                sessionList.setTime(dateTime);
                sessionList.setS_count(count);
                sessionList.setBolen(bolen);
                sessionLists.add(sessionList);

            }

            holder.recyclerview_session.setHasFixedSize(true);
            holder.recyclerview_session.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            MyorderSessiondapters itemListAdapters = new MyorderSessiondapters(context, funcTitle, list.getDate(), getViewModel, sessionLists, orderSessionMap);
            holder.recyclerview_session.setAdapter(itemListAdapters);
            //on click
            holder.date_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editFunc_Map=new LinkedHashMap<>();
                    getViewModel.setEditFuncMap(editFunc_Map);
                    editSessionMap=new LinkedHashMap<>();
                    getViewModel.setEditSessionMap(editSessionMap);
                    editHeaderMap=new LinkedHashMap<>();
                    getViewModel.setEditHeaderMap(editHeaderMap);
                    getViewModel.setFunc_title(funcTitle);
                    String s = funcTitle + "/" + list.getDate();
                    getViewModel.setFunc_Session(s);
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "myOrderFuncLists>>49>>" + dateLists.size());
        return dateLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private RecyclerView recyclerview_session;
        private CardView date_card;

        public ViewHolder(View view) {
            super(view);
            recyclerview_session = view.findViewById(R.id.recyclerview_session);
            date = view.findViewById(R.id.date);
            date_card = view.findViewById(R.id.date_card);



        }
    }
}
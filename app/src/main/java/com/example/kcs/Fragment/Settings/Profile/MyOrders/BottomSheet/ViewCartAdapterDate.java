package com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet;

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
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Header.SessionDateTime;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrdersItems.SelectedDateList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ViewCartAdapterDate extends RecyclerView.Adapter<ViewCartAdapterDate.ViewHolder> {
    private Context context;
    private List<OrderDishLists> orderDishListsses = new ArrayList<>();
    private ViewCartAdapter viewCartAdapter;
    private String TAG = "ViewCartAdapterDate";
    private String func_title, s_user_name, sess_title;

    private GetViewModel getViewModel;
    private List<SelectedHeader> selectedHeaders = new ArrayList<>();
    private List<SelectedSessionList> e_sessionLists=new ArrayList<>();
    private BottomSheetDialog bottomSheet;
    //edit hash map list
    private List<SelectedHeader> e_selectedHeaders=new ArrayList<>();
    private LinkedHashMap<String, List<SelectedHeader>> editHeaderMap = new LinkedHashMap<>();
    private List<SessionDateTime> sessionDateTimes=new ArrayList<>();
    //cancel map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> editFunc_Map = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editDateMap = new LinkedHashMap<>();
    private int n;
    //order hash map
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>> orderFunc_Map = new LinkedHashMap<>();
    //Date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>> orderDateMap = new LinkedHashMap<>();
    //Session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>> orderSessionMap = new LinkedHashMap<>();
    //Header map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>> orderHeaderMap = new LinkedHashMap<>();
    //Item map
    private LinkedHashMap<String, List<OrderDishLists>> orderItemMap = new LinkedHashMap<>();
    //selected headers
    private List<SelectedHeader> o_selectedHeaders=new ArrayList<>();
    private List<SelectedSessionList> o_selectedSessionLists=new ArrayList<>();
    private List<SelectedDateList> o_dateLists=new ArrayList<>();


    public ViewCartAdapterDate(Context context, GetViewModel getViewModel, String func_title, BottomSheetDialog bottomSheet, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>> orderDateMap, List<SelectedDateList> o_dateLists) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.func_title = func_title;
        this.bottomSheet = bottomSheet;
        this.orderDateMap = new LinkedHashMap<>(orderDateMap);
        this.o_dateLists = o_dateLists;
    }


    @NonNull
    @Override
    public ViewCartAdapterDate.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.bottom_sheet_order_date, parent, false);
        return new ViewCartAdapterDate.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCartAdapterDate.ViewHolder holder, int position) {

        //get user name shared prefernces
        s_user_name = new SharedPreferences_data(context).getS_user_name();
        //clear
///////////***************************clear list in live data model****************************//////////////////////
        //get Edit Delete Cancel value
        getViewModel.getEcdLive().observe((LifecycleOwner) context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                n=integer;
                n=-1;
                getViewModel.setEcd(n);
            }
        });
        //get header map
        getViewModel.getEditHeaderMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SelectedHeader>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SelectedHeader>> stringListLinkedHashMap) {
                editHeaderMap=stringListLinkedHashMap;
            }
        });
        //get edit selected header list
        getViewModel.getE_selectedHeadersLive().observe((LifecycleOwner) context, new Observer<List<SelectedHeader>>() {
            @Override
            public void onChanged(List<SelectedHeader> selectedHeaders) {
                e_selectedHeaders=selectedHeaders;
            }
        });

        ///////////***************************clear list in live data model****************************//////////////////////

        //get edit func map to cancel orders
        getViewModel.getEditFuncMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> stringLinkedHashMapLinkedHashMap) {
                editFunc_Map=stringLinkedHashMapLinkedHashMap;
            }
        });
        //get edit selected header and session list
        getViewModel.getE_sessionListsLive().observe((LifecycleOwner) context, new Observer<List<SelectedSessionList>>() {
            @Override
            public void onChanged(List<SelectedSessionList> sessionLists) {
                e_sessionLists=sessionLists;
            }
        });


        e_sessionLists=new ArrayList<>();
        getViewModel.setE_sessionLists(e_sessionLists);
        editHeaderMap=new LinkedHashMap<>();
        getViewModel.setEditHeaderMap(editHeaderMap);


        final SelectedDateList o_dateLists1=o_dateLists.get(position);


        //set date
        getViewModel.setSelected_date(o_dateLists1.getDate());


        holder.date.setText(o_dateLists1.getDate());
        MyLog.e(TAG,"orders>> o_dateLists1.getDate()"+o_dateLists1.getDate());

        //get order session map
        orderSessionMap=orderDateMap.get(o_dateLists1.getDate());
        Set<String> set = orderSessionMap.keySet();
        List<String> aList1 = new ArrayList<String>(set.size());
        for (String x1 : set)
            aList1.add(x1);
        o_selectedSessionLists.clear();
        for(int i=0;i<aList1.size();i++) {
            SelectedSessionList sessionList = new SelectedSessionList();
            String[] scb = (aList1.get(i)).split("-");
            String[] cb = (scb[1]).split("_");
            String bolen=cb[1];
            String count=cb[0];
            String[] se=(scb[0]).split("!");
            String sess=se[0];
            String time=se[1];
            sessionList.setSession_title(sess);
            sessionList.setS_count(count);
            sessionList.setTime(time);
            sessionList.setBolen(bolen);
            o_selectedSessionLists.add(sessionList);

        }


        holder.recyclerview_order_session_deatils.setHasFixedSize(true);
        holder.recyclerview_order_session_deatils.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ViewCartAdapterSession viewCartAdapter = new ViewCartAdapterSession(context, getViewModel,func_title,bottomSheet,orderSessionMap,o_dateLists1.getDate(),o_selectedSessionLists);
        holder.recyclerview_order_session_deatils.setAdapter(viewCartAdapter);

    }

    @Override
    public int getItemCount() {

            return o_dateLists.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private RecyclerView recyclerview_order_session_deatils;


        public ViewHolder(View view) {
            super(view);
            recyclerview_order_session_deatils = view.findViewById(R.id.recyclerview_order_session_deatils);
            date = view.findViewById(R.id.date);


        }
    }
}

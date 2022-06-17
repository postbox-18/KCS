package com.example.kcs.Fragment.PlaceOrders.Date;

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

import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Header.SessionDateTime;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.PlaceOrderViewCartAdapterSession;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.OrderItemLists;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.ViewCartAdapter;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.ViewCartAdapterSession;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrdersItems.SelectedDateList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class PlaceOrderViewCartAdapterDate extends RecyclerView.Adapter<PlaceOrderViewCartAdapterDate.ViewHolder> {
    private Context context;
    private List<OrderItemLists> orderItemListss = new ArrayList<>();
    private ViewCartAdapter viewCartAdapter;
    private String TAG = "PlaceOrderViewCartAdapterDate";
    private String func_title, s_user_name, sess_title;
    private List<SelectedSessionList> sessionLists = new ArrayList<>();
    private GetViewModel getViewModel;
    private List<SelectedHeader> selectedHeaders = new ArrayList<>();
    private List<SelectedSessionList> e_sessionLists=new ArrayList<>();
    private BottomSheetDialog bottomSheet;
    //edit hash map list
    private List<SelectedHeader> e_selectedHeaders=new ArrayList<>();
    private LinkedHashMap<String, List<SelectedHeader>> editHeaderMap = new LinkedHashMap<>();
    private List<SessionDateTime> sessionDateTimes=new ArrayList<>();
    private List<SelectedDateList> e_dateLists=new ArrayList<>();
    //cancel map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> editFunc_Map = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editDateMap = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> editSessionMap = new LinkedHashMap<>();
    private int n;



    public PlaceOrderViewCartAdapterDate(Context context, GetViewModel getViewModel, String func_title, List<SelectedDateList> dateLists, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editDateMap) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.func_title =func_title;
        this.editDateMap = new LinkedHashMap<>(editDateMap);
        this.e_dateLists = dateLists;
    }


    @NonNull
    @Override
    public PlaceOrderViewCartAdapterDate.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.bottom_sheet_order_date, parent, false);
        return new PlaceOrderViewCartAdapterDate.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOrderViewCartAdapterDate.ViewHolder holder, int position) {
        final SelectedDateList e_dateLists1=e_dateLists.get(position);
        holder.date.setText(e_dateLists1.getDate());

        //get order session map
        editSessionMap=editDateMap.get(e_dateLists1.getDate());
        //get date list
        Set<String> set = editSessionMap.keySet();
        List<String> aList1 = new ArrayList<String>(set.size());
        for (String x1 : set)
            aList1.add(x1);
        e_sessionLists.clear();
        for(int i=0;i<aList1.size();i++)
        {
            SelectedSessionList sessionList=new SelectedSessionList();

            String[] str=(aList1.get(i)).split("_");
            String bolen=str[1];
            String[]s=(str[0]).split("!");
            String sess=s[0];
            String time=s[1];

            sessionList.setSession_title(sess);
            sessionList.setTime(time);
            sessionList.setBolen(bolen);

            e_sessionLists.add(sessionList);

        }

        holder.recyclerview_order_session_deatils.setHasFixedSize(true);
        holder.recyclerview_order_session_deatils.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        PlaceOrderViewCartAdapterSession viewCartAdapter = new PlaceOrderViewCartAdapterSession(context, getViewModel, func_title, e_sessionLists,editSessionMap);
        holder.recyclerview_order_session_deatils.setAdapter(viewCartAdapter);

    }

    @Override
    public int getItemCount() {

        return e_dateLists.size();

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


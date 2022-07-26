package com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrdersItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.ViewModel.SharedPreferences_data;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.OrderDishLists;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.Fragment.Dish.SelectedDishList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


    //Edit HashMap
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>> editFunc_Map = new LinkedHashMap<>();
    //Date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>> editDateMap = new LinkedHashMap<>();
    //Session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>> editSessionMap = new LinkedHashMap<>();
    //Header map
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>> editHeaderMap = new LinkedHashMap<>();
    //Item map
    private LinkedHashMap<String, List<SelectedDishList>> editItemMap = new LinkedHashMap<>();


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
    //Date List
    private List<SelectedDateList> dateLists=new ArrayList<>();
    //clear Date List
    private String gn_Date,current_Date;
    private Date date = new Date();
    public MyOrdersAdapterDate(Context context, GetViewModel getViewModel, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>> orderDateMap, List<SelectedDateList> dateLists, String funcTitle) {

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
        String phone_number=new SharedPreferences_data(context).getS_phone_number();
        gn_Date=list.getDate();
        //get data func,header,list item size from hash map
        holder.date.setText(list.getDate());
        ///////////***************************clear list in live data model****************************//////////////////////

        //get func map
        getViewModel.getEditFuncMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>> stringLinkedHashMapLinkedHashMap) {
                editFunc_Map=stringLinkedHashMapLinkedHashMap;
            }
        });
        //get date map
        getViewModel.getEditDateMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>> stringLinkedHashMapLinkedHashMap) {
                editDateMap=stringLinkedHashMapLinkedHashMap;
            }
        });

        //get session map
        getViewModel.getEditSessionMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>> stringLinkedHashMapLinkedHashMap) {
                editSessionMap=stringLinkedHashMapLinkedHashMap;
            }
        });
        //get header map
        getViewModel.getEditHeaderMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>> stringLinkedHashMapLinkedHashMap) {
                editHeaderMap=stringLinkedHashMapLinkedHashMap;
            }
        });
        //get item map
        getViewModel.getEditItemMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SelectedDishList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SelectedDishList>> stringListLinkedHashMap) {
                editItemMap=stringListLinkedHashMap;
            }
        });
        ///////////***************************clear list in live data model****************************//////////////////////

                //get selected sessions
                orderSessionMap = orderDateMap.get(list.getDate());
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

                    editDateMap=new LinkedHashMap<>();
                    getViewModel.setEditDateMap(editDateMap);


                    editSessionMap=new LinkedHashMap<>();
                    getViewModel.setEditSessionMap(editSessionMap);

                    editHeaderMap=new LinkedHashMap<>();
                    getViewModel.setEditHeaderMap(editHeaderMap);

                    editItemMap=new LinkedHashMap<>();
                    getViewModel.setEditItemMap(editItemMap);

                    getViewModel.setFunc_title(funcTitle);
                    String s = funcTitle + "/" + list.getDate();
                    getViewModel.setFunc_Session(s);
                }
            });

        }

        //////////////*********remove old data automatic**************///////////////////////
        //clear date list

        Date date1=new Date();
        Date date2=new Date();

        SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
        current_Date = dates.format(date);
        gn_Date=gn_Date.replace("-","/");
        MyLog.e(TAG,"dates>>GN>>"+gn_Date+">>Cure>>"+current_Date);

        //Setting dates
        try {
            date1 = dates.parse(current_Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            date2 = dates.parse(gn_Date);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        //MyLog.e(TAG,"dates>>diff>>"+date2.before(date1));



        if(date2.before(date1))
        {
            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            //Convert long to String
            String dayDifference = Long.toString(differenceDates);
            MyLog.e(TAG,"dates>>differ>>"+dayDifference);
            gn_Date=gn_Date.replace("/","-");
            int n=Integer.parseInt(dayDifference);
            MyLog.e(TAG,"dates>>delete>>"+n);
            if(n>=2) {
                getViewModel.DeleteDate(phone_number, funcTitle, gn_Date);
            }

        }
        //////////////*********remove old data automatic**************///////////////////////

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
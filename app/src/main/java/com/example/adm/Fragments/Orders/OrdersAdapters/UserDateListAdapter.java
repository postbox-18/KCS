package com.example.adm.Fragments.Orders.OrdersAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderDishLists;
import com.example.adm.Fragments.Orders.Classes.SelectedDateList;
import com.example.adm.Fragments.Orders.Classes.SelectedSessionList;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class UserDateListAdapter extends RecyclerView.Adapter<UserDateListAdapter.ViewHolder> {
    private Context context;
    private String TAG = "UserDateListAdapter";
    private String s_user_name,funcTitle;
    private GetViewModel getViewModel;
    //order hash map
    //order map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>> orderMap = new LinkedHashMap<>();
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

    //selected date list
    private List<SelectedDateList> o_dateLists=new ArrayList<>();
    //selected session list
    private List<SelectedSessionList> o_selectedSessionLists=new ArrayList<>();

    //clear Date List
    private String gn_Date,current_Date;
    private Date date = new Date();

    public UserDateListAdapter(Context context, GetViewModel getViewModel, List<SelectedDateList> o_dateLists, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>> orderDateMap, String s_user_name, String funcTitle) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.o_dateLists = o_dateLists;
        this.orderDateMap = orderDateMap;
        this.s_user_name = s_user_name;
        this.funcTitle = funcTitle;
    }

    @NonNull
    @Override
    public UserDateListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.date_cardview, parent, false);
        return new UserDateListAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull UserDateListAdapter.ViewHolder holder, int position) {

        final SelectedDateList list = o_dateLists.get(position);
        gn_Date=list.getDate();
        holder.date.setText(list.getDate());

        orderSessionMap=orderDateMap.get(list.getDate());
        //get session
        Set<String> stringSet = orderSessionMap.keySet();
        List<String> aList = new ArrayList<String>(stringSet.size());
        for (String x : stringSet)
            aList.add(x);
        o_selectedSessionLists=new ArrayList<>();
        for(int i=0;i<aList.size();i++)
        {
            SelectedSessionList sessionList = new SelectedSessionList();

            String[] be = (aList.get(i)).split("_");
            String bolen=be[1];
            String[] scb = (be[0]).split("-");
            String count=scb[1];
            String[] se=(scb[0]).split("!");
            String sess=se[0];
            String time=se[1];
            sessionList.setSession_title(sess);
            sessionList.setCount(count);
            sessionList.setTime(time);
            sessionList.setBolen(bolen);
            o_selectedSessionLists.add(sessionList);
        }
        holder.recyclerview_session.setHasFixedSize(true);
        holder.recyclerview_session.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        UserSessionListAdapter userSessionListAdapter=new UserSessionListAdapter(context,getViewModel,o_selectedSessionLists,orderSessionMap);
        holder.recyclerview_session.setAdapter(userSessionListAdapter);

      //on click
        holder.date_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String s=s_user_name+"/"+funcTitle+"/"+list.getDate();
                    getViewModel.setDateString(s);
            }
        });

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
                getViewModel.DeleteDate(s_user_name, funcTitle, gn_Date);
            }

        }
        else
        {

        }






    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "sessionLists>>49>>" + o_dateLists.size());
        return o_dateLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private CardView date_card;
        private RecyclerView recyclerview_session;
        public ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            date_card = view.findViewById(R.id.date_card);
            recyclerview_session = view.findViewById(R.id.recyclerview_session);


        }
    }
}



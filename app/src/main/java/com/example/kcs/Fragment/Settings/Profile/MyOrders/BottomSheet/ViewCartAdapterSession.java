package com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Header.SessionDateTime;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ViewCartAdapterSession extends RecyclerView.Adapter<ViewCartAdapterSession.ViewHolder> {
    private Context context;
    private List<OrderItemLists> orderItemListss = new ArrayList<>();
    private ViewCartAdapter viewCartAdapter;
    private String TAG = "ViewCartAdapterSession";
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
    //cancel map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editFunc_Map = new LinkedHashMap<>();
    private int n;
    //order hashmap
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> orderFunc_Map = new LinkedHashMap<>();
    //header map
    private LinkedHashMap<String, List<OrderItemLists>> orderHeaderMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap = new LinkedHashMap<>();
    //selected headers
    private List<SelectedHeader> o_selectedHeaders=new ArrayList<>();


    public ViewCartAdapterSession(Context context, GetViewModel getViewModel, String s, List<SelectedSessionList> sessionLists, String s1, BottomSheetDialog bottomSheet, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.func_title = s;
        this.sess_title = s1;
        this.sessionLists = sessionLists;
        this.bottomSheet = bottomSheet;
        this.orderSessionMap = orderSessionMap;
    }


    @NonNull
    @Override
    public ViewCartAdapterSession.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.bottom_sheet_order_session, parent, false);
        return new ViewCartAdapterSession.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCartAdapterSession.ViewHolder holder, int position) {

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
        getViewModel.getEditFuncMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> stringLinkedHashMapLinkedHashMap) {
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

        if (sessionLists == null) {
            String[] s=sess_title.split("!");
            String[] date_time=(s[1]).split("_");
            String bolen=date_time[1];
            if(bolen.equals("true")) {
                holder.session_title.setText(s[0]);
                holder.session_title.setTextColor(context.getResources().getColor(R.color.btn_gradient_light));
                holder.date_time.setText(date_time[0]);
                holder.date_time.setTextColor(context.getResources().getColor(R.color.colorSecondary));
                //img
                holder.edit.setImageResource(R.drawable.ic_knife_01);
                holder.edit.setVisibility(View.VISIBLE);
                holder.cancel.setImageResource(R.drawable.ic_calendar_x_fill);
                holder.delete.setImageResource(R.drawable.ic_trash3_fill);
            }
            else if(bolen.equals("false")) {
                holder.session_title.setText(s[0]);
                holder.session_title.setTextColor(context.getResources().getColor(R.color.text_silver));
                holder.date_time.setText(date_time[0]);
                holder.date_time.setTextColor(context.getResources().getColor(R.color.text_silver));
                //img
                holder.edit.setVisibility(View.GONE);
                holder.cancel.setImageResource(R.drawable.ic_calendar_x_fill_cancel);
                holder.delete.setImageResource(R.drawable.ic_trash3_fill_cancel);
            }

            //get selected header to viewHeaderAdapter
            orderHeaderMap=orderSessionMap.get(sess_title);
            MyLog.e(TAG,"orders>>orderSessionMap session iff>>"+new GsonBuilder().setPrettyPrinting().create().toJson(orderSessionMap));
            Set<String> set = orderHeaderMap.keySet();
            List<String> aList1 = new ArrayList<String>(set.size());
            for (String x1 : set)
                aList1.add(x1);
            o_selectedHeaders.clear();
            for(int i=0;i<aList1.size();i++)
            {
                SelectedHeader header=new SelectedHeader(
                        aList1.get(i)
                );

                o_selectedHeaders.add(header);
                //get header list and item size
            }
            holder.recyclerview_order_item_details.setHasFixedSize(true);
            holder.recyclerview_order_item_details.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

            ViewCartAdapterHeader viewCartAdapter = new ViewCartAdapterHeader(context, getViewModel, o_selectedHeaders, sess_title, func_title,bolen,orderHeaderMap);
            holder.recyclerview_order_item_details.setAdapter(viewCartAdapter);




           /* //get selected session and header hashmap
            getViewModel.getSh_f_mapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SelectedHeader>>>() {
                @Override
                public void onChanged(LinkedHashMap<String, List<SelectedHeader>> stringListLinkedHashMap) {
                    MyLog.e(TAG,"cancel>> sess_titlen  "+sess_title);
                    selectedHeaders = stringListLinkedHashMap.get(sess_title);

                    ViewCartAdapterHeader viewCartAdapter = new ViewCartAdapterHeader(context, getViewModel, selectedHeaders, sess_title, func_title,bolen);
                    holder.recyclerview_order_item_details.setAdapter(viewCartAdapter);
                    holder.recyclerview_order_item_details.setHasFixedSize(true);
                    holder.recyclerview_order_item_details.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

                }
            });*/


            //onclick
            //edit
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog(sess_title,0, bolen);
                }
            });
            //cancel
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog(sess_title,1, bolen);
                }
            });//edit
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog(sess_title,2, bolen);
                }
            });

        }
        else {
            final SelectedSessionList list = sessionLists.get(position);
            //set session date time bolen
            String sess_date=list.getSession_title()+"!"+list.getDate_time()+"_"+list.getBolen();
            MyLog.e(TAG,"cancel>> session>>"+list.getSession_title());
            holder.session_title.setText(list.getSession_title());
            holder.session_title.setTextColor(context.getResources().getColor(R.color.btn_gradient_light));
            holder.date_time.setText(list.getDate_time());
            holder.date_time.setTextColor(context.getResources().getColor(R.color.colorSecondary));

            //get selected header to viewHeaderAdapter
            orderHeaderMap=orderSessionMap.get(sess_date);
            MyLog.e(TAG,"orders>>orderSessionMap session else>>"+new GsonBuilder().setPrettyPrinting().create().toJson(orderSessionMap));

            Set<String> set = orderHeaderMap.keySet();
            List<String> aList1 = new ArrayList<String>(set.size());
            for (String x1 : set)
                aList1.add(x1);
            o_selectedHeaders.clear();
            for(int i=0;i<aList1.size();i++)
            {
                SelectedHeader header=new SelectedHeader(
                        aList1.get(i)
                );

                o_selectedHeaders.add(header);
                //get header list and item size
            }
            holder.recyclerview_order_item_details.setHasFixedSize(true);
            holder.recyclerview_order_item_details.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

            ViewCartAdapterHeader viewCartAdapter = new ViewCartAdapterHeader(context, getViewModel, o_selectedHeaders, sess_date, func_title, list.getBolen(), orderHeaderMap);
            holder.recyclerview_order_item_details.setAdapter(viewCartAdapter);

           /* //get selected session and header hashmap
            getViewModel.getSh_f_mapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SelectedHeader>>>() {
                @Override
                public void onChanged(LinkedHashMap<String, List<SelectedHeader>> stringListLinkedHashMap) {
                    selectedHeaders = stringListLinkedHashMap.get(sess_date);
                    ViewCartAdapterHeader viewCartAdapter = new ViewCartAdapterHeader(context, getViewModel, selectedHeaders, sess_date, func_title, list.getBolen());
                    holder.recyclerview_order_item_details.setAdapter(viewCartAdapter);
                    holder.recyclerview_order_item_details.setHasFixedSize(true);
                    holder.recyclerview_order_item_details.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

                }
            });*/


            //onclick
            //edit
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog(sess_date,0,list.getBolen());
                }
            });
            //cancel
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog(sess_date,1, list.getBolen());
                }
            });//edit
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog(sess_date,2, list.getBolen());
                }
            });
        }

    }
    private void alertDialog(String session_title, int n, String bolen) {

        String[] str=session_title.split("_");
        String b=str[1];
        String[] s=(str[0]).split("!");
        String ses=s[0];
        String dTime=s[1];
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        if(n==0) {
            alert.setMessage("You want to Edit the "+ses+" Session at "+dTime+" is "+b);
            alert.setTitle("Edit");
        }

        else if(n==1) {
            alert.setMessage("You want to Cancel the  "+ses+" Session at "+dTime+" is "+b);
            alert.setTitle("Cancel");
        }

        else if(n==2) {
            alert.setMessage("You want to Delete the  "+ses+" Session at "+dTime+" is "+b);
            alert.setTitle("Delete");
        }
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                bottomSheet.dismiss();
                getViewModel.setEcd(n);
                //set value for Edit Cancel Delete
                MyLog.e(TAG,"cancel>>value sess "+n);
                if(n==0) {
                    getViewModel.setI_value(1);
                    e_selectedHeaders = new ArrayList<>();
                    getViewModel.setE_selectedHeaders(e_selectedHeaders);
                    getViewModel.getSelecteds_map();
                }
                else if(n==1)
                {
                    MyLog.e(TAG,"Cancel>>sess_date>>"+session_title);
                    //Cancel
                    //set cancel hash map
                    getViewModel.CancelOrders(func_title, session_title,n,s_user_name,bolen,editFunc_Map);
                }
                else if(n==2)
                {
                    //Delete
                    getViewModel.CancelOrders(func_title, session_title,n,s_user_name,bolen,editFunc_Map);

                }

            }


        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        if (sessionLists == null) {
            return 1;
        } else {
            return sessionLists.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView session_title,date_time;
        private RecyclerView recyclerview_order_item_details;
        private ImageView edit,cancel,delete;


        public ViewHolder(View view) {
            super(view);
            recyclerview_order_item_details = view.findViewById(R.id.recyclerview_order_item_details);
            session_title = view.findViewById(R.id.session_title);
            date_time = view.findViewById(R.id.date_time);
            edit = view.findViewById(R.id.edit);
            cancel = view.findViewById(R.id.cancel);
            delete = view.findViewById(R.id.delete);


        }
    }
}


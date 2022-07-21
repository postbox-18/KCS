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
import com.example.kcs.ViewModel.SharedPreferences_data;
import com.example.kcs.Fragment.Header.SessionDateTime;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.Fragment.Dish.SelectedDishList;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ViewCartAdapterSession extends RecyclerView.Adapter<ViewCartAdapterSession.ViewHolder> {
    private Context context;
    private List<OrderDishLists> orderDishListsses = new ArrayList<>();
    private ViewCartAdapterItem viewCartAdapterItem;
    private String TAG = "ViewCartAdapterSession";
    private String func_title, phone_number, sess_title, date;
    private GetViewModel getViewModel;
    private List<SelectedHeader> selectedHeaders = new ArrayList<>();
    private List<SelectedSessionList> e_sessionLists = new ArrayList<>();
    private BottomSheetDialog bottomSheet;
    //edit hash map list
    private List<SelectedDishList> selectedDishLists = new ArrayList<>();
    private List<SessionDateTime> sessionDateTimes = new ArrayList<>();
    //cancel map
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
    private int n = -1;
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
    private List<SelectedHeader> o_selectedHeaders = new ArrayList<>();
    private List<SelectedSessionList> o_selectedSessionLists = new ArrayList<>();


    public ViewCartAdapterSession(Context context, GetViewModel getViewModel, String func_title, BottomSheetDialog bottomSheet, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>> orderSessionMap, String date, List<SelectedSessionList> o_selectedSessionLists) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.func_title = func_title;
        this.date = date;
        this.o_selectedSessionLists = o_selectedSessionLists;
        this.bottomSheet = bottomSheet;
        this.orderSessionMap = new LinkedHashMap<>(orderSessionMap);
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
        phone_number = new SharedPreferences_data(context).getS_phone_number();
        //clear
        ///////////***************************clear list in live data model****************************//////////////////////
        //get Edit Delete Cancel value
        getViewModel.getEcdLive().observe((LifecycleOwner) context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                n = integer;
                n = -1;
                getViewModel.setEcd(n);
            }
        });
        //get item map
        getViewModel.getEditItemMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SelectedDishList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SelectedDishList>> stringListLinkedHashMap) {
                editItemMap = stringListLinkedHashMap;
                //editItemMap = new LinkedHashMap<>();
                getViewModel.setEditItemMap(editItemMap);
            }
        });
        ///////////***************************clear list in live data model****************************//////////////////////

        //get edit func map to cancel orders
        //get edit func map
        getViewModel.getEditFuncMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>> stringLinkedHashMapLinkedHashMap) {
                editFunc_Map = stringLinkedHashMapLinkedHashMap;
            }
        });
        //get edit selected header and session list
        getViewModel.getE_sessionListsLive().observe((LifecycleOwner) context, new Observer<List<SelectedSessionList>>() {
            @Override
            public void onChanged(List<SelectedSessionList> sessionLists) {
                e_sessionLists = sessionLists;
            }
        });


        e_sessionLists = new ArrayList<>();
        getViewModel.setE_sessionLists(e_sessionLists);



        final SelectedSessionList list = o_selectedSessionLists.get(position);


        //set session date time bolen
        String sess_date = list.getSession_title() + "!" + list.getTime() + "-" + list.getS_count() + "_" + list.getBolen();

        //set selected session time count
        getViewModel.setSessionDateTimeCount(sess_date);
        MyLog.e(TAG, "s_counts>>sess set >>" + sess_date);


        MyLog.e(TAG, "cancel>> session>>" + list.getSession_title());
        if ((list.getBolen()).equals("true")) {
            holder.session_title.setText(list.getSession_title());
            holder.session_title.setTextColor(context.getResources().getColor(R.color.btn_gradient_light));
            holder.date_time.setText(list.getTime());
            holder.date_time.setTextColor(context.getResources().getColor(R.color.colorSecondary));
            holder.count.setText(list.getS_count());
            holder.count.setTextColor(context.getResources().getColor(R.color.btn_gradient_light));
            //img
            holder.edit.setImageResource(R.drawable.ic_knife_01);
            holder.edit.setVisibility(View.VISIBLE);

            holder.cancel.setImageResource(R.drawable.ic_calendar_x_fill);
            holder.delete.setImageResource(R.drawable.ic_trash3_fill);
        } else if ((list.getBolen()).equals("false")) {
            holder.session_title.setText(list.getSession_title());
            holder.session_title.setTextColor(context.getResources().getColor(R.color.text_silver));
            holder.date_time.setText(list.getTime());
            holder.date_time.setTextColor(context.getResources().getColor(R.color.text_silver));
            holder.count.setText(list.getS_count());
            holder.count.setTextColor(context.getResources().getColor(R.color.text_silver));
            //img
            holder.edit.setVisibility(View.GONE);
            holder.cancel.setImageResource(R.drawable.ic_calendar_x_fill_cancel);
            holder.delete.setImageResource(R.drawable.ic_trash3_fill_cancel);
        }


        //get selected header to viewHeaderAdapter
        orderHeaderMap = orderSessionMap.get(sess_date);


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
        holder.recyclerview_order_item_details.setHasFixedSize(true);
        holder.recyclerview_order_item_details.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        MyLog.e(TAG, "s_count>>sess>>" + sess_date);
        ViewCartAdapterHeader viewCartAdapter = new ViewCartAdapterHeader(context, getViewModel, o_selectedHeaders, sess_date, func_title, orderHeaderMap, date);
        holder.recyclerview_order_item_details.setAdapter(viewCartAdapter);


        //onclick
        //edit
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.e(TAG, "s_counts>>sess set >>" + sess_date);
                alertDialog(sess_date, 0, list.getBolen());
            }
        });
        //cancel
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog(sess_date, 1, list.getBolen());
            }
        });//edit
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog(sess_date, 2, list.getBolen());
            }
        });


    }

    private void alertDialog(String session_title, int n, String bolen) {
        MyLog.e(TAG, "s_counts>>sess set alertDialog >>" + session_title);

        String[] scb = session_title.split("-");
        String[] cb = (scb[1]).split("_");
        String[] st = (scb[0]).split("!");
        String count = cb[0];
        String b = (cb[1]).toUpperCase(Locale.ROOT);
        String ses = st[0];
        String dTime = st[1];
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("You want to Edit the " + ses + " Session at " + dTime + " and Head Count is " + count + " Your order is " + b);
        if (n == 0) {
            alert.setTitle("Edit");
        } else if (n == 1) {
            alert.setTitle("Cancel");
        } else if (n == 2) {
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
                MyLog.e(TAG, "cancel>>value sess " + n);
                if (n == 0) {
                    getViewModel.setI_value(1);
                    getViewModel.getSelecteds_map(date, ses, dTime, b, count);
                } else if (n == 1) {
                    MyLog.e(TAG, "Cancel>>sess_date>>" + session_title);
                    //Cancel
                    //set cancel hash map
                    getViewModel.CancelOrders(func_title, session_title, n, phone_number, bolen, editFunc_Map, date);
                } else if (n == 2) {
                    //Delete
                    getViewModel.CancelOrders(func_title, session_title, n, phone_number, bolen, editFunc_Map, date);

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
       /* if (sessionLists == null) {
            return 1;
        } else {*/
        return o_selectedSessionLists.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView session_title, date_time, count;
        private RecyclerView recyclerview_order_item_details;
        private ImageView edit, cancel, delete;


        public ViewHolder(View view) {
            super(view);
            recyclerview_order_item_details = view.findViewById(R.id.recyclerview_order_item_details);
            session_title = view.findViewById(R.id.session_title);
            date_time = view.findViewById(R.id.date_time);
            edit = view.findViewById(R.id.edit);
            cancel = view.findViewById(R.id.cancel);
            delete = view.findViewById(R.id.delete);
            count = view.findViewById(R.id.count);


        }
    }
}


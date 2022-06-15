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
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MyorderSessiondapters extends RecyclerView.Adapter<MyorderSessiondapters.ViewHolder> {
    private Context context;
    private String TAG = "MyorderSessiondapters";
    private String func_title;
    private List<MyOrdersList> myOrdersList;
    private List<SelectedSessionList> sessionLists = new ArrayList<>();
    private GetViewModel getViewModel;
    //edit hash map list
    private List<SelectedSessionList> e_sessionLists=new ArrayList<>();
    private List<SelectedHeader> e_selectedHeaders=new ArrayList<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editFunc_Map = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> editSessionMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<SelectedHeader>> editHeaderMap = new LinkedHashMap<>();


    public MyorderSessiondapters(Context context, String func_title, GetViewModel getViewModel, List<SelectedSessionList> sessionLists) {
        this.func_title = func_title;
        this.context = context;
        this.sessionLists = sessionLists;
        this.getViewModel = getViewModel;
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

        //set session title and date
        holder.session_title.setText(sessionLists1.getSession_title());
        holder.date_time.setText(sessionLists1.getDate_time());

        //get hash map of my orders list
        getViewModel.getF_mapMyordersMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<MyOrdersList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<MyOrdersList>> stringListLinkedHashMap) {
                myOrdersList = stringListLinkedHashMap.get(func_title + "/" + sessionLists1.getSession_title()+"!"+sessionLists1.getDate_time());
                getViewModel.setMyOrdersList(myOrdersList);
                holder.recyclerview_item_list.setHasFixedSize(true);
                holder.recyclerview_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                MyorderItemListAdapters itemListAdapters = new MyorderItemListAdapters(context, getViewModel, myOrdersList);
                holder.recyclerview_item_list.setAdapter(itemListAdapters);
            }
        });
        ///////////***************************clear list in live data model****************************//////////////////////

        //get func map
        getViewModel.getEditFuncMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> stringLinkedHashMapLinkedHashMap) {
                editFunc_Map=stringLinkedHashMapLinkedHashMap;
            }
        });


        //get session map
        getViewModel.getEditSessionMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> stringLinkedHashMapLinkedHashMap) {
                editSessionMap=stringLinkedHashMapLinkedHashMap;
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

        //on click
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
                String s = func_title + "/" + sessionLists1.getSession_title()+"!"+sessionLists1.getDate_time()+"_"+sessionLists1.getBolen();
                getViewModel.setFunc_Session(s);
            }
        });


    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "sessionLists>>49>>" + sessionLists.size());
        return sessionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView session_title, date_time;
        private CardView session_card;
        private RecyclerView recyclerview_item_list;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            recyclerview_item_list = view.findViewById(R.id.recyclerview_item_list);
            session_title = view.findViewById(R.id.session_title);
            date_time = view.findViewById(R.id.date_time);
            session_card = view.findViewById(R.id.session_card);


        }
    }
}



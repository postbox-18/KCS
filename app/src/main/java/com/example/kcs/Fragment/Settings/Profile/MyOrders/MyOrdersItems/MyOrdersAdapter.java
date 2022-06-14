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
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrderFuncList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {
    private Context context;
    private String TAG="MyOrdersAdapter";
    private List<MyOrderFuncList> myOrderFuncLists=new ArrayList<>();
    private List<SessionList> sessionLists=new ArrayList<>();
    private List<MyOrdersList> myOrdersList;
    private GetViewModel getViewModel;
    //edit hash map list
    private List<SessionList> e_sessionLists=new ArrayList<>();
    private List<SelectedHeader> e_selectedHeaders=new ArrayList<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editFunc_Map = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> editSessionMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<SelectedHeader>> editHeaderMap = new LinkedHashMap<>();

    public MyOrdersAdapter(Context context, List<MyOrderFuncList> myOrderFuncLists, GetViewModel getViewModel) {
        this.myOrderFuncLists = myOrderFuncLists;
        this.context = context;
        this.getViewModel = getViewModel;
    }

    @NonNull
    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.myorder_cardview, parent, false);
        return new MyOrdersAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersAdapter.ViewHolder holder, int position) {
        final MyOrderFuncList myOrderFuncLists1 = myOrderFuncLists.get(position);

        //get data func,header,list item size from hash map
        holder.func.setText(myOrderFuncLists1.getFunc());
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

        //get session list
        getViewModel.getSs_f_mapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SessionList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SessionList>> stringListLinkedHashMap) {
                String username=new SharedPreferences_data(context).getS_user_name();
                sessionLists=stringListLinkedHashMap.get(username+"-"+myOrderFuncLists1.getFunc());
                holder.recyclerview_session.setHasFixedSize(true);
                holder.recyclerview_session.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                MyorderSessiondapters itemListAdapters = new MyorderSessiondapters(context, myOrderFuncLists1.getFunc(),getViewModel,sessionLists);
                holder.recyclerview_session.setAdapter(itemListAdapters);
            }
        });



        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.e(TAG, "edit>>s on click card");
                editFunc_Map=new LinkedHashMap<>();
                getViewModel.setEditFuncMap(editFunc_Map);
                editSessionMap=new LinkedHashMap<>();
                getViewModel.setEditSessionMap(editSessionMap);
                editHeaderMap=new LinkedHashMap<>();
                getViewModel.setEditHeaderMap(editHeaderMap);
                getViewModel.setFunc_title(myOrderFuncLists1.getFunc());
                //getViewModel.SetBreadCrumsList(myOrderFuncLists1.getFunc(), 0);
            }
        });

    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "myOrderFuncLists>>49>>" + myOrderFuncLists.size());
        return myOrderFuncLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView   func;
        private CardView card_view;
        private RecyclerView recyclerview_session;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            recyclerview_session = view.findViewById(R.id.recyclerview_session);
            func = view.findViewById(R.id.func);
            card_view = view.findViewById(R.id.card_view);


        }
    }
}


package com.example.kcs.Fragment.Profile.MyOrders.BottomSheet;

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
import com.example.kcs.Fragment.PlaceOrders.SelectedHeader;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ViewCartAdapterSession extends RecyclerView.Adapter<ViewCartAdapterSession.ViewHolder> {
    private Context context;
    private List<OrderItemLists> orderItemListss = new ArrayList<>();
    private ViewCartAdapter viewCartAdapter;
    private String TAG = "ViewCartAdapterSession";
    private String func_title,s_user_name;
    private List<SessionList> sessionLists=new ArrayList<>();
    private GetViewModel getViewModel;
    private List<SelectedHeader> selectedHeaders=new ArrayList<>();


    public ViewCartAdapterSession(Context context, GetViewModel getViewModel, List<SessionList> sessionLists,String s) {
        this.context=context;
        this.getViewModel=getViewModel;
        this.func_title=s;
        this.sessionLists=sessionLists;
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
        final SessionList list=sessionLists.get(position);

        holder.session_title.setText(list.getSession_title());

        //get user name shared prefernces
        s_user_name=new SharedPreferences_data(context).getS_user_name();

        //get selected header List
        /*getViewModel.getSelectedHeadersListMutableLiveData().observe((LifecycleOwner) context, new Observer<List<SelectedHeader>>() {
            @Override
            public void onChanged(List<SelectedHeader> selectedHeaders) {
                ViewCartAdapterHeader viewCartAdapter=new ViewCartAdapterHeader(context,getViewModel,selectedHeaders,list.getSession_title(),func_title);
                holder.recyclerview_order_item_details.setAdapter(viewCartAdapter);
                holder.recyclerview_order_item_details.setHasFixedSize(true);
                holder.recyclerview_order_item_details.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            }
        });*/

        //get selected session and header hashmap
        getViewModel.getSh_f_mapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SelectedHeader>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SelectedHeader>> stringListLinkedHashMap) {
                selectedHeaders=stringListLinkedHashMap.get(list.getSession_title());
                ViewCartAdapterHeader viewCartAdapter=new ViewCartAdapterHeader(context,getViewModel,selectedHeaders,list.getSession_title(),func_title);
                holder.recyclerview_order_item_details.setAdapter(viewCartAdapter);
                holder.recyclerview_order_item_details.setHasFixedSize(true);
                holder.recyclerview_order_item_details.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

            }
        });



    }


    @Override
    public int getItemCount() {
        return sessionLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView session_title;
        private RecyclerView recyclerview_order_item_details;


        public ViewHolder(View view) {
            super(view);
            recyclerview_order_item_details = view.findViewById(R.id.recyclerview_order_item_details);
            session_title = view.findViewById(R.id.session_title);

        }
    }
}


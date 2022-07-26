package com.example.adm.Fragments.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;

import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.List;

public class AdaptersNotify extends RecyclerView.Adapter<AdaptersNotify.ViewHolder> {
    private List<NotifyList> notifyLists;
    private Context context;
    private String TAG="AdaptersNotify";
    private GetViewModel getViewModel;
    public AdaptersNotify(Context context, GetViewModel getViewModel, List<NotifyList> notifyLists) {
        this.notifyLists = notifyLists;
        this.context = context;
        this.getViewModel = getViewModel;
    }

    @NonNull
    @Override
    public AdaptersNotify.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.notify_recycler_adapters, parent, false);
        return new AdaptersNotify.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptersNotify.ViewHolder holder, int position) {
        final NotifyList list = notifyLists.get(position);
        holder.msg.setText(list.getMsg());

    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "userdeatils>>49>>" + notifyLists.size());
        return notifyLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView msg;

        public ViewHolder(View view) {
            super(view);
            msg = view.findViewById(R.id.msg);



        }
    }
}

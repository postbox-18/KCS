package com.example.kcs.Fragment.Session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Header.SessionDateTime;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.ViewModel.TimeList;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder>  {
    private Context context;
    private List<SessionList> sessionLists;
    private String TAG="SessionAdapter",funList_title;
    private GetViewModel getViewModel;
    private LinkedHashMap<String, List<TimeList>> stringListLinkedHashMap = new LinkedHashMap<>();
    private List<SessionDateTime> sessionDateTimes = new ArrayList<>();
    private LinkedHashMap<String, List<SessionDateTime>> f_mapsdtMutable = new LinkedHashMap<>();
    public SessionAdapter(Context context, List<SessionList> sessionLists, GetViewModel getViewModel, String funList_title) {
        this.context=context;
        this.sessionLists=sessionLists;
        this.getViewModel=getViewModel;
        this.funList_title=funList_title;
    }

    @NonNull
    @Override
    public SessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.session_layout, parent, false);
        return new SessionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final SessionList sessionList1 = sessionLists.get(position);

        String[] str = (sessionList1.getSession_title()).split(" ");
        if(str.length>1) {
            Spannable word = new SpannableString(str[0]);
            word.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorSecondary)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.session_title.setText(word);
            Spannable wordTwo = new SpannableString(str[1]);
            wordTwo.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.session_title.append(" ");
            holder.session_title.append(wordTwo);
        }
        else
        {
            holder.session_title.setText(sessionList1.getSession_title());
            holder.session_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
        }
        

        //holder.session_title.setText(sessionList1.getSession_title());
        holder.session_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(1);
                getViewModel.setSession_title(sessionList1.getSession_title());
                getViewModel.SetBreadCrumsList(sessionList1.getSession_title(), 1);
                //get SessionDateTime Hash Map
                getViewModel.getF_mapsdtMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SessionDateTime>>>() {
                    @Override
                    public void onChanged(LinkedHashMap<String, List<SessionDateTime>> stringListLinkedHashMap) {
                        f_mapsdtMutable = stringListLinkedHashMap;
                        MyLog.e(TAG,"dateTime>> gson>>"+new GsonBuilder().setPrettyPrinting().create().toJson(f_mapsdtMutable));
                        MyLog.e(TAG, "datetime>>set >>" + funList_title + "-" + sessionList1.getSession_title());
                        sessionDateTimes = f_mapsdtMutable.get(funList_title + "-" + sessionList1.getSession_title());
                        if((sessionDateTimes==null) || (sessionDateTimes.isEmpty())) {
                            sessionDateTimes=new ArrayList<>();
                            getViewModel.setSessionDateTimes(sessionDateTimes);
                            getViewModel.setTimepicker("");
                            getViewModel.setDate_picker("");


                        }
                        else
                        {

                            getViewModel.setSessionDateTimes(sessionDateTimes);
                            MyLog.e(TAG, "datetime>>set sessionDateTimes>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(sessionDateTimes));
                            getViewModel.setTimepicker(sessionDateTimes.get(0).getTime());
                            getViewModel.setDate_picker(sessionDateTimes.get(0).getDate());
                        }


                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return sessionLists.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout session_linear;
        private ImageView session_img;
        private TextView session_title;
        public ViewHolder(View view) {
            super(view);
            session_img=view.findViewById(R.id.session_img);
            session_title=view.findViewById(R.id.session_title);
            session_linear=view.findViewById(R.id.session_linear);

        }
    }
}

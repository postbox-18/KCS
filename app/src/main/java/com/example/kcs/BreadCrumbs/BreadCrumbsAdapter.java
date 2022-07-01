package com.example.kcs.BreadCrumbs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Header.HeaderList;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

public class BreadCrumbsAdapter extends RecyclerView.Adapter<BreadCrumbsAdapter.ViewHolder> {
    private Context context;
    private List<BreadCrumbList> breadCrumbLists = new ArrayList<>();
    private String TAG = "BreadCrumbsAdapter";
    private GetViewModel getViewModel;

    private List<FunList> funLists = new ArrayList<>();
    private List<SessionList> sessionLists = new ArrayList<>();
    private List<HeaderList> headerLists = new ArrayList<>();

    public BreadCrumbsAdapter(Context context, GetViewModel getViewModel, List<BreadCrumbList> breadCrumbLists) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.breadCrumbLists = breadCrumbLists;
    }


    @NonNull
    @Override
    public BreadCrumbsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.layout_breadcrums, parent, false);
        return new BreadCrumbsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreadCrumbsAdapter.ViewHolder holder, int position) {

        //func list
        getViewModel.getFunMutableList().observe((LifecycleOwner) context, new Observer<List<FunList>>() {
            @Override
            public void onChanged(List<FunList> funLists1) {
                funLists = funLists1;
            }
        });

        //session list
        getViewModel.getSessionListMutable().observe((LifecycleOwner) context, new Observer<List<SessionList>>() {
            @Override
            public void onChanged(List<SessionList> sessionLists1) {
                sessionLists = sessionLists1;
            }
        });

        //header list
        getViewModel.getHeaderListMutableList().observe((LifecycleOwner) context, new Observer<List<HeaderList>>() {
            @Override
            public void onChanged(List<HeaderList> headerLists1) {
                headerLists = headerLists1;
            }
        });

        final BreadCrumbList breadCrumbList1 = breadCrumbLists.get(position);
        String item = breadCrumbList1.getBreadcrumbs();
        holder.list.setText(item);
        holder.breadCrumsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //func list
                for (int i = 0; i < funLists.size(); i++) {
                    if ((funLists.get(i).getFun()).equals(item)) {
                        getViewModel.setI_value(0);
                        break;
                    }

                }
                //session list
                for (int i = 0; i < sessionLists.size(); i++) {
                    if ((sessionLists.get(i).getSession_title()).equals(item)) {
                        getViewModel.setI_value(6);
                        break;
                    }
                }
                //header list
                for (int i = 0; i < headerLists.size(); i++) {
                    if ((headerLists.get(i).getHeader()).equals(item)) {
                        getViewModel.setI_value(1);
                        break;
                    }
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return breadCrumbLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView list;
        private ConstraintLayout breadCrumsLayout;

        public ViewHolder(View view) {
            super(view);
            list = view.findViewById(R.id.list);
            breadCrumsLayout = view.findViewById(R.id.breadCrumsLayout);

        }
    }
}


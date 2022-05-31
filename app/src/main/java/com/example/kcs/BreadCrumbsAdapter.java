package com.example.kcs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

public class BreadCrumbsAdapter extends RecyclerView.Adapter<BreadCrumbsAdapter.ViewHolder> {
    private Context context;
    private List<BreadCrumbList> breadCrumbLists = new ArrayList<>();
    private String TAG = "BreadCrumbsAdapter";
    private GetViewModel getViewModel;


    public BreadCrumbsAdapter(Context context, GetViewModel getViewModel, List<BreadCrumbList> breadCrumbLists) {
        this.context=context;
        this.getViewModel=getViewModel;
        this.breadCrumbLists=breadCrumbLists;
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
        final BreadCrumbList breadCrumbList1=breadCrumbLists.get(position);
        holder.list.setText(breadCrumbList1.getBreadcrumbs());


    }


    @Override
    public int getItemCount() {
        return breadCrumbLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView list;

        public ViewHolder(View view) {
            super(view);
            list = view.findViewById(R.id.list);

        }
    }
}


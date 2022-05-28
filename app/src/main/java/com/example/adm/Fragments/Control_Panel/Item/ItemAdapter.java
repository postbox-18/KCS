package com.example.adm.Fragments.Control_Panel.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<ItemArrayList> item;
    private Context context;
    private String TAG="ItemAdapter";
    public ItemAdapter(Context context, List<ItemArrayList> item, GetViewModel getViewModel) {
        this.item = item;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_cardview, parent, false);
        return new ItemAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        final ItemArrayList item1 = item.get(position);
        holder.item_title.setText(item1.getItem());
    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "item>>49>>" + item.size());
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private EditText item_title;

        public ViewHolder(View view) {
            super(view);
            item_title = view.findViewById(R.id.item_title);
        }
    }
}


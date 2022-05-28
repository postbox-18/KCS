package com.example.adm.Fragments.Control_Panel.Header;

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

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
    private List<HeaderList> headerLists;
    private Context context;
    private String TAG="HeaderAdapter";
    public HeaderAdapter(Context context, List<HeaderList> headerLists, GetViewModel getViewModel) {
        this.headerLists = headerLists;
        this.context = context;
    }

    @NonNull
    @Override
    public HeaderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.header_cardview, parent, false);
        return new HeaderAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderAdapter.ViewHolder holder, int position) {
        final HeaderList item1 = headerLists.get(position);
        holder.header_title.setText(item1.getHeader());
    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "item>>49>>" + headerLists.size());
        return headerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private EditText header_title;

        public ViewHolder(View view) {
            super(view);
            header_title = view.findViewById(R.id.header_title);
        }
    }
}



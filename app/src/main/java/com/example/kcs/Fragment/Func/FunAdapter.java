package com.example.kcs.Fragment.Func;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.R;

import java.util.List;

public class FunAdapter extends RecyclerView.Adapter<FunAdapter.ViewHolder> {
    private Context context;
    private List<FunList>funLists;
    private String TAG="FunAdapter";
    public FunAdapter(Context context, List<FunList> funLists) {
        this.context=context;
        this.funLists=funLists;
        
    }

    @NonNull
    @Override
    public FunAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fun_activity, parent, false);
        return new FunAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FunAdapter.ViewHolder holder, int position) {
        final FunList funList1 = funLists.get(position);
        //img update soon
        //holder.fun_img.setText(funList1.getUsername());
        holder.fun_title.setText(funList1.getFun());
    }

    @Override
    public int getItemCount() {
        return funLists.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView fun_img;
        private TextView fun_title;
        public ViewHolder(View view) {
            super(view);
            fun_img=view.findViewById(R.id.fun_img);
            fun_title=view.findViewById(R.id.fun_title);

        }
    }
}

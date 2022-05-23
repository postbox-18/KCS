package com.example.kcs.Fragment.Header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Items.ItemList;
import com.example.kcs.MyViewModel;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;

import java.util.LinkedHashMap;
import java.util.List;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder>  {
    private Context context;
    private List<HeaderList>headerLists;
    private String TAG="HeaderAdapter";
    HeaderAdapter.GetHeaderFragment getHeaderFragment;
    private GetViewModel getViewModel;
    private MyViewModel myViewModel;
    public interface GetHeaderFragment
    {
        void getheaderFragment(HeaderList headerList1, int position);
    }
    public HeaderAdapter(Context context, List<HeaderList> headerLists,GetHeaderFragment getHeaderFragment) {
        this.context=context;
        this.headerLists=headerLists;
        this.getHeaderFragment=getHeaderFragment;
        getViewModel= new ViewModelProvider((FragmentActivity)context).get(GetViewModel.class);
        myViewModel= new ViewModelProvider((FragmentActivity)context).get(MyViewModel.class);

    }

    @NonNull
    @Override
    public HeaderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.header_layout, parent, false);
        return new HeaderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final HeaderList headerList1 = headerLists.get(position);
        //img update soon
        //holder.header_img.setText(funList1.getUsername());
        holder.header_title.setText(headerList1.getHeader());
        holder.header_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                getHeaderFragment.getheaderFragment(headerList1,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return headerLists.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout header_linear;
        private ImageView header_img;
        private TextView header_title;
        public ViewHolder(View view) {
            super(view);
            header_img=view.findViewById(R.id.header_img);
            header_title=view.findViewById(R.id.header_title);
            header_linear=view.findViewById(R.id.header_linear);

        }
    }
}

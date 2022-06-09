package com.example.adm.Fragments.Control_Panel.Header;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;

import com.example.adm.Fragments.Control_Panel.Selected_UnSelected_List.HeaderList;
import com.example.adm.Fragments.Control_Panel.Selected_UnSelected_List.ItemArrayList;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
    private List<HeaderList> headerLists;
    private Context context;
    private String TAG="HeaderAdapter";
    private GetViewModel getViewModel;
    private LinkedHashMap<String, List<ItemArrayList>> itemArrayMap=new LinkedHashMap<>();
    private List<ItemArrayList> itemList = new ArrayList<>();
    public HeaderAdapter(Context context, List<HeaderList> headerLists, GetViewModel getViewModel) {
        this.headerLists = headerLists;
        this.context = context;
        this.getViewModel = getViewModel;
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

        //get hash map
        getViewModel.getItemArrayListMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<ItemArrayList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<ItemArrayList>> stringListLinkedHashMap) {
                itemArrayMap=stringListLinkedHashMap;


            }
        });
        //onclick
        holder.header_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get item list
                itemList=itemArrayMap.get(item1.getHeader());
                getViewModel.setHeader_title(item1.getHeader());
                getViewModel.setItemArrayList(itemList);
                getViewModel.setI_value(3);

            }
        });
    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "item>>49>>" + headerLists.size());
        return headerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView header_title;
        private CardView header_cardView;

        public ViewHolder(View view) {
            super(view);
            header_title = view.findViewById(R.id.header_title);
            header_cardView = view.findViewById(R.id.header_cardView);
        }
    }
}



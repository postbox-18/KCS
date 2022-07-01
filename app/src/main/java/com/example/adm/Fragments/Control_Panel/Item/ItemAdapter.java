package com.example.adm.Fragments.Control_Panel.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Control_Panel.Selected_UnSelected_List.ItemArrayList;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.SelectedHeader;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<ItemArrayList> item=new ArrayList<>();
    private List<SelectedHeader> selectedHeaders=new ArrayList<>();
    private Context context;
    private String TAG="ItemAdapter",header_title;
    private GetViewModel getViewModel;
    private LinkedHashMap<String,List<ItemArrayList>> selectedHeaderMap=new LinkedHashMap<>();
    public ItemAdapter(Context context, List<ItemArrayList> item, GetViewModel getViewModel) {
        this.item = item;
        this.context = context;
        this.getViewModel = getViewModel;
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


        //get hash map
        getViewModel.getItemArrayListMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<ItemArrayList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<ItemArrayList>> stringListLinkedHashMap) {
                selectedHeaderMap=stringListLinkedHashMap;
            }
        });

        //get header title
        getViewModel.getHeader_title_Mutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                header_title=s;
            }
        });

        if((item1.getSelected()).equals("true"))
        {
            holder.switchView.setChecked(true);
            //holder.item_cardView.setCardBackgroundColor(context.getResources().getColor(R.color.light_blue_color));
            holder.item_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
        }
        else
        {
            holder.switchView.setChecked(false);
           // holder.item_cardView.setCardBackgroundColor(context.getResources().getColor(R.color.lightGray));
            holder.item_title.setTextColor(context.getResources().getColor(R.color.light_gray));
        }




        holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    MyLog.e(TAG, "switch>>get enabled"+b );
                    holder.item_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
                }
                else
                {
                    MyLog.e(TAG, "switch>>get not enabled" +b);
                    holder.item_title.setTextColor(context.getResources().getColor(R.color.light_gray));

                }
                //selectedHeaderMap.get(header_title).get(position).setSelected(String.valueOf(b));
                getViewModel.updateItem(header_title, item.get(position).getItem(), String.valueOf(b));



            }
        });
    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "item>>49>>" + item.size());
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_title;
        private Switch switchView;
        private CardView item_cardView;

        public ViewHolder(View view) {
            super(view);
            item_title = view.findViewById(R.id.item_title);
            item_cardView = view.findViewById(R.id.item_cardView);
            switchView = view.findViewById(R.id.switchView);

        }
    }
}


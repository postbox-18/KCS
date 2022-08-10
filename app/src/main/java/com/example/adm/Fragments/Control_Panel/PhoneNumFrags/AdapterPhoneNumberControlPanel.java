package com.example.adm.Fragments.Control_Panel.PhoneNumFrags;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.CheckPhoneNumber;
import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Control_Panel.HeaderFrags.Dish.DishList;
import com.example.adm.Fragments.Control_Panel.HeaderFrags.Header.HeaderList;
import com.example.adm.Fragments.Control_Panel.HeaderFrags.Item.ItemArrayList;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class AdapterPhoneNumberControlPanel extends RecyclerView.Adapter<AdapterPhoneNumberControlPanel.ViewHolder> {
    private List<CheckPhoneNumber> checkPhoneNumberList;
    private Context context;
    private String TAG = "AdapterPhoneNumberControlPanel";
    private GetViewModel getViewModel;




    public AdapterPhoneNumberControlPanel(Context context, GetViewModel getViewModel, List<CheckPhoneNumber> checkPhoneNumberList) {
        this.context=context;
        this.getViewModel=getViewModel;
        this.checkPhoneNumberList=checkPhoneNumberList;
    }

    @NonNull
    @Override
    public AdapterPhoneNumberControlPanel.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.phonenumbercontrolpaneladapter_cardview, parent, false);
        return new AdapterPhoneNumberControlPanel.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPhoneNumberControlPanel.ViewHolder holder, int position) {
        final CheckPhoneNumber item1 = checkPhoneNumberList.get(position);
        holder.phone_number.setText(item1.getPhone_number());

        if((item1.getBoolen()).equals("true"))
        {
            holder.switchView.setChecked(true);
            //holder.item_cardView.setCardBackgroundColor(context.getResources().getColor(R.color.light_blue_color));
            holder.phone_number.setTextColor(context.getResources().getColor(R.color.colorSecondary));
        }
        else
        {
            holder.switchView.setChecked(false);
            // holder.dish_cardView.setCardBackgroundColor(context.getResources().getColor(R.color.lightGray));
            holder.phone_number.setTextColor(context.getResources().getColor(R.color.light_gray));
        }


        /*holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    MyLog.e(TAG, "switch>>get enabled"+b );
                    holder.phone_number.setTextColor(context.getResources().getColor(R.color.colorSecondary));
                }
                else
                {
                    MyLog.e(TAG, "switch>>get not enabled" +b);
                    holder.phone_number.setTextColor(context.getResources().getColor(R.color.light_gray));

                }
                //selectedHeaderMap.get(header_title).get(position).setSelected(String.valueOf(b));
                getViewModel.updatePhoneNumberItem(phone_number, checkPhoneNumberList.get(position).getPhone_number(),String.valueOf(b), null);



            }
        });*/
    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "item>>49>>" + checkPhoneNumberList.size());
        return checkPhoneNumberList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView phone_number;
        private Switch switchView;;

        public ViewHolder(View view) {
            super(view);
            phone_number = view.findViewById(R.id.phone_number);
            switchView = view.findViewById(R.id.switchView);


        }
    }
}


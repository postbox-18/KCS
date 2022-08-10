package com.example.adm.Fragments.Control_Panel.HeaderFrags.Dish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {
    private List<DishList> dishLists = new ArrayList<>();

    private Context context;
    private String TAG="DishAdapter";
    private String header_title,item;
    private GetViewModel getViewModel;

    public DishAdapter(Context context, List<DishList> dishLists, GetViewModel getViewModel, String header_title, String item) {
        this.dishLists = dishLists;
        this.context = context;
        this.header_title = header_title;
        this.item = item;
        this.getViewModel = getViewModel;
    }

    @NonNull
    @Override
    public DishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.dish_cardview, parent, false);
        return new DishAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull DishAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final DishList item1 = dishLists.get(position);
        holder.dish_title.setText(item1.getDish());




        if((item1.getBolen()).equals("true"))
        {
            holder.switchView.setChecked(true);
            //holder.item_cardView.setCardBackgroundColor(context.getResources().getColor(R.color.light_blue_color));
            holder.dish_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
        }
        else
        {
            holder.switchView.setChecked(false);
            // holder.dish_cardView.setCardBackgroundColor(context.getResources().getColor(R.color.lightGray));
            holder.dish_title.setTextColor(context.getResources().getColor(R.color.light_gray));
        }

        //onclick
        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dish=item1.getDish()+"_"+item1.getBolen();
                getViewModel.DeleteItem(header_title,item,dish);

                //getViewModel.updateItem(header_title, item, item1.getDish(), null,null,0);
            }
        });


        holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    MyLog.e(TAG, "switch>>get enabled"+b );
                    holder.dish_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
                }
                else
                {
                    MyLog.e(TAG, "switch>>get not enabled" +b);
                    holder.dish_title.setTextColor(context.getResources().getColor(R.color.light_gray));

                }
                //selectedHeaderMap.get(header_title).get(position).setSelected(String.valueOf(b));
                getViewModel.updateItem(header_title, item, dishLists.get(position).getDish(),String.valueOf(b), null);



            }
        });
    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "item>>49>>" + dishLists.size());
        return dishLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dish_title;
        private Switch switchView;
        private CardView dish_cardView;
        private ImageView trash;

        public ViewHolder(View view) {
            super(view);
            dish_title = view.findViewById(R.id.dish_title);
            dish_cardView = view.findViewById(R.id.dish_cardView);
            switchView = view.findViewById(R.id.switchView);
            trash = view.findViewById(R.id.trashImg);

        }
    }
}



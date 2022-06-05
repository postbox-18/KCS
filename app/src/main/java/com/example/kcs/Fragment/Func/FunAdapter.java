package com.example.kcs.Fragment.Func;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.ImgList;
import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Header.HeaderFragment;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FunAdapter extends RecyclerView.Adapter<FunAdapter.ViewHolder> {
    private Context context;
    private List<FunList> funLists;
    private String TAG = "FunAdapter";

    private GetViewModel getViewModel;
    //img
    private List<ImgList> imgLists = new ArrayList<>();

    public FunAdapter(Context context, List<FunList> funLists, GetViewModel getViewModel) {
        this.context = context;
        this.funLists = funLists;
        this.getViewModel = getViewModel;

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
        //get img list
        getViewModel.getIf_f_mapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<ImgList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<ImgList>> stringListLinkedHashMap) {
                imgLists = stringListLinkedHashMap.get(funList1.getFun());

                if (imgLists != null) {
                    Picasso.get()
                            .load(imgLists.get(0).getImg_url())
                            .placeholder(R.drawable.logo)
                            .fit()
                            .centerCrop()
                            .into(holder.fun_img);
                }


            }
        });


        //holder.fun_title.setText(funList1.getFun());
        String[] str = (funList1.getFun()).split(" ");
        if(str.length>1) {
            Spannable word = new SpannableString(str[0]);
            word.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorSecondary)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.fun_title.setText(word);
            Spannable wordTwo = new SpannableString(str[1]);
            wordTwo.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.fun_title.append(" ");
            holder.fun_title.append(wordTwo);
        }
        else
        {
            holder.fun_title.setText(funList1.getFun());
            holder.fun_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
        }


        holder.fun_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getViewModel.getfunFragment(funList1.getFun());
                getViewModel.SetBreadCrumsList(funList1.getFun(), 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return funLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView fun_img;
        private TextView fun_title;
        private CardView fun_card;

        public ViewHolder(View view) {
            super(view);
            fun_img = view.findViewById(R.id.fun_img);
            fun_title = view.findViewById(R.id.fun_title);
            fun_card = view.findViewById(R.id.fun_card);

        }
    }
}

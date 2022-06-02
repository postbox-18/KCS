package com.example.kcs.Fragment.Header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Fragment.Items.ItemList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.LinkedHashMap;
import java.util.List;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder>  {
    private Context context;
    private List<HeaderList>headerLists;
    private String TAG="HeaderAdapter";
    //HeaderAdapter.GetHeaderFragment getHeaderFragment;
    private GetViewModel getViewModel;
    //private MyViewModel myViewModel;
    private List<LinkedHashMap<String, List<ItemList>>> linkedHashMaps;
    /*public interface GetHeaderFragment
    {
        void getheaderFragment(HeaderList headerList1, int position);
    }*/
    public HeaderAdapter(Context context, List<HeaderList> headerLists, GetViewModel getViewModel, List<LinkedHashMap<String, List<ItemList>>> linkedHashMaps) {
        this.context=context;
        this.headerLists=headerLists;
        this.getViewModel=getViewModel;
        this.linkedHashMaps=linkedHashMaps;
        //this.getHeaderFragment=getHeaderFragment;
        //getViewModel= new ViewModelProvider((FragmentActivity)context).get(GetViewModel.class);


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

        String[] str = (headerList1.getHeader()).split("-");
        if(str.length>1) {
            Spannable word = new SpannableString(str[0]);
            word.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorSecondary)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.header_title.setText(word);
            Spannable wordTwo = new SpannableString(str[1]);
            wordTwo.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.header_title.append("-");
            holder.header_title.append(wordTwo);
        }
        else
        {
            holder.header_title.setText(headerList1.getHeader());
            holder.header_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
        }
        
        //holder.header_title.setText(headerList1.getHeader());
        holder.header_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getHeaderFragment.getheaderFragment(headerList1,position);.
                getViewModel.getheaderFragment(headerList1.getHeader(),position,linkedHashMaps);
                getViewModel.SetBreadCrumsList(headerList1.getHeader(), 2);

            }
        });

    }

    @Override
    public int getItemCount() {
        return headerLists.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView header_card;
        private ImageView header_img;
        private TextView header_title;
        public ViewHolder(View view) {
            super(view);
            header_img=view.findViewById(R.id.header_img);
            header_title=view.findViewById(R.id.header_title);
            header_card=view.findViewById(R.id.header_card);

        }
    }
}

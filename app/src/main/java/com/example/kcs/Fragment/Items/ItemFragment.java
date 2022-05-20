package com.example.kcs.Fragment.Items;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kcs.Fragment.Header.HeaderList;
import com.example.kcs.Fragment.HomeFragment;
import com.example.kcs.Fragment.ItemList;
import com.example.kcs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String HEADER_LIST = "HEADER_LIST";
    private static final String ITEM_LIST = "ITEM_LIST";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private TextView header_title;
    private ImageView back_btn;

    private HeaderList headerList;
    private RecyclerView recyclerview_item;
    private List<ItemList> itemLists=new ArrayList<>();
    private ItemListAdapater itemListAdapater;
    public ItemFragment(HeaderList headerList, List<ItemList> itemLists) {
        this.headerList=headerList;
        this.itemLists=itemLists;
        // Required empty public constructor
    }


    public static ItemFragment newInstance(String param1, String param2,HeaderList headerList1,List<ItemList> itemLists) {
        ItemFragment fragment = new ItemFragment(headerList1, itemLists);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(HEADER_LIST, String.valueOf(headerList1));
        args.putString(ITEM_LIST, String.valueOf(itemLists));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_item, container, false);

        header_title=view.findViewById(R.id.header_title);
        recyclerview_item=view.findViewById(R.id.recyclerview_item);
        back_btn=view.findViewById(R.id.back_btn);
        header_title.setText(headerList.getHeader());
        recyclerview_item.setHasFixedSize(true);
        recyclerview_item.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        itemListAdapater=new ItemListAdapater(getContext(),itemLists);
        recyclerview_item.setAdapter(itemListAdapater);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new HomeFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
            }
        });
        return view;
    }


}
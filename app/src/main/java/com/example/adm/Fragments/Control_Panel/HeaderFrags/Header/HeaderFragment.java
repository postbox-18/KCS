package com.example.adm.Fragments.Control_Panel.HeaderFrags.Header;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adm.Fragments.Control_Panel.HeaderFrags.Dish.DishList;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeaderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerview_header;
    private List<HeaderList> headerList=new ArrayList<>();
    private HeaderAdapter headerAdapter;

    private ImageView back_btn;
    private GetViewModel getViewModel;
    //item map
    private LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> itemArrayListMap = new LinkedHashMap<>();
    //dish map
    private LinkedHashMap<String, List<DishList>> dishListMap = new LinkedHashMap<>();


    private String TAG="HeaderFragment";
    public HeaderFragment() {
        // Required empty public constructor
    }
    public static HeaderFragment newInstance(String param1, String param2) {
        HeaderFragment fragment = new HeaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View view= inflater.inflate(R.layout.fragment_header, container, false);
        getViewModel = new ViewModelProvider(getActivity()).get(GetViewModel.class);

        back_btn=view.findViewById(R.id.back_btn);
        recyclerview_header=view.findViewById(R.id.recyclerview_header);


        recyclerview_header.setHasFixedSize(true);
        recyclerview_header.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        headerList=new ArrayList<>();


        //get item map list
        getViewModel.getItemArrayListMapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, LinkedHashMap<String, List<DishList>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> stringLinkedHashMapLinkedHashMap) {
                itemArrayListMap=stringLinkedHashMapLinkedHashMap;
                Set<String> stringSet = itemArrayListMap.keySet();
                List<String> aList = new ArrayList<String>(stringSet.size());
                for (String x : stringSet)
                    aList.add(x);
                headerList=new ArrayList<>();
                for(int i=0;i<aList.size();i++)
                {
                    HeaderList list=new HeaderList(
                            aList.get(i)
                    );
                    headerList.add(list);
                }
                headerAdapter=new HeaderAdapter(getContext(),headerList,getViewModel,itemArrayListMap);
                recyclerview_header.setAdapter(headerAdapter);

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(0);
            }
        });



        return view;
    }

}
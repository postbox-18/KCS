package com.example.kcs.Fragment.Header;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
    private static final String HEADER_LIST = "HEADER_LIST";
    private static final String FUN_LIST = "FUN_LIST";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //call from FunAdapter
    private FunList funList;
    private TextView fun_title;
    //Header
    private RecyclerView recyclerview_header;
    private HeaderAdapter headerAdapter;
    private List<HeaderList> headerList=new ArrayList<>();

    private String TAG="HeaderFragment";
    public HeaderFragment(FunList funList1, List<HeaderList> headerList) {
        this.funList=funList1;
        this.headerList=headerList;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeaderFragment newInstance(String param1, String param2,FunList funList,List<HeaderList> headerList) {
        HeaderFragment fragment = new HeaderFragment(funList, headerList);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(FUN_LIST, String.valueOf(funList));
        args.putString(HEADER_LIST, String.valueOf(headerList));
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
        recyclerview_header=view.findViewById(R.id.recyclerview_header);
        fun_title=view.findViewById(R.id.fun_title);

        fun_title.setText(funList.getFun());
        //recyclerview_header
        recyclerview_header.setHasFixedSize(true);
        recyclerview_header.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        headerAdapter=new HeaderAdapter(getContext(),headerList);
        recyclerview_header.setAdapter(headerAdapter);


        return view;
    }

}
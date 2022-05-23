package com.example.kcs.Fragment.Items;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Header.HeaderList;
import com.example.kcs.MyViewModel;
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
    private List<ItemList> itemLists = new ArrayList<>();
    private List<CheckedList> checkedLists = new ArrayList<>();
    private ItemListAdapater itemListAdapater;
    private MyViewModel myViewModel;
    private AppCompatButton order_btn,cancel_btn;
    private String TAG="ItemFragment";
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public ItemFragment(HeaderList headerList, List<ItemList> itemLists) {
        this.headerList = headerList;
        this.itemLists = itemLists;
        // Required empty public constructor
    }


    public static ItemFragment newInstance(String param1, String param2, HeaderList headerList1, List<ItemList> itemLists) {
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
        myViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        header_title = view.findViewById(R.id.header_title);
        recyclerview_item = view.findViewById(R.id.recyclerview_item);
        back_btn = view.findViewById(R.id.back_btn);
        cancel_btn = view.findViewById(R.id.cancel_btn);
        order_btn = view.findViewById(R.id.order_btn);

        firebaseDatabase = FirebaseDatabase.getInstance();


        header_title.setText(headerList.getHeader());
        recyclerview_item.setHasFixedSize(true);
        recyclerview_item.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        itemListAdapater = new ItemListAdapater(getContext(), itemLists);
        recyclerview_item.setAdapter(itemListAdapater);
        itemListAdapater.notifyDataSetChanged();

        myViewModel.getCheckedListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<CheckedList>>() {
            @Override
            public void onChanged(List<CheckedList> checkedLists1) {
                checkedLists=new ArrayList<>();
                checkedLists=checkedLists1;

                //MyLog.e(TAG, "Checkitem>>>>" + new GsonBuilder().setPrettyPrinting().create().toJson(checkedLists));
            }
        });

        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkedLists.size()>0)
                {
                    GetOrderBtn(checkedLists);
                }
                else
                {
                    Toast.makeText(getContext(), "Empty List", Toast.LENGTH_SHORT).show();
                }

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewModel.setI_value(0);
               /* if(myViewModel.getFunList().getFun()==null)
                {
                    myViewModel.setI_value(0);
                }
                else
                {
                    myViewModel.setI_value(1);
                }*/
            }
        });
        return view;
    }

    private void GetOrderBtn(List<CheckedList> checkedLists) {


        databaseReference = firebaseDatabase.getReference("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user_name=new SharedPreferences_data(getContext()).getS_user_name();

                for(int i=0;i<checkedLists.size();i++ ) {
                    //getFunc
                    databaseReference.child(user_name).child("Function").child(headerList.getHeader()).child(String.valueOf(i)).setValue(checkedLists.get(i).getItemList());
                }
                Toast.makeText(getContext(), "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(getContext(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
        
        
        
        /*if(myViewModel.getFunList().getFun()==null)
        {
            showAlertDialog(checkedLists);
        }
        else
        {
            myViewModel.setI_value(1);
        }*/
    }

    private void showAlertDialog(List<CheckedList> checkedLists) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Please select Function Type!");
        String[] str=new String[checkedLists.size()];
        for (int i = 0; i < checkedLists.size(); i++) {
            str[i] = checkedLists.get(i).getItemList();
        }
        MyLog.e(TAG,"dialog>>"+str);
        //String[] items = {"java","android","Data Structures","HTML","CSS"};
        boolean[] checkedItems = {false, false, false, false, false,false};
        alertDialog.setMultiChoiceItems(str, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                switch (which) {
                    case 0:
                        if(isChecked)
                            dialog.dismiss();
                            //Toast.makeText(getContext(), "Clicked on java", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        if(isChecked)
                            dialog.dismiss();
                            //Toast.makeText(getContext(), "Clicked on android",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        if(isChecked)
                            dialog.dismiss();
                            //Toast.makeText(getContext(), "Clicked on Data Structures", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        if(isChecked)
                            dialog.dismiss();
                            //Toast.makeText(getContext(), "Clicked on HTML", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        if(isChecked)
                            dialog.dismiss();
                            //Toast.makeText(getContext(), "Clicked on CSS", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}

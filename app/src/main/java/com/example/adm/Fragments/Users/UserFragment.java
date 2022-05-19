package com.example.adm.Fragments.Users;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SharedPreferences_data;
import com.example.adm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView user_name, phone_number, email;
    private String s_user_name, s_phone_number, s_email;
    private String TAG = "UserFragment";

    private List<UserDetailsList> userDetails;
    private UserDetailsAdapter userDetailsAdapter;
    private RecyclerView user_details_recyclerview;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        user_details_recyclerview = view.findViewById(R.id.user_details_recyclerview);
        user_details_recyclerview.setHasFixedSize(true);
        user_details_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        user_name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.email);
        phone_number = view.findViewById(R.id.phone_number);
        userDetails = new ArrayList<>();
        s_user_name = new SharedPreferences_data(getContext()).getS_user_name();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users-Id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "snap>>" + snapshot);
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "snap>>" + datas.child("username").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("email").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("phone_number").getValue().toString());
                    UserDetailsList userList = new UserDetailsList(
                            datas.child("username").getValue().toString(),
                            datas.child("email").getValue().toString(),
                            datas.child("phone_number").getValue().toString()
                    );
                    userDetails.add(userList);
                }

                userDetailsAdapter=new UserDetailsAdapter(getContext(),userDetails);
                user_details_recyclerview.setAdapter(userDetailsAdapter);

                /*String value = snapshot.getValue(String.class);
                retrieve_data.setText(value);*/
               /* user_name.setText(snapshot.child("username").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
                phone_number.setText(snapshot.child("phone_number").getValue().toString());*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
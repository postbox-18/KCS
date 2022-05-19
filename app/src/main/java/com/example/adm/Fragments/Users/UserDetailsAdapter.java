package com.example.adm.Fragments.Users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.R;
import com.google.gson.GsonBuilder;

import java.util.List;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.ViewHolder> {
    private List<UserDetailsList> userDetailsList;
    private Context context;
    private String TAG="UserDetailsAdapter";
    public UserDetailsAdapter(Context context, List<UserDetailsList> userDetails) {
        this.userDetailsList = userDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public UserDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_details_cardview, parent, false);
        return new UserDetailsAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailsAdapter.ViewHolder holder, int position) {
        final UserDetailsList detailsList = userDetailsList.get(position);
        holder.user_name.setText(detailsList.getUsername());
        holder.email.setText(detailsList.getEmail());
        holder.phone_number.setText(detailsList.getPhone_number());
    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "userdeatils>>49>>" + userDetailsList.size());
        return userDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView user_name, email, phone_number;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            user_name = view.findViewById(R.id.user_name);
            email = view.findViewById(R.id.email);
            phone_number = view.findViewById(R.id.phone_number);


        }
    }
}

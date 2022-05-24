package com.example.kcs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Header.HeaderFragment;
import com.example.kcs.Fragment.HomeFragment;
import com.example.kcs.Fragment.Items.ItemFragment;
import com.example.kcs.Fragment.Profile.MyOrders.MyOrdersFragment;
import com.example.kcs.Fragment.Profile.ProfileFragment;
import com.example.kcs.ViewModel.GetViewModel;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private GetViewModel getViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViewModel = new ViewModelProvider(this).get(GetViewModel.class);
        MyLog.e(TAG, "logout>> main activity ");

        getViewModel.setI_value(0);
        getViewModel.getValue().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                MyLog.e(TAG, "integer>>" + integer);
                Fragment fragment = new Fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();


                switch (integer) {
                    case 0:
                        fragment = new HomeFragment();
                        fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
                        break;
                    case 1:
                        //MyLog.e(TAG, "Data>>fun list>>" + new GsonBuilder().setPrettyPrinting().create().toJson(myViewModel.getHeaderLists()));
                        fragment = new HeaderFragment();
                        fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
                        break;
                    case 2:
                        //MyLog.e(TAG, "Data>>header list>>" + new GsonBuilder().setPrettyPrinting().create().toJson(myViewModel.getItemLists()));

                        fragment = new ItemFragment();
                        fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
                        break;
                    case 3:
                        fragment = new ProfileFragment();
                        fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
                        break;
                    case 4:
                        fragment = new MyOrdersFragment();
                        fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
                        break;
                    default:
                        fragment = new HomeFragment();
                        fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
                        break;


                }

            }
        });


    }
}
package com.example.kcs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Header.HeaderFragment;
import com.example.kcs.Fragment.HomeFragment;
import com.example.kcs.Fragment.Items.ItemFragment;
import com.example.kcs.Fragment.MyViewModel;
import com.example.kcs.Fragment.ProfileFragment;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        MyLog.e(TAG, "logout>> main activity ");
        /*Fragment fragment = new HomeFragment();*/
        myViewModel.setI_value(0);
        MyLog.e(TAG, "integer>>" + myViewModel.getI_value());
        myViewModel.getValue().observe(this, new Observer<Integer>() {
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
                        fragment = new HeaderFragment((myViewModel.getFunList()), (myViewModel.getHeaderLists()), (myViewModel.getGetHeaderFragment()));
                        fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
                        break;
                    case 2:
                        //MyLog.e(TAG, "Data>>header list>>" + new GsonBuilder().setPrettyPrinting().create().toJson(myViewModel.getItemLists()));
                        fragment = new ItemFragment((myViewModel.getHeaderList()), (myViewModel.getItemLists()));
                        fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
                        break;
                    case 3:
                        fragment = new ProfileFragment();
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
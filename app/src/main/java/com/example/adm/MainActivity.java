package com.example.adm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.Control_Panel.Control_PanelFragment;
import com.example.adm.Fragments.Control_Panel.Item.ItemFragment;
import com.example.adm.Fragments.HomeFragment;
import com.example.adm.Fragments.ProfileFragment;
import com.example.adm.ViewModel.GetViewModel;


public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private GetViewModel getViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViewModel = new ViewModelProvider(this).get(GetViewModel.class);

        MyLog.e(TAG, "logout>> main activity ");

        //load data base
        getViewModel.GetOrdesList();
        getViewModel.GetUserList();
        //getViewModel.GetSessionList();
        /*getViewModel.GetHeader();
        getViewModel.GetFun();
        getViewModel.GetItem();*/
        getViewModel.GetUpdateHeader();
        getViewModel.GetUpdateFun();
        getViewModel.GetUpdateItem();


        getViewModel.setI_value(0);
        //get value to pass fragment
        getViewModel.getValue().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                MyLog.e(TAG, "integer>>" + integer);
                Fragment fragment = new Fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = fragmentManager.beginTransaction();

                String fragmentTAg = "";

                switch (integer) {
                    case 0:
                        fragment = new HomeFragment();

                        fragmentTAg = "HomeFragment";
                        break;
                    case 1:
                        fragment = new Control_PanelFragment();

                        fragmentTAg = "Control_PanelFragment";
                        break;
                    case 2:
                        fragment = new ProfileFragment();
                        fragmentTAg = "ProfileFragment";
                        break;
                    case 3:
                        fragment =new ItemFragment();
                        fragmentTAg="ItemFragment";
                        break;
                }
                fragmentTransaction.replace(R.id.Fragment, fragment);
                fragmentTransaction.addToBackStack(fragmentTAg);
                fragmentTransaction.commit();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            ///val done=supportFragmentManager.popBackStackImmediate()
            MyLog.i(TAG, "onBackPressedAct:onBackPressed pop:");
            //super.onBackPressed();
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
            MyLog.i(TAG, "onBackPressedAct:onBackPressed in:");
            //snack

        }

    }
}
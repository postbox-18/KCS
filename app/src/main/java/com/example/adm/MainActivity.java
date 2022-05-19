package com.example.adm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.adm.Classes.MyLog;
import com.example.adm.Fragments.HomeFragment;


public class MainActivity extends AppCompatActivity {
    private String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyLog.e(TAG,"logout>> main activity ");
        Fragment fragment=new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();




    }
}
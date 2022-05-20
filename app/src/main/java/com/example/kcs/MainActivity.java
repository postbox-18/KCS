package com.example.kcs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Header.HeaderFragment;
import com.example.kcs.Fragment.HomeFragment;
import com.example.kcs.Fragment.Items.ItemFragment;
import com.example.kcs.Fragment.MyViewModel;
import com.example.kcs.Fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private String TAG="MainActivity";
    private MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyLog.e(TAG,"logout>> main activity ");
        /*Fragment fragment = new HomeFragment();*/
        myViewModel.setI_value(0);

        myViewModel.getValue().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Fragment fragment=new Fragment();
                FragmentManager fragmentManager = fragment.getParentFragmentManager();
                switch (integer)
              {

                  case 0:
                      fragment=new HomeFragment();
                      fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
                      break;
                  case 1:
                      fragment=new HeaderFragment();
                      fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
                      break;
                  case 2:
                      fragment=new ItemFragment();
                      fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();
                      break;

              }

            }
        });




    }
}
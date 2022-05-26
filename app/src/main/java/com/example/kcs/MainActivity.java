package com.example.kcs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Header.HeaderFragment;
import com.example.kcs.Fragment.Header.HeaderList;
import com.example.kcs.Fragment.HomeFragment;
import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.Fragment.Items.ItemFragment;
import com.example.kcs.Fragment.Items.ItemSelectedList.UserItemList;
import com.example.kcs.Fragment.Items.ItemSelectedList.UserItemListAdapters;
import com.example.kcs.Fragment.Profile.MyOrders.MyOrdersFragment;
import com.example.kcs.Fragment.Profile.ProfileFragment;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private GetViewModel getViewModel;

    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //snack bar
    private RecyclerView recyclerview_selected_count;
    private AppCompatButton order_btn, cancel_btn;
    private List<LinkedHashMap<String, List<CheckedList>>> linkedHashMaps = new ArrayList<>();
    private List<UserItemList> userItemLists = new ArrayList<>();
    private UserItemListAdapters userItemListAdapters;
    private String headerList_title, func_title;
    private List<CheckedList> checkedLists = new ArrayList<>();
    private List<FunList> funLists = new ArrayList<>();
    private List<HeaderList> headerLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View parentLayout = findViewById(android.R.id.content);

        MyLog.e(TAG, "logout>> main activity ");

        getViewModel = new ViewModelProvider(this).get(GetViewModel.class);

        firebaseDatabase = FirebaseDatabase.getInstance();

        //get header title
        getViewModel.getHeader_title_Mutable().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                headerList_title = s;
            }
        });

        //get func title
        getViewModel.getFunc_title_Mutable().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                func_title = s;
            }
        });
        //get header list
        getViewModel.getHeaderListMutableList().observe(this, new Observer<List<HeaderList>>() {
            @Override
            public void onChanged(List<HeaderList> headerLists1) {
                headerLists=headerLists1;
            }
        });

        //snack bar
        final Snackbar snackbar = Snackbar.make(parentLayout, "", Snackbar.LENGTH_INDEFINITE);
        View customSnackView = getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.addView(customSnackView, 0);
        snackbarLayout.setPadding(0, 0, 0, 0);
        cancel_btn = customSnackView.findViewById(R.id.cancel_btn);
        order_btn = customSnackView.findViewById(R.id.order_btn);
        recyclerview_selected_count = customSnackView.findViewById(R.id.recyclerview_selected_count);


        recyclerview_selected_count.setHasFixedSize(true);
        recyclerview_selected_count.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));


        //get hash map checked list
        getViewModel.getCheck_s_mapMutable().observe(this, new Observer<List<LinkedHashMap<String, List<CheckedList>>>>() {
            @Override
            public void onChanged(List<LinkedHashMap<String, List<CheckedList>>> linkedHashMaps1) {
                linkedHashMaps = linkedHashMaps1;
                //MyLog.e(TAG, "s_map>>" + new GsonBuilder().setPrettyPrinting().create().toJson(linkedHashMaps));
                List<CheckedList> list = new ArrayList<>();
               // MyLog.e(TAG, "maps>>s_map>>" + new GsonBuilder().setPrettyPrinting().create().toJson(linkedHashMaps));
                MyLog.e(TAG, "maps>>title>>" +headerList_title);
                MyLog.e(TAG,"chs>>before"+ new GsonBuilder().setPrettyPrinting().create().toJson(userItemLists));
                for (int k = 0; k < linkedHashMaps.size(); k++) {
                    list = linkedHashMaps.get(k).get(headerList_title);
                    if (list != null) {
                        //MyLog.e(TAG, "maps>>checked list>>" + new GsonBuilder().setPrettyPrinting().create().toJson(list));
                        UserItemList userItemList = new UserItemList(
                                headerList_title,
                                list.size()
                        );
                        userItemLists.add(userItemList);
                        getViewModel.setUserItemLists(userItemLists);
                        if (userItemList.getList_size() > 0) {
                            snackbar.show();
                        } else {
                            snackbar.dismiss();
                        }
                    } else {
                        MyLog.e(TAG, "maps>>checked list is null");
                    }
                }
                MyLog.e(TAG,"chs>>after"+ new GsonBuilder().setPrettyPrinting().create().toJson(userItemLists));


                }
                // MyLog.e(TAG, "items>>userList>>" + new GsonBuilder().setPrettyPrinting().create().toJson(userItemLists));


        });

        //get user-item list
        getViewModel.getUserItemListsMutableLiveData().observe(this, new Observer<List<UserItemList>>() {
            @Override
            public void onChanged(List<UserItemList> userItemLists) {

                userItemListAdapters = new UserItemListAdapters(getApplication(), getViewModel, userItemLists);
                recyclerview_selected_count.setAdapter(userItemListAdapters);
            }
        });


        //checked list
        getViewModel.getCheckedList_Mutable().observe(this, new Observer<List<CheckedList>>() {
            @Override
            public void onChanged(List<CheckedList> checkedLists1) {
                checkedLists = checkedLists1;


                snackbar.show();
                //order btn
                order_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkedLists.size() > 0) {
                            GetOrderBtn(checkedLists, func_title);
                        } else {
                            Toast.makeText(MainActivity.this, "Empty List", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });


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
                        //MyLog.e(TAG, "Data>>fun list>>" + new GsonBuilder().setPrettyPrinting().create().toJson(myViewModel.getHeaderLists()));
                        fragment = new HeaderFragment();

                        fragmentTAg = "HeaderFragment";
                        break;
                    case 2:
                        //MyLog.e(TAG, "Data>>header list>>" + new GsonBuilder().setPrettyPrinting().create().toJson(myViewModel.getItemLists()));
                        fragment = new ItemFragment();

                        fragmentTAg = "ItemFragment";
                        break;
                    case 3:
                        fragment = new ProfileFragment();

                        fragmentTAg = "ProfileFragment";
                        break;
                    case 4:
                        fragment = new MyOrdersFragment();

                        fragmentTAg = "MyOrdersFragment";
                        break;
                    default:
                        fragment = new HomeFragment();

                        fragmentTAg = "HomeFragment";
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
        /*if (supportFragmentManager.backStackEntryCount > 0) {
///val done=supportFragmentManager.popBackStackImmediate()
MyLog.i(TAG, "onBackPressedAct:onBackPressed pop:")
super.onBackPressed()
} else {

//snack

}*/
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

    private void GetOrderBtn(List<CheckedList> checkedLists, String func_title) {

        //get func list
        getViewModel.getFunMutableList().observe(this, new Observer<List<FunList>>() {
            @Override
            public void onChanged(List<FunList> funLists1) {
                funLists = funLists1;
                MyLog.e(TAG, "fun>>in" + funLists.size());
            }
        });

        //fragment
        getViewModel.getI_fragmentMutable().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                MyLog.e(TAG, "int>>" + integer);
                if (integer == 1) {
                    MyLog.e(TAG, "title>>out" + func_title);
                    SaveOrders(func_title);
                } else {
                    MyLog.e(TAG, "fun>>out" + funLists.size());
                    showAlertDialog(funLists);

                }

            }
        });

    }

    private void SaveOrders(String func_title) {
        databaseReference = firebaseDatabase.getReference("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user_name = new SharedPreferences_data(getApplication()).getS_user_name();

                for (int i = 0; i < checkedLists.size(); i++) {
                    //getFunc
                    databaseReference.child(user_name).child(func_title).child(headerList_title).child(String.valueOf(i)).setValue(checkedLists.get(i).getItemList());
                }


                Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(MainActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog(List<FunList> funLists) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplication());
        alertDialog.setTitle("Please select Function Type!");
        String[] str = new String[funLists.size()];
        for (int i = 0; i < funLists.size(); i++) {
            str[i] = funLists.get(i).getFun();
        }
        MyLog.e(TAG, "dialog>>" + str);
        //String[] items = {"java","android","Data Structures","HTML","CSS"};
        boolean[] checkedItems = {false, false, false, false, false, false};
        alertDialog.setMultiChoiceItems(str, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                for (int i = 0; i < funLists.size(); i++) {
                    if (which == i) {
                        getViewModel.setFunc_title(str[i]);
                        dialog.dismiss();
                        SaveOrders(str[i]);
                        break;
                    } else {
                        continue;
                    }
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}
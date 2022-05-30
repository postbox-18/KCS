package com.example.kcs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.example.kcs.Fragment.PlaceOrders.PlaceOrderFragment;
import com.example.kcs.Fragment.PlaceOrders.SelectedHeader;
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
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private GetViewModel getViewModel;

    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //snack bar
    private RecyclerView recyclerview_selected_count;
    //private AppCompatButton order_btn, cancel_btn;
    //private List<LinkedHashMap<String, List<CheckedList>>> linkedHashMaps = new ArrayList<>();
    private LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap = new LinkedHashMap<>();
    private List<UserItemList> userItemLists = new ArrayList<>();
    private List<SelectedHeader> selectedHeadersList = new ArrayList<>();
    private UserItemListAdapters userItemListAdapters;
    private String headerList_title, func_title;
    private List<CheckedList> checkedLists = new ArrayList<>();
    private List<FunList> funLists = new ArrayList<>();
    private List<HeaderList> headerLists = new ArrayList<>();
    private CardView view_cart_cardView;
    private String user_name;
    private Integer integer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View parentLayout = findViewById(android.R.id.content);

        MyLog.e(TAG, "logout>> main activity ");

        getViewModel = new ViewModelProvider(this).get(GetViewModel.class);

        firebaseDatabase = FirebaseDatabase.getInstance();

        user_name = new SharedPreferences_data(getApplication()).getS_user_name();
        //get MyOrder Details
        getViewModel.GetMyOrdersDetails(user_name);

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
                headerLists = headerLists1;
            }
        });

        //snack bar
        final Snackbar snackbar = Snackbar.make(parentLayout, "", Snackbar.LENGTH_INDEFINITE);
        View customSnackView = getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.addView(customSnackView, 0);
        snackbarLayout.setPadding(0, 0, 0, 0);


        view_cart_cardView = customSnackView.findViewById(R.id.view_cart_cardView);
        recyclerview_selected_count = customSnackView.findViewById(R.id.recyclerview_selected_count);


        recyclerview_selected_count.setHasFixedSize(true);
        recyclerview_selected_count.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));

        //linked hash map of checked list
        getViewModel.getF_mapMutable().observe(this, new Observer<LinkedHashMap<String, List<CheckedList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap1) {
                stringListLinkedHashMap = stringListLinkedHashMap1;
                MyLog.e(TAG, "chs>>keyset>>" + stringListLinkedHashMap.keySet());
                //MyLog.e(TAG,"f_map main>>"+ new GsonBuilder().setPrettyPrinting().create().toJson(stringListLinkedHashMap));
                //MyLog.e(TAG,"chs>>before"+ new GsonBuilder().setPrettyPrinting().create().toJson(userItemLists));
                Set<String> stringSet = stringListLinkedHashMap.keySet();
                List<String> aList = new ArrayList<String>(stringSet.size());
                for (String x : stringSet)
                    aList.add(x);
                //MyLog.e(TAG,"chs>>list "+ new GsonBuilder().setPrettyPrinting().create().toJson(aList));

                //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                userItemLists.clear();
                for (int i = 0; i < aList.size(); i++) {
                    MyLog.e(TAG, "chs>>list header>> " + aList.get(i));
                    MyLog.e(TAG, "chs>>list size " + stringListLinkedHashMap.get(aList.get(i)).size());
                    UserItemList userItemList = new UserItemList(
                            aList.get(i),
                            stringListLinkedHashMap.get(aList.get(i)).size()
                    );
                    userItemLists.add(userItemList);
                }
                //MyLog.e(TAG,"chs>>after"+ new GsonBuilder().setPrettyPrinting().create().toJson(userItemLists));
                getViewModel.setUserItemLists(userItemLists);
                if (userItemLists.size() > 0) {
                    snackbar.show();
                } else {
                    snackbar.dismiss();
                }


            }
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
               /* order_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkedLists.size() > 0) {
                            GetOrderBtn(checkedLists, func_title);
                        } else {
                            Toast.makeText(MainActivity.this, "Empty List", Toast.LENGTH_SHORT).show();
                        }

                    }
                });*/

                //get View Cart Btn details
                //get func list
                getViewModel.getFunMutableList().observe(MainActivity.this, new Observer<List<FunList>>() {
                    @Override
                    public void onChanged(List<FunList> funLists1) {
                        funLists = funLists1;
                        MyLog.e(TAG, "fun>>in" + funLists.size());
                    }
                });

                //get linked hasp map to view item list
                //get Checked list hash map
                getViewModel.getF_mapMutable().observe(MainActivity.this, new Observer<LinkedHashMap<String, List<CheckedList>>>() {
                    @Override
                    public void onChanged(LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap1) {
                        //MyLog.e(TAG, "cart>>f_map>>before>>" + new GsonBuilder().setPrettyPrinting().create().toJson(stringListLinkedHashMap));
                        stringListLinkedHashMap = stringListLinkedHashMap1;

                        //MyLog.e(TAG, "cart>>list " + new GsonBuilder().setPrettyPrinting().create().toJson(selectedHeadersList));

                    }
                });


                //which fragment going to pass
                getViewModel.getI_fragmentMutable().observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer1) {
                        integer = integer1;

                    }
                });

                view_cart_cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        //get func title is selected
                        GetFuncTitleList(funLists, stringListLinkedHashMap, integer);

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
                    case 5:
                        fragment = new PlaceOrderFragment();
                        fragmentTAg = "OrderFragment";
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

    private void GetFuncTitleList(List<FunList> funLists, LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap, Integer integer) {

        //get linked hasp map to view item list
        Set<String> stringSet = stringListLinkedHashMap.keySet();

        for (String a : stringSet) {
            SelectedHeader aList = new SelectedHeader(
                    a
            );
            selectedHeadersList.add(aList);

        }
        getViewModel.setSelectedHeadersList(selectedHeadersList);
        MyLog.e(TAG, "integer>>int>>" + integer);
        if (integer == 1) {
            MyLog.e(TAG, "integer>>title>>out>>" + func_title);
            getViewModel.setI_value(5);
        } else {
            MyLog.e(TAG, "integer>>fun>>out>>" + funLists.size());
            showAlertDialog(funLists);
            MyLog.e(TAG, "integer>>fun_list" + new GsonBuilder().setPrettyPrinting().create().toJson(funLists));
        }


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


    private void showAlertDialog(List<FunList> funLists) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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
                        getViewModel.setI_value(5);
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
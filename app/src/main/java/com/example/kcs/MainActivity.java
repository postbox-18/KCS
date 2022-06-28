package com.example.kcs;

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
import android.widget.LinearLayout;

import com.example.kcs.BreadCrumbs.BreadCrumbList;
import com.example.kcs.BreadCrumbs.BreadCrumbsAdapter;
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
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.OrderItemLists;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrdersFragment;
import com.example.kcs.Fragment.Settings.Profile.ProfileFragment;
import com.example.kcs.Fragment.Session.SessionFragment;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    //Bread Crumbs
    private LinearLayout breadCrums;
    private RecyclerView recyclerview_breadcrumbs;
    private BreadCrumbsAdapter breadCrumbsAdapter;
    private List<BreadCrumbList> breadcrumbsList = new ArrayList<>();

    //snack bar
    private RecyclerView recyclerview_selected_count;
    //private AppCompatButton order_btn, cancel_btn;
    //private List<LinkedHashMap<String, List<CheckedList>>> linkedHashMaps = new ArrayList<>();
    private LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap = new LinkedHashMap<>();
    private List<UserItemList> userItemLists = new ArrayList<>();
    private List<SelectedHeader> selectedHeadersList = new ArrayList<>();
    private UserItemListAdapters userItemListAdapters;
    private String headerList_title, func_title, session_title, date;
    private List<CheckedList> checkedLists = new ArrayList<>();
    private List<FunList> funLists = new ArrayList<>();
    private List<HeaderList> headerLists = new ArrayList<>();
    private CardView view_cart_cardView;
    private String user_name;
    private Integer integer, frag_integer = -1;
    //header map
    private LinkedHashMap<String, List<CheckedList>> headerMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> sessionMap = new LinkedHashMap<>();
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> funcMap = new LinkedHashMap<>();
    private List<SelectedSessionList> selectedSessionLists = new ArrayList<>();
    private String date_time;
    //order func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>>> orderFunc_Map = new LinkedHashMap<>();
    //edit hash map
    //edit hash map list
    private List<SessionList> e_sessionLists = new ArrayList<>();
    private List<SelectedHeader> e_selectedHeaders = new ArrayList<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> editFunc_Map = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editDateMap = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> editSessionMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<SelectedHeader>> editHeaderMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View parentLayout = findViewById(android.R.id.content);
        recyclerview_breadcrumbs = findViewById(R.id.recyclerview_breadcrumbs);
        breadCrums = findViewById(R.id.breadCrums);

        MyLog.e(TAG, "logout>> main activity ");
        getViewModel = new ViewModelProvider(this).get(GetViewModel.class);
        firebaseDatabase = FirebaseDatabase.getInstance();
        user_name = new SharedPreferences_data(getApplication()).getS_user_name();

        //to load data base
        getViewModel.GetHeader();
        getViewModel.GetFun();
        getViewModel.GetSession();
        getViewModel.GetItem();
        getViewModel.GetImg();
        getViewModel.GetSessionTime();


        recyclerview_breadcrumbs.setHasFixedSize(true);
        recyclerview_breadcrumbs.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));


        //get breadcrumbs list
        getViewModel.getBreadCrumbListsMutableLiveData().observe(this, new Observer<List<BreadCrumbList>>() {
            @Override
            public void onChanged(List<BreadCrumbList> breadCrumbLists) {
                breadcrumbsList = breadCrumbLists;
                if (breadcrumbsList != null) {
                    breadCrumbsAdapter = new BreadCrumbsAdapter(getApplicationContext(), getViewModel, breadcrumbsList);
                    recyclerview_breadcrumbs.setAdapter(breadCrumbsAdapter);
                }
            }
        });


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

        //get session_title
        getViewModel.getSession_titleMutable().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                session_title = s;

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


        //get func hash map
        getViewModel.getEditFuncMapMutableLiveData().observe(this, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> stringLinkedHashMapLinkedHashMap) {
                editFunc_Map = stringLinkedHashMapLinkedHashMap;
            }
        });

        //get time picker
        getViewModel.getDate_pickerMutable().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                date = s;
            }
        });
        //get selected session list
        getViewModel.getSelectedSessionListsMutableLiveData().observe(this, new Observer<List<SelectedSessionList>>() {
            @Override
            public void onChanged(List<SelectedSessionList> selectedSessionLists1) {
                selectedSessionLists = selectedSessionLists1;


                for (int k = 0; k < selectedSessionLists.size(); k++) {
                    if (session_title == null) {

                        MyLog.e(TAG, "placeorder>>get funcMap>>" + func_title);
                        editDateMap = editFunc_Map.get(func_title);
                        editSessionMap = editDateMap.get(date);
                        MyLog.e(TAG, "placeorder>>get sessionMap>>" + session_title);
                        date_time = selectedSessionLists.get(k).getSession_title() + "!" + (selectedSessionLists.get(k).getTime());
                        editHeaderMap = editSessionMap.get(date_time);

                        Set<String> stringSet = editHeaderMap.keySet();
                        List<String> aList = new ArrayList<String>(stringSet.size());
                        for (String x : stringSet)
                            aList.add(x);

                        //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                        userItemLists.clear();
                        for (int i = 0; i < aList.size(); i++) {
                            MyLog.e(TAG, "chs>>list header>> " + aList.get(i));
                            MyLog.e(TAG, "chs>>list size " + editHeaderMap.get(aList.get(i)).size());
                            UserItemList userItemList = new UserItemList(
                                    aList.get(i),
                                    editHeaderMap.get(aList.get(i)).size()
                            );
                            userItemLists.add(userItemList);
                        }
                        getViewModel.setUserItemLists(userItemLists);
                        MyLog.e(TAG, "chs>>list header>> " + userItemLists.size());
                        /*if (userItemLists.size() > 0) {
                            snackbar.show();
                            MyLog.e(TAG, "chs>>snackbar Show");
                        } else {
                            snackbar.dismiss();
                        }*/
                        break;
                    } else {
                        if (session_title.equals(selectedSessionLists.get(k).getSession_title())) {
                            MyLog.e(TAG, "placeorder>>get funcMap>>" + func_title);
                            sessionMap = funcMap.get(func_title);
                            MyLog.e(TAG, "placeorder>>get sessionMap>>" + session_title);
                            MyLog.e(TAG, "orders>>sess>>" + selectedSessionLists.get(k).getSession_title());
                            String[] str = (selectedSessionLists.get(k).getSession_title()).split("!");
                            date_time = str[0] + "!" + (selectedSessionLists.get(k).getTime());
                            MyLog.e(TAG, "placeorder>>get date_time>>" + date_time);
                            headerMap = sessionMap.get(date_time);

                            Set<String> stringSet = headerMap.keySet();
                            List<String> aList = new ArrayList<String>(stringSet.size());
                            for (String x : stringSet)
                                aList.add(x);

                            //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                            userItemLists.clear();
                            for (int i = 0; i < aList.size(); i++) {
                                MyLog.e(TAG, "chs>>list header>> " + aList.get(i));
                                MyLog.e(TAG, "chs>>list size " + headerMap.get(aList.get(i)).size());
                                UserItemList userItemList = new UserItemList(
                                        aList.get(i),
                                        headerMap.get(aList.get(i)).size()
                                );
                                userItemLists.add(userItemList);
                            }
                            getViewModel.setUserItemLists(userItemLists);
                            MyLog.e(TAG, "chs>>list header>> " + userItemLists.size());
                            if (userItemLists.size() > 0) {
                                snackbar.show();
                                MyLog.e(TAG, "chs>>snackbar Show");
                            } else {
                                snackbar.dismiss();
                            }
                            break;
                        } else {
                            continue;
                        }
                    }
                }
            }
        });

        //func map
        getViewModel.getFuncMapMutableLiveData().observe(this, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> stringLinkedHashMapLinkedHashMap) {
                funcMap = stringLinkedHashMapLinkedHashMap;


            }
        });

        /*getViewModel.getF_mapMutable().observe(this, new Observer<LinkedHashMap<String, List<CheckedList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<CheckedList>> stringListLinkedHashMap1) {
                stringListLinkedHashMap = stringListLinkedHashMap1;
                MyLog.e(TAG, "chs>>keyset>>" + stringListLinkedHashMap.keySet());

                Set<String> stringSet = stringListLinkedHashMap.keySet();
                List<String> aList = new ArrayList<String>(stringSet.size());
                for (String x : stringSet)
                    aList.add(x);

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
                getViewModel.setUserItemLists(userItemLists);
                MyLog.e(TAG, "chs>>list header>> " + userItemLists.size());
                if (userItemLists.size() > 0) {
                    snackbar.show();
                } else {
                    snackbar.dismiss();
                }


            }
        });*/


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
                        stringListLinkedHashMap = stringListLinkedHashMap1;


                    }
                });


                Set<String> stringSet = headerMap.keySet();
                List<String> aList = new ArrayList<String>(stringSet.size());
                for (String x : stringSet)
                    aList.add(x);

                //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                selectedHeadersList.clear();
                for (int i = 0; i < aList.size(); i++) {
                    MyLog.e(TAG, "chs>>list header>> " + aList.get(i));
                    SelectedHeader list = new SelectedHeader(
                            aList.get(i)

                    );
                    selectedHeadersList.add(list);
                }
                getViewModel.setSelectedHeadersList(selectedHeadersList);


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
                        //GetFuncTitleList(funLists, stringListLinkedHashMap, integer);
                        MyLog.e(TAG, "integer>>int>>" + integer);
                        if (integer == 1) {
                            MyLog.e(TAG, "integer>>title>>out>>" + func_title);
                            getViewModel.setI_value(5);
                        } else {
                            MyLog.e(TAG, "integer>>fun>>out>>" + funLists.size());
                            showAlertDialog(funLists);
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
                frag_integer = integer;
                //breadCrums
                //breadCrums
                if (integer == 0) {
                    breadCrums.setVisibility(View.GONE);
                } else {
                    breadCrums.setVisibility(View.VISIBLE);

                }

                if (integer == 4) {
                    orderFunc_Map.clear();
                    getViewModel.setOrderFunc_Map(orderFunc_Map);
                    breadCrums.setVisibility(View.GONE);
                    snackbar.dismiss();
                } else {
                    breadCrums.setVisibility(View.VISIBLE);
                }
                /*if (integer == 1) {
                    editFunc_Map.clear();
                    getViewModel.setEditFuncMap(editFunc_Map);
                }*/


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
                        fragment = new HeaderFragment();
                        fragmentTAg = "HeaderFragment";
                        break;
                    case 2:
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
                        fragmentTAg = "PlaceOrderFragment";
                        break;
                    case 6:
                        fragment = new SessionFragment();
                        fragmentTAg = "SessionFragment";
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            ///val done=supportFragmentManager.popBackStackImmediate()
            if (breadcrumbsList != null && breadcrumbsList.size() > 0) {
                breadcrumbsList.remove(breadcrumbsList.size() - 1);
                getViewModel.setBreadCrumbLists(breadcrumbsList);
                breadCrums.setVisibility(View.VISIBLE);
            } else {
                breadCrums.setVisibility(View.GONE);
            }
            //super.onBackPressed();
            getSupportFragmentManager().popBackStackImmediate();
        } else {

            super.onBackPressed();
            MyLog.e(TAG, "breadcrumbs>>onBackPressedAct:onBackPressed in:");
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
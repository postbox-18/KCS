package com.example.kcs.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.kcs.BreadCrumbs.BreadCrumbList;
import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Header.HeaderList;
import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.Fragment.Items.ItemList;
import com.example.kcs.Fragment.Items.ItemSelectedList.UserItemList;
import com.example.kcs.Fragment.PlaceOrders.SelectedHeader;
import com.example.kcs.Fragment.Profile.MyOrders.BottomSheet.OrderItemLists;
import com.example.kcs.Fragment.Profile.MyOrders.MyOrdersList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetViewModel extends AndroidViewModel {
    //Header List
    private MutableLiveData<List<HeaderList>> headerListMutableList = new MutableLiveData<>();
    private List<HeaderList> headerList = new ArrayList<>();

    //func List
    private MutableLiveData<List<FunList>> funMutableList = new MutableLiveData<>();
    private List<FunList> funLists = new ArrayList<>();

    //item list
    private MutableLiveData<List<ItemList>> itemMutable = new MutableLiveData<>();
    private List<ItemList> itemLists = new ArrayList<>();


    //item list
    private MutableLiveData<List<SessionList>> sessionListMutable = new MutableLiveData<>();
    private List<SessionList> sessionList = new ArrayList<>();


    //checked list
    private MutableLiveData<List<CheckedList>> checkedList_Mutable = new MutableLiveData<>();
    private List<CheckedList> checkedLists = new ArrayList<>();

    //Linked HashMap item checked list
    private LinkedHashMap<String, List<CheckedList>> f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<CheckedList>>> f_mapMutable = new MutableLiveData<>();

    // item checked list Linked HashMap
    private List<LinkedHashMap<String, List<CheckedList>>> check_s_map = new ArrayList<>();
    private MutableLiveData<List<LinkedHashMap<String, List<CheckedList>>>> check_s_mapMutable = new MutableLiveData<>();

    //item list-get header
    private MutableLiveData<List<ItemList>> itemHeaderMutable = new MutableLiveData<>();
    private List<ItemList> itemHeaderLists = new ArrayList<>();

    //Linked HashMap item list
    private LinkedHashMap<String, List<ItemList>> f_maps = new LinkedHashMap<>();
    private List<LinkedHashMap<String, List<ItemList>>> s_map = new ArrayList<>();
    private MutableLiveData<List<LinkedHashMap<String, List<ItemList>>>> s_mapMutable = new MutableLiveData<>();

    //check user login
    private MutableLiveData<Boolean> EmailMutable = new MutableLiveData<>();
    private String email;
    private boolean check_email = false;

    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    //Session Fragment title
    private String session_title;
    private MutableLiveData<String> session_titleMutable = new MutableLiveData<>();

    //Selected Header title
    private String header_title;
    private MutableLiveData<String> header_title_Mutable = new MutableLiveData<>();

    //Selected Func
    private String func_title;
    private MutableLiveData<String> func_title_Mutable = new MutableLiveData<>();

    //Fragment to pass
    private Integer i_value;
    private MutableLiveData<Integer> value = new MutableLiveData<>();

    //item Fragment
    private Integer i_fragment;
    private MutableLiveData<Integer> i_fragmentMutable = new MutableLiveData<>();

    //user selected list
    private List<UserItemList> userItemLists = new ArrayList<>();
    private MutableLiveData<List<UserItemList>> userItemListsMutableLiveData = new MutableLiveData<>();

    //my orders list
    private List<MyOrdersList> myOrdersList = new ArrayList<>();
    private MutableLiveData<List<MyOrdersList>> myOrdersListsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<LinkedHashMap<String, List<MyOrdersList>>> myordersHashMapMutable = new MutableLiveData<>();
    private LinkedHashMap<String, List<MyOrdersList>> f_mapMyorders = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<MyOrdersList>>> f_mapMyordersMutableLiveData = new MutableLiveData<>();
    private List<MyOrderFuncList> myOrderFuncLists = new ArrayList<>();
    private MutableLiveData<List<MyOrderFuncList>> myOrderFuncListsMutableLiveData = new MutableLiveData<>();
    private String func;

    //selected header and item list to view list
    private List<SelectedHeader> selectedHeadersList = new ArrayList<>();
    private MutableLiveData<List<SelectedHeader>> selectedHeadersListMutableLiveData = new MutableLiveData<>();

    //selected Item list
    private List<OrderItemLists> orderItemLists = new ArrayList<>();
    private MutableLiveData<List<OrderItemLists>> orderItemListsMutableLiveData = new MutableLiveData<>();
    private LinkedHashMap<String, List<OrderItemLists>> orderItemList_f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<OrderItemLists>>> orderItemList_f_mapMutableLiveData = new MutableLiveData<>();


    //breadcrumbs
    private List<BreadCrumbList> breadCrumbLists = new ArrayList<>();
    private MutableLiveData<List<BreadCrumbList>> breadCrumbListsMutableLiveData = new MutableLiveData<>();
    private String TAG = "ViewClassModel";


    public GetViewModel(@NonNull Application application) {
        super(application);
        String s_user_name = new SharedPreferences_data(application).getS_user_name();

        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        GetHeader();
        GetFun();
        GetSession();
        GetItem();
        GetMyOrdersDetails(s_user_name);

    }

    public MutableLiveData<LinkedHashMap<String, List<MyOrdersList>>> getF_mapMyordersMutableLiveData() {
        return f_mapMyordersMutableLiveData;
    }

    public void setBreadCrumbLists(List<BreadCrumbList> breadCrumbLists) {
        this.breadCrumbLists = breadCrumbLists;
        this.breadCrumbListsMutableLiveData.postValue(breadCrumbLists);
    }

    public MutableLiveData<List<SessionList>> getSessionListMutable() {
        return sessionListMutable;
    }

    public MutableLiveData<List<BreadCrumbList>> getBreadCrumbListsMutableLiveData() {
        return breadCrumbListsMutableLiveData;
    }

    public void setSession_title(String session_title) {
        this.session_title = session_title;
        this.session_titleMutable.postValue(session_title);
    }

    public MutableLiveData<String> getSession_titleMutable() {
        return session_titleMutable;
    }

    private void GetSession() {
        databaseReference = firebaseDatabase.getReference("Items").child("Session");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "Session>>snap>>fun>>" + snapshot);
                int size = 0;
                sessionList = new ArrayList<>();
                for (DataSnapshot datas : snapshot.getChildren()) {
                    String path = "" + size;
                    MyLog.e(TAG, "Session>>snap>>fun>>" + path);
                    //MyLog.e(TAG, "home>>snap>>fun>>" +  datas.child("0").getValue().toString());
                    SessionList sessionList1 = new SessionList(
                            datas.getValue().toString());
                    sessionList.add(sessionList1);
                    size++;

                }
                sessionListMutable.postValue(sessionList);
                //MyLog.e(TAG,"session>>session_list>>"+new GsonBuilder().setPrettyPrinting().create().toJson(sessionList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "home>>snap>>fun>>Fail to get data.");
            }
        });
    }


    public void GetMyOrdersDetails(String s_user_name) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders").child(s_user_name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = 0;
                MyLog.e(TAG, "onData>>snapshot>>" + snapshot);
                myOrderFuncLists = new ArrayList<>();
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "onData>>datas>>" + datas);
                    MyLog.e(TAG, "onData>>func>>" + datas.getKey().toString());
                    func = datas.getKey().toString();
                    sessionList=new ArrayList<>();
                    for (DataSnapshot ondata : datas.getChildren()) {
                        session_title=ondata.getKey().toString();
                        myOrdersList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : ondata.getChildren()) {

                            header_title = dataSnapshot.getKey().toString();
                            MyLog.e(TAG, "onData>>dataonData>>" + dataSnapshot);
                            MyLog.e(TAG, "onData>>header_title>>" + dataSnapshot.getKey().toString());
                            size = 0;
                            for (DataSnapshot shot : dataSnapshot.getChildren()) {
                                MyLog.e(TAG, "onData>>shots>>" + shot);
                                MyLog.e(TAG, "onData>>shots>>" + shot.getKey().toString());
                                size++;
                            }
                            MyLog.e(TAG, "onData>>size>" + size);
                            MyOrdersList itemList = new MyOrdersList(
                                    header_title,
                                    size
                            );

                            myOrdersList.add(itemList);
                            String s=func+"-"+session_title;
                            f_mapMyorders.put(s, myOrdersList);
                            myordersHashMapMutable.postValue(f_mapMyorders);

                        }
                        SessionList sessionList1=new SessionList(
                                session_title
                        );
                        sessionList.add(sessionList1);
                    }


                    //get func name into list
                    MyOrderFuncList list = new MyOrderFuncList(
                            func
                    );
                    myOrderFuncLists.add(list);
                }
                //set func list
                myOrderFuncListsMutableLiveData.postValue(myOrderFuncLists);
                //set session list
                sessionListMutable.postValue(sessionList);
                //set f_mpa of my orders
                f_mapMyordersMutableLiveData.postValue(f_mapMyorders);
                MyLog.e(TAG, "selected_list>>f_map>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(f_mapMyorders));
                MyLog.e(TAG, "selected_list>>sessionList>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(sessionList));
                //MyLog.e(TAG,"onData>>myOrderFuncLists>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(myOrderFuncLists));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public MutableLiveData<LinkedHashMap<String, List<OrderItemLists>>> getOrderItemList_f_mapMutableLiveData() {
        return orderItemList_f_mapMutableLiveData;
    }

    public MutableLiveData<List<OrderItemLists>> getOrderItemListsMutableLiveData() {
        return orderItemListsMutableLiveData;
    }


    public void setSelectedHeadersList(List<SelectedHeader> headerList) {
        this.selectedHeadersList = headerList;
        this.selectedHeadersListMutableLiveData.postValue(headerList);
    }

    public MutableLiveData<List<SelectedHeader>> getSelectedHeadersListMutableLiveData() {
        return selectedHeadersListMutableLiveData;
    }

    public MutableLiveData<List<MyOrderFuncList>> getMyOrderFuncListsMutableLiveData() {
        return myOrderFuncListsMutableLiveData;
    }

    public MutableLiveData<LinkedHashMap<String, List<MyOrdersList>>> getMyordersHashMapMutable() {
        return myordersHashMapMutable;
    }


    public void setMyOrdersList(List<MyOrdersList> myOrdersList) {
        this.myOrdersList = myOrdersList;
        this.myOrdersListsMutableLiveData.postValue(myOrdersList);
    }

    public MutableLiveData<List<MyOrdersList>> getMyOrdersListsMutableLiveData() {
        return myOrdersListsMutableLiveData;
    }

    public void setF_map(LinkedHashMap<String, List<CheckedList>> f_map) {
        //MyLog.e(TAG, "f_maps>>set>>" + new GsonBuilder().setPrettyPrinting().create().toJson(f_map));
        this.f_map = f_map;
        this.f_mapMutable.postValue(f_map);
    }

    public MutableLiveData<LinkedHashMap<String, List<CheckedList>>> getF_mapMutable() {
        return f_mapMutable;
    }

    public void setUserItemLists(List<UserItemList> userItemLists) {
        this.userItemLists = userItemLists;
        this.userItemListsMutableLiveData.postValue(userItemLists);
    }

    public MutableLiveData<List<UserItemList>> getUserItemListsMutableLiveData() {
        return userItemListsMutableLiveData;
    }

    public void setCheck_s_map(List<LinkedHashMap<String, List<CheckedList>>> check_s_map) {
        this.check_s_map = check_s_map;
        this.check_s_mapMutable.postValue(check_s_map);
    }

    public MutableLiveData<List<LinkedHashMap<String, List<CheckedList>>>> getCheck_s_mapMutable() {
        return check_s_mapMutable;
    }

    public MutableLiveData<Integer> getI_fragmentMutable() {
        return i_fragmentMutable;
    }

    public void setI_fragment(Integer i_fragment) {
        this.i_fragment = i_fragment;
        this.i_fragmentMutable.postValue(i_fragment);
    }

    public MutableLiveData<List<ItemList>> getItemMutable() {
        return itemMutable;
    }

    public MutableLiveData<List<CheckedList>> getCheckedList_Mutable() {
        return checkedList_Mutable;
    }

    public void setCheckedLists(List<CheckedList> checkedLists) {
        this.checkedLists = checkedLists;
        this.checkedList_Mutable.postValue(checkedLists);
    }

    public MutableLiveData<List<HeaderList>> getHeaderListMutableList() {
        return headerListMutableList;
    }

    public MutableLiveData<Integer> getValue() {
        return value;
    }

    public void setFunc_title(String func_title) {
        this.func_title = func_title;
        func_title_Mutable.postValue(func_title);
    }

    public MutableLiveData<String> getFunc_title_Mutable() {
        return func_title_Mutable;
    }

    public void setI_value(Integer i_value) {
        this.i_value = i_value;
        this.value.postValue(i_value);
    }

    public MutableLiveData<List<ItemList>> getItemHeaderMutable() {
        return itemHeaderMutable;
    }

    public void setHeader_title(String header_title) {
        this.header_title = header_title;
    }

    public MutableLiveData<String> getHeader_title_Mutable() {
        return header_title_Mutable;
    }

    public void setEmail(String email) {
        GetUserDeatils(email);
        this.email = email;

    }

    public MutableLiveData<Boolean> getEmailMutable() {
        return EmailMutable;
    }


    public MutableLiveData<List<LinkedHashMap<String, List<ItemList>>>> getS_mapMutable() {
        return s_mapMutable;
    }

    private void GetItem() {
        databaseReference = firebaseDatabase.getReference("Items").child("List");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "list>>snap>>" + snapshot);
                int size = 0;
                for (int k = 0; k < headerList.size(); k++) {
                    MyLog.e(TAG, "list>>snap>>fun>>" + snapshot.child(headerList.get(k).getHeader()).getValue());
                    itemLists = new ArrayList<>();
                    ArrayList<String> str = new ArrayList<>();
                    str = (ArrayList<String>) snapshot.child(headerList.get(k).getHeader()).getValue();
                    for (String i : str) {
                        MyLog.e(TAG, "list>>" + i);
                        if (i != null) {
                            ItemList itemLists1 = new ItemList(
                                    i);
                            itemLists.add(itemLists1);
                        } else {
                            continue;
                        }
                    }
                    itemMutable.postValue(itemLists);
                    //MyLog.e(TAG, "stringListLinkedHashMap1>>set item>>" + new GsonBuilder().setPrettyPrinting().create().toJson(itemLists));
                    f_maps.put(headerList.get(k).getHeader(), itemLists);
                    //MyLog.e(TAG, "itemLists>>" + new GsonBuilder().setPrettyPrinting().create().toJson(itemLists));
                    size++;
                }

                s_map.add(f_maps);
                s_mapMutable.postValue(s_map);
                //MyLog.e(TAG, "hashmap>>item list>>" + new GsonBuilder().setPrettyPrinting().create().toJson(s_map));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "list>>snap>>fun>>Fail to get data.");
            }
        });
    }

    private void GetUserDeatils(String email) {
        databaseReference = firebaseDatabase.getReference("Users-Id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "snap>>" + snapshot);
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "error>>at firebase  emails " + datas.child("email").getValue().toString());
                    if (Objects.equals(email, datas.child("email").getValue().toString())) {
                        new SharedPreferences_data(getApplication()).setS_email(datas.child("email").getValue().toString());
                        new SharedPreferences_data(getApplication()).setS_user_name(datas.child("username").getValue().toString());
                        new SharedPreferences_data(getApplication()).setS_phone_number(datas.child("phone_number").getValue().toString());
                        check_email = true;
                        EmailMutable.postValue(check_email);
                        MyLog.e(TAG, "error>>at firebase  emails " + check_email);
                        break;
                    } else {
                        continue;
                        //MyLog.e(TAG, "error>>at firebase  emails "+check_email);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<List<HeaderList>> getListMutableLiveData() {
        return headerListMutableList;
    }

    public MutableLiveData<List<FunList>> getFunMutableList() {
        return funMutableList;
    }

    private void GetFun() {
        databaseReference = firebaseDatabase.getReference("Items").child("Function");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "home>>snap>>fun>>" + snapshot);
                int size = 0;
                funLists = new ArrayList<>();

                for (DataSnapshot datas : snapshot.getChildren()) {
                  /*  MyLog.e(TAG, "snap>>" + datas.child("username").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("email").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("phone_number").getValue().toString());*/
                    String path = "" + size;
                    MyLog.e(TAG, "home>>snap>>fun>>" + path);
                    //MyLog.e(TAG, "home>>snap>>fun>>" +  datas.child("0").getValue().toString());
                    FunList funList1 = new FunList(
                            datas.getValue().toString());
                    funLists.add(funList1);
                    size++;

                }
                funMutableList.postValue(funLists);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "home>>snap>>fun>>Fail to get data.");
            }
        });
    }

    private void GetHeader() {
        databaseReference = firebaseDatabase.getReference("Items").child("Category");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "home>>snap>>" + snapshot);
                int size = 0;
                headerList = new ArrayList<>();
                for (DataSnapshot datas : snapshot.getChildren()) {
                  /*  MyLog.e(TAG, "snap>>" + datas.child("username").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("email").getValue().toString());*/
                    String path = "" + size;
                    MyLog.e(TAG, "home>>snap>>" + path);
                    MyLog.e(TAG, "home>>snap>>" + datas.getValue().toString());
                    //MyLog.e(TAG, "home>>snap>>" +  datas.child("a").getValue().toString());

                    HeaderList headerList1 = new HeaderList(
                            datas.getValue().toString()
                    );
                    headerList.add(headerList1);
                    size++;

                }
                headerListMutableList.postValue(headerList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "home>>snap>>Fail to get data.");
            }
        });
    }


    public void getheaderFragment(String header, int position, List<LinkedHashMap<String, List<ItemList>>> linkedHashMaps) {

        header_title_Mutable.postValue(header);
        List<ItemList> itemLists = s_map.get(0).get(header);
        //itemLists=itemLists1;
        //MyLog.e(TAG, "stringListLinkedHashMap1>>set>>" + new GsonBuilder().setPrettyPrinting().create().toJson(itemLists));
        this.itemHeaderMutable.postValue(itemLists);
        MyLog.e(TAG, "itm>nut>>" + itemLists.size());
        //MyLog.e(TAG, "hashmap>>data>>" + new GsonBuilder().setPrettyPrinting().create().toJson(itemLists));
        if (itemLists.size() > 0) {
            value.postValue(2);

        } else {

            Toast.makeText(getApplication(), "Empty Response", Toast.LENGTH_SHORT).show();
        }
    }

    public void getfunFragment(String fun) {
        this.setI_value(6);
        this.func_title_Mutable.postValue(fun);
    }

    public void GetViewList(String s_user_name, String func_title) {

        //MyLog.e(TAG,"orderItemList_f_map>>f_maps>>map orderitem list before11>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(orderItemList_f_map));
        orderItemList_f_map.clear();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders").child(s_user_name).child(func_title);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = 0;

                for (DataSnapshot datas : snapshot.getChildren()) {
                    orderItemLists = new ArrayList<>();
                    header_title = datas.getKey().toString();
                    MyLog.e(TAG, "itemlists>>datas>>" + datas);
                    MyLog.e(TAG, "orderItemList_f_map>>datas>>" + datas.getKey().toString());
                    for (DataSnapshot dataSnapshot : datas.getChildren()) {
                        MyLog.e(TAG, "itemlists>>datasnap>>" + dataSnapshot);
                        MyLog.e(TAG, "orderItemList_f_map>>datasnap>>" + dataSnapshot.getKey().toString() + "\t==\t" + dataSnapshot.getValue().toString());
                        OrderItemLists itemLists = new OrderItemLists(
                                dataSnapshot.getValue().toString()
                        );
                        orderItemLists.add(itemLists);
                        //MyLog.e(TAG,"orderItemList_f_map>>f_maps>>map orderitem list before>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(orderItemList_f_map));
                        orderItemList_f_map.put(header_title, orderItemLists);
                        //MyLog.e(TAG,"orderItemList_f_map>>f_maps>>map orderitem list after>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(orderItemList_f_map));
                        size++;
                    }

                }
                orderItemList_f_mapMutableLiveData.postValue(orderItemList_f_map);
                //get selected header list
                //get linked hasp map to view item list
                selectedHeadersList.clear();
                Set<String> stringSet = orderItemList_f_map.keySet();

                for (String a : stringSet) {
                    SelectedHeader aList = new SelectedHeader(
                            a
                    );
                    selectedHeadersList.add(aList);

                }
                selectedHeadersListMutableLiveData.postValue(selectedHeadersList);


                // MyLog.e(TAG,"itemlists>>f_maps>>map get>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(orderItemList_f_map));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void SetBreadCrumsList(String s, int i) {
        if (i == 0) {
            breadCrumbLists.clear();
            MyLog.e(TAG, "breadcrumbs>>breadcrumbsList>>" + s);
            BreadCrumbList list = new BreadCrumbList(s);
            breadCrumbLists.add(list);
            breadCrumbListsMutableLiveData.postValue(breadCrumbLists);
        } else {
            //MyLog.e(TAG, "breadcrumbs>>breadcrumbsList>>" + s);
            BreadCrumbList list = new BreadCrumbList(s);
            breadCrumbLists.add(list);
            breadCrumbListsMutableLiveData.postValue(breadCrumbLists);
        }

    }
}

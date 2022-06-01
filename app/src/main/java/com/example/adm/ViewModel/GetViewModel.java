package com.example.adm.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SessionList;
import com.example.adm.Classes.SharedPreferences_data;
import com.example.adm.Fragments.Control_Panel.Func.FuncList;
import com.example.adm.Fragments.Control_Panel.Header.HeaderList;
import com.example.adm.Fragments.Control_Panel.Item.ItemArrayList;
import com.example.adm.Fragments.Control_Panel.UpdatedList;
import com.example.adm.Fragments.Orders.BottomSheet.OrderItemLists;
import com.example.adm.Fragments.Orders.BottomSheet.SelectedHeader;
import com.example.adm.Fragments.Orders.UserItemList;
import com.example.adm.Fragments.Orders.BottomSheet.OrderLists;
import com.example.adm.Fragments.Users.UserDetailsList;
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
    //check user login
    private MutableLiveData<Boolean> EmailMutable = new MutableLiveData<>();
    private String email;
    private boolean check_email = false;

    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    //Selected Header
    private String header_title;
    private MutableLiveData<String> header_title_Mutable = new MutableLiveData<>();

    //Selected Func
    private String func_title;
    private MutableLiveData<String> func_title_Mutable = new MutableLiveData<>();

    //fragment
    private Integer i_value;
    private MutableLiveData<Integer> value = new MutableLiveData<>();

    //Orders list
    private List<OrderLists> orderLists = new ArrayList<>();
    private MutableLiveData<List<OrderLists>> orderListsMutable = new MutableLiveData<>();


    //User list
    private List<UserDetailsList> userLists = new ArrayList<>();
    private MutableLiveData<List<UserDetailsList>> UserListMutable = new MutableLiveData<>();


    //user-item list
    private List<UserItemList> userItemLists = new ArrayList<>();

    //Linked HashMap
    private LinkedHashMap<String, List<UserItemList>> f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<UserItemList>>> f_mapMutable = new MutableLiveData<>();
    private List<LinkedHashMap<String, List<UserItemList>>> s_map = new ArrayList<>();
    private MutableLiveData<List<LinkedHashMap<String, List<UserItemList>>>> s_mapMutable = new MutableLiveData<>();

    //get Data from Firebase database
    //header list
    private List<HeaderList> headerList = new ArrayList<>();
    private MutableLiveData<List<HeaderList>> headerListMutableLiveData = new MutableLiveData<>();
    //func list
    private List<FuncList> funcList = new ArrayList<>();
    private MutableLiveData<List<FuncList>> funcListMutableLiveData = new MutableLiveData<>();
    //item list
    private List<ItemArrayList> itemList = new ArrayList<>();
    private MutableLiveData<List<ItemArrayList>> itemListMutableLiveData = new MutableLiveData<>();
    //session list
    private List<SessionList> sessionLists = new ArrayList<>();
    private MutableLiveData<List<SessionList>> sessionListsMutableLiveData = new MutableLiveData<>();


    //updated list
    private List<UpdatedList> updatedLists = new ArrayList<>();
    private MutableLiveData<List<UpdatedList>> updatedListsMutableLiveData = new MutableLiveData<>();

    //selected Item list
    private List<OrderItemLists> orderItemLists = new ArrayList<>();
    private MutableLiveData<List<OrderItemLists>> orderItemListsMutableLiveData = new MutableLiveData<>();
    private LinkedHashMap<String, List<OrderItemLists>> orderItemList_f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<OrderItemLists>>> orderItemList_f_mapMutableLiveData = new MutableLiveData<>();

    //orderlist to get item list view
    private OrderLists orderListsView;
    private MutableLiveData<OrderLists> orderListsViewMutableLiveData = new MutableLiveData<>();

    //selected list
    private List<SelectedHeader> selectedHeaders=new ArrayList<>();
    private MutableLiveData<List<SelectedHeader>> selectedHeadersMutableLiveData=new MutableLiveData<>();

    private String TAG = "ViewClassModel";
    String s_user_name, func, header, item,session_title;


    public GetViewModel(@NonNull Application application) {
        super(application);
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        GetOrdesList();
        GetUserList();
        //GetSessionList();
        /*GetHeader();
        GetFun();
        GetItem();*/
        GetUpdateHeader();
        GetUpdateFun();
        GetUpdateItem();

    }

    public MutableLiveData<List<SessionList>> getSessionListsMutableLiveData() {
        return sessionListsMutableLiveData;
    }

    public MutableLiveData<List<SelectedHeader>> getSelectedHeadersMutableLiveData() {
        return selectedHeadersMutableLiveData;
    }

    public void setOrderListsView(OrderLists orderListsView) {
        this.orderListsView = orderListsView;
        this.orderListsViewMutableLiveData.postValue(orderListsView);
    }

    public MutableLiveData<OrderLists> getOrderListsViewMutableLiveData() {
        return orderListsViewMutableLiveData;
    }

    public MutableLiveData<LinkedHashMap<String, List<OrderItemLists>>> getOrderItemList_f_mapMutableLiveData() {
        return orderItemList_f_mapMutableLiveData;
    }

    public MutableLiveData<List<OrderItemLists>> getOrderItemListsMutableLiveData() {
        return orderItemListsMutableLiveData;
    }


    public MutableLiveData<List<LinkedHashMap<String, List<UserItemList>>>> getS_mapMutable() {
        return s_mapMutable;
    }

    public void setUpdatedLists(List<UpdatedList> updatedLists) {
        this.updatedLists = updatedLists;
        this.updatedListsMutableLiveData.postValue(updatedLists);
    }

    public MutableLiveData<List<UpdatedList>> getUpdatedListsMutableLiveData() {
        return updatedListsMutableLiveData;
    }

    public MutableLiveData<List<HeaderList>> getHeaderListMutableLiveData() {
        return headerListMutableLiveData;
    }

    public MutableLiveData<List<FuncList>> getFuncListMutableLiveData() {
        return funcListMutableLiveData;
    }

    public MutableLiveData<List<ItemArrayList>> getItemListMutableLiveData() {
        return itemListMutableLiveData;
    }

    private void GetUpdateItem() {
        databaseReference = firebaseDatabase.getReference("Items").child("List");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "list>>snap>>" + snapshot);
                int size = 0;


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    itemList = new ArrayList<>();
                    ItemArrayList itemList1 = new ItemArrayList(
                            dataSnapshot.getValue().toString()
                    );
                    itemList.add(itemList1);
                }

                itemListMutableLiveData.postValue(itemList);
                //MyLog.e(TAG, "data>>item>>" + new GsonBuilder().setPrettyPrinting().create().toJson(itemList));
                size++;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "list>>snap>>fun>>Fail to get data.");
            }
        });
    }

    private void GetUpdateFun() {
        databaseReference = firebaseDatabase.getReference("Items").child("Function");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "home>>snap>>fun>>" + snapshot);
                int size = 0;
                funcList = new ArrayList<>();

                for (DataSnapshot datas : snapshot.getChildren()) {
                    String path = "" + size;
                    MyLog.e(TAG, "home>>snap>>fun>>" + path);
                    //MyLog.e(TAG, "home>>snap>>fun>>" +  datas.child("0").getValue().toString());
                    FuncList funList1 = new FuncList(
                            datas.getValue().toString());
                    funcList.add(funList1);
                    size++;

                }
                funcListMutableLiveData.postValue(funcList);
                //MyLog.e(TAG, "data>>func>>" + new GsonBuilder().setPrettyPrinting().create().toJson(funcList));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "home>>snap>>fun>>Fail to get data.");
            }
        });
    }

    private void GetUpdateHeader() {
        databaseReference = firebaseDatabase.getReference("Items").child("Category");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "home>>snap>>" + snapshot);
                int size = 0;
                headerList = new ArrayList<>();
                for (DataSnapshot datas : snapshot.getChildren()) {

                    String path = "" + size;

                    //MyLog.e(TAG, "home>>snap>>" +  datas.child("a").getValue().toString());

                    HeaderList headerList1 = new HeaderList(
                            datas.getValue().toString()
                    );
                    headerList.add(headerList1);
                    size++;

                }

                headerListMutableLiveData.postValue(headerList);
                // MyLog.e(TAG, "data>>header>>" + new GsonBuilder().setPrettyPrinting().create().toJson(headerList));

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "home>>snap>>Fail to get data.");
            }
        });
    }


    public MutableLiveData<List<UserDetailsList>> getUserListMutable() {
        return UserListMutable;
    }

    public MutableLiveData<LinkedHashMap<String, List<UserItemList>>> getF_mapMutable() {
        return f_mapMutable;
    }

    private void GetUserList() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users-Id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //MyLog.e(TAG, "snap>>" + snapshot);
                for (DataSnapshot datas : snapshot.getChildren()) {
                  /*  MyLog.e(TAG, "snap>>" + datas.child("username").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("email").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("phone_number").getValue().toString());
                  */
                    UserDetailsList userList = new UserDetailsList(
                            datas.child("username").getValue().toString(),
                            datas.child("email").getValue().toString(),
                            datas.child("phone_number").getValue().toString()
                    );
                    userLists.add(userList);
                }
                UserListMutable.postValue(userLists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<List<OrderLists>> getOrderListsMutable() {
        return orderListsMutable;
    }

    private void GetOrdesList() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int size = 0;
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "snap>>datas>>" + datas);
                    MyLog.e(TAG, "snap>>datas>>" + datas.getKey().toString());
                    userItemLists = new ArrayList<>();
                    s_user_name = datas.getKey().toString();
                    for (DataSnapshot dataSnapshot : datas.getChildren()) {
                        func = dataSnapshot.getKey().toString();
                        MyLog.e(TAG, "snap>>datasnap>>" + dataSnapshot);
                        MyLog.e(TAG, "snap>>datasnap>>" + dataSnapshot.getKey().toString());

                        sessionLists=new ArrayList<>();
                        for (DataSnapshot shots : dataSnapshot.getChildren()) {
                            session_title = shots.getKey().toString();

                            MyLog.e(TAG, "snap>>shots>>" + shots);
                            MyLog.e(TAG, "snap>>shots>>" + shots.getKey().toString());
                            userItemLists = new ArrayList<>();
                            for(DataSnapshot shotdatas:shots.getChildren())
                            {
                                header=shotdatas.getKey().toString();
                                MyLog.e(TAG, "snap>>shotdatas>>" + shotdatas);
                                MyLog.e(TAG, "snap>>shotdatas>>" + shotdatas.getKey().toString());
                                size = 0;
                            for (DataSnapshot data : shotdatas.getChildren()) {
                                MyLog.e(TAG, "snap>>data ss>>" + data.getKey().toString());

                                size++;
                            }

                            MyLog.e(TAG, "snap>>size>" + size);
                            UserItemList itemList = new UserItemList(
                                    header,
                                    size
                            );
                            size = 0;
                            userItemLists.add(itemList);
                            String s = s_user_name + "-" + func+"-"+session_title;
                            f_map.put(s, userItemLists);


                        }
                            //set session list
                            SessionList list1=new SessionList(
                                    session_title
                            );
                            sessionLists.add(list1);
                            sessionListsMutableLiveData.postValue(sessionLists);
                            //MyLog.e(TAG,"session>>f_maps>>session list>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(sessionLists));

                        }
                        OrderLists orderLists1 = new OrderLists(
                                s_user_name,
                                func
                        );
                        orderLists.add(orderLists1);

                    }

                }

                orderListsMutable.postValue(orderLists);
                f_mapMutable.postValue(f_map);
                s_map.add(f_map);
                s_mapMutable.postValue(s_map);
                //MyLog.e(TAG,"session>>f_maps>>>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(f_map));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }


   /* public MutableLiveData<List<ItemList>> getItemMutable() {
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
    }*/

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

    /*public MutableLiveData<List<ItemList>> getItemHeaderMutable() {
        return itemHeaderMutable;
    }*/

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

   /* public MutableLiveData<List<LinkedHashMap<String, List<ItemList>>>> getS_mapMutable() {
        return s_mapMutable;
    }*/

    /*private void GetItem() {
        databaseReference = firebaseDatabase.getReference("Items").child("List");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "list>>snap>>" + snapshot);
                int size=0;
                for(int k=0;k<headerList.size();k++) {
                    MyLog.e(TAG, "list>>snap>>fun>>" + snapshot.child(headerList.get(k).getHeader()).getValue());
                    itemLists = new ArrayList<>();
                    ArrayList<String> str = new ArrayList<>();
                    str = (ArrayList<String>) snapshot.child(headerList.get(k).getHeader()).getValue();
                    for (String i : str) {
                        MyLog.e(TAG, "list>>" + i);
                        ItemList itemLists1 = new ItemList(
                                i);
                        itemLists.add(itemLists1);
                    }
                    itemMutable.postValue(itemLists);
                    f_map.put(headerList.get(k).getHeader(),itemLists);
                    //MyLog.e(TAG, "itemLists>>" + new GsonBuilder().setPrettyPrinting().create().toJson(itemLists));
                    size++;
                }
                s_map.add(f_map);
                s_mapMutable.postValue(s_map);
                //MyLog.e(TAG, "hashmap>>item list>>" + new GsonBuilder().setPrettyPrinting().create().toJson(s_map));




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "list>>snap>>fun>>Fail to get data.");
            }
        });
    }*/

    public boolean GetUserDeatils(String email) {

        databaseReference = firebaseDatabase.getReference("Admin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // MyLog.e(TAG, "snap>>" + snapshot);
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "error>>at firebase  emails " + datas.child("email").getValue().toString());
                    if (Objects.equals(email, datas.child("email").getValue().toString())) {
                        new SharedPreferences_data(getApplication()).setS_email(datas.child("email").getValue().toString());
                        new SharedPreferences_data(getApplication()).setS_user_name(datas.child("username").getValue().toString());
                        new SharedPreferences_data(getApplication()).setS_phone_number(datas.child("phone_number").getValue().toString());
                        check_email = true;
                        EmailMutable.postValue(check_email);
                        MyLog.e(TAG, "boolean>>at firebase  emails " + check_email);
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
        MyLog.e(TAG, "boolean>>at return " + check_email);
        return check_email;
    }

   /* public MutableLiveData<List<HeaderList>> getListMutableLiveData() {
        return headerListMutableList;
    }

    public MutableLiveData<List<FunList>> getFunMutableList() {
        return funMutableList;
    }*/

    /* private void GetFun() {
         databaseReference = firebaseDatabase.getReference("Items").child("Function");

         databaseReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 MyLog.e(TAG, "home>>snap>>fun>>" + snapshot);
                 int size=0;
                 funLists=new ArrayList<>();

                 for (DataSnapshot datas : snapshot.getChildren()) {
                   *//*  MyLog.e(TAG, "snap>>" + datas.child("username").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("email").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("phone_number").getValue().toString());*//*
                    String path=""+size;
                    MyLog.e(TAG, "home>>snap>>fun>>" +  path);
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
                int size=0;
                headerList=new ArrayList<>();
                for (DataSnapshot datas : snapshot.getChildren()) {
                  *//*  MyLog.e(TAG, "snap>>" + datas.child("username").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("email").getValue().toString());*//*
                    String path=""+size;
                    MyLog.e(TAG, "home>>snap>>" +  path);
                    MyLog.e(TAG, "home>>snap>>" +  datas.getValue().toString());
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
        List<ItemList> itemLists=s_map.get(0).get(header);
        //itemLists=itemLists1;
        this.itemHeaderMutable.postValue(itemLists);
        MyLog.e(TAG,"itm>nut>>"+itemLists.size());
        //MyLog.e(TAG, "hashmap>>data>>" + new GsonBuilder().setPrettyPrinting().create().toJson(itemLists));
        if(itemLists.size()>0) {
            value.postValue(2);

        }
        else
        {

            Toast.makeText(getApplication(), "Empty Response", Toast.LENGTH_SHORT).show();
        }
    }
*/
    public void getfunFragment(String fun) {
        this.setI_value(1);
        this.func_title_Mutable.postValue(fun);
    }

    public void UpdateListBase() {
        for (int i = 0; i < updatedLists.size(); i++) {
            int n = updatedLists.get(i).getPosition();
            databaseReference = firebaseDatabase.getReference("DUMMY").child(updatedLists.get(i).getTitle());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    MyLog.e(TAG, "updatesnap>>" + snapshot);
                    MyLog.e(TAG, "updatesnap>>value>>" + snapshot.getValue());
                    databaseReference.setValue("CREAM");

                    Toast.makeText(getApplication(), "Successfully updated data.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplication(), "Fail to update data.", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }


    public void GetViewList(OrderLists orderLists, List<SessionList> sessionLists) {
        //MyLog.e(TAG,"orderItemList_f_map>>f_maps>>map orderitem list before11>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(orderItemList_f_map));
        orderItemList_f_map.clear();
        firebaseDatabase = FirebaseDatabase.getInstance();
        for(int i=0;i<sessionLists.size();i++) {
            MyLog.e(TAG,"sessionllist>>"+sessionLists.get(i).getSession_title());
            databaseReference = firebaseDatabase.getReference("Orders").child(orderLists.getS_user_name()).child(orderLists.getFunc()).child(sessionLists.get(i).getSession_title());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int size = 0;

                    for (DataSnapshot datas : snapshot.getChildren()) {
                        orderItemLists = new ArrayList<>();
                        header = datas.getKey().toString();
                        MyLog.e(TAG, "itemlists>>datas>>" + datas);
                        MyLog.e(TAG, "orderItemList_f_map>>datas>>" + datas.getKey().toString());
                        for (DataSnapshot dataSnapshot : datas.getChildren()) {
                            MyLog.e(TAG, "itemlists>>datasnap>>" + dataSnapshot);
                            MyLog.e(TAG, "orderItemList_f_map>>datasnap>>" + dataSnapshot.getKey().toString() + "\t==\t" + dataSnapshot.getValue().toString());
                            OrderItemLists itemLists = new OrderItemLists(
                                    dataSnapshot.getValue().toString()
                            );
                            orderItemLists.add(itemLists);
                            //MyLog.e(TAG, "orderItemList_f_map>>f_maps>>map orderitem list before>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(orderItemList_f_map));
                            orderItemList_f_map.put(header, orderItemLists);
                            //MyLog.e(TAG, "sessionllist>>f_maps>>map orderitem list after>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(orderItemList_f_map));
                            size++;
                        }

                    }
                    orderItemList_f_mapMutableLiveData.postValue(orderItemList_f_map);
                    //get selected header list
                    //get linked hasp map to view item list
                    selectedHeaders.clear();
                    Set<String> stringSet = orderItemList_f_map.keySet();

                    for (String a : stringSet) {
                        SelectedHeader aList = new SelectedHeader(
                                a
                        );
                        selectedHeaders.add(aList);

                    }
                    selectedHeadersMutableLiveData.postValue(selectedHeaders);


                    MyLog.e(TAG, "sessionllist>>f_maps>>map get>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(orderItemList_f_map));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}

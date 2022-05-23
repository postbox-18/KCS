package com.example.kcs.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Header.HeaderList;
import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.Fragment.Items.ItemList;
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

public class GetViewModel extends AndroidViewModel {
    //Header List
    private MutableLiveData<List<HeaderList>> headerMutableList=new MutableLiveData<>();
    private List<HeaderList> headerList=new ArrayList<>();

    //func List
    private MutableLiveData<List<FunList>> funMutableList=new MutableLiveData<>();
    private List<FunList> funLists=new ArrayList<>();

    //item list
    private MutableLiveData<List<ItemList>> itemMutable=new MutableLiveData<>();
    private List<ItemList> itemLists=new ArrayList<>();
    //Linked HashMap
    private LinkedHashMap<String,List<ItemList>> f_map=new LinkedHashMap<>();
    private List<LinkedHashMap<String,List<ItemList>>> s_map=new ArrayList<>();
    private MutableLiveData<List<LinkedHashMap<String,List<ItemList>>>> s_mapMutable=new MutableLiveData<>();

    //check user login
    private MutableLiveData<Boolean> EmailMutable=new MutableLiveData<>();
    private String email;
    private boolean check_email=false;

    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //Header Fragment
    private FunList fun_title;
    private MutableLiveData<HeaderList> headerListMutableLiveData = new MutableLiveData<>();

    private String TAG="ViewClassModel";

    public GetViewModel(@NonNull Application application) {
        super(application);
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        GetUserDeatils(email);
        GetHeader();
        GetFun();
        GetItem();


    }



    public void setEmail(String email) {
        GetUserDeatils(email);
        this.email = email;
        this.EmailMutable.postValue(check_email);
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
    }

    private void GetUserDeatils(String email) {
        databaseReference = firebaseDatabase.getReference("Users-Id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "snap>>" + snapshot);
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "error>>at firebase  emails " + datas.child("email").getValue().toString());
                    if(Objects.equals(email, datas.child("email").getValue().toString())) {
                        new SharedPreferences_data(getApplication()).setS_email(datas.child("email").getValue().toString());
                        new SharedPreferences_data(getApplication()).setS_user_name(datas.child("username").getValue().toString());
                        new SharedPreferences_data(getApplication()).setS_phone_number(datas.child("phone_number").getValue().toString());
                        check_email=true;
                        MyLog.e(TAG, "error>>at firebase  emails "+check_email);
                        break;
                    }
                    else
                    {
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
        return headerMutableList;
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
                int size=0;
                funLists=new ArrayList<>();

                for (DataSnapshot datas : snapshot.getChildren()) {
                  /*  MyLog.e(TAG, "snap>>" + datas.child("username").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("email").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("phone_number").getValue().toString());*/
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
                  /*  MyLog.e(TAG, "snap>>" + datas.child("username").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("email").getValue().toString());*/
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
                headerMutableList.postValue(headerList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "home>>snap>>Fail to get data.");
            }
        });
    }
}

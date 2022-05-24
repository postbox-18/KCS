/*
package com.example.kcs;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Func.FunAdapter;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Header.HeaderAdapter;
import com.example.kcs.Fragment.Header.HeaderList;
import com.example.kcs.Fragment.Items.ItemList;
import com.example.kcs.Fragment.Items.CheckedList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MyViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> value = new MutableLiveData<>();
    private Integer i_value;

    private Fragment fragment;
    private MutableLiveData<Fragment> fragmentMutableLiveData = new MutableLiveData<>();

    private Context context;

    private HeaderList headerList;
    private MutableLiveData<HeaderList> headerMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<FunList> funListMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<List<CheckedList>> checkedListMutableLiveData = new MutableLiveData<List<CheckedList>>();
    private MutableLiveData<List<ItemList>> itemListMutableLiveData = new MutableLiveData<List<ItemList>>();
    private MutableLiveData<List<HeaderList>> headerlistMutableLiveData = new MutableLiveData<List<HeaderList>>();
    private FunList funList;
    private List<HeaderList> headerLists = new ArrayList<>();
    private List<ItemList> itemLists = new ArrayList<>();
    private List<FunList> funLists = new ArrayList<>();
    private List<CheckedList> checkedLists = new ArrayList<>();
    //private HeaderAdapter.GetHeaderFragment getHeaderFragment;


    public MyViewModel(@NonNull Application application) {
        super(application);

        */
/*this.headerMutableLiveData.postValue(headerList);
        this.funListMutableLiveData.postValue(funList);*//*

        String json=new SharedPreferences_data(application).getChecked_item_list();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CheckedList>>() {}.getType();
        if(json==null||json.isEmpty())
        {

        }
        else {
            checkedLists = gson.fromJson(json, type);
            this.checkedListMutableLiveData.postValue(checkedLists);
        }

    }

    public MutableLiveData<List<ItemList>> getItemListMutableLiveData() {
        return itemListMutableLiveData;
    }

    public MutableLiveData<Integer> getValue() {
        return value;
    }


    public Integer getI_value() {
        return i_value;
    }


    public void setI_value(Integer i_value) {
        this.i_value = i_value;
        this.value.postValue(i_value);
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public MutableLiveData<Fragment> getFragmentMutableLiveData() {
        return fragmentMutableLiveData;
    }

    public void setFragmentMutableLiveData(MutableLiveData<Fragment> fragmentMutableLiveData) {
        this.fragmentMutableLiveData = fragmentMutableLiveData;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public HeaderList getHeaderList() {
        return headerList;
    }

    public void setHeader(HeaderList headerList) {
        this.headerList = headerList;

    }

    public FunList getFunList() {
        return funList;
    }

    public MutableLiveData<HeaderList> getHeaderMutableLiveData() {
        return headerMutableLiveData;
    }

    public MutableLiveData<FunList> getFunListMutableLiveData() {
        return funListMutableLiveData;
    }

    public void setValue(MutableLiveData<Integer> value) {
        this.value = value;
    }

    public void setHeaderListMutableLiveData(MutableLiveData<HeaderList> headerListMutableLiveData) {
        this.headerMutableLiveData = headerListMutableLiveData;
    }

    public void setFunListMutableLiveData(MutableLiveData<FunList> funListMutableLiveData) {
        this.funListMutableLiveData = funListMutableLiveData;
    }

    public void setFunList(FunList funList) {
        this.funList = funList;
        this.funListMutableLiveData.postValue(funList);
    }

    public List<HeaderList> getHeaderLists() {
        return headerLists;
    }



    public List<ItemList> getItemLists() {
        return itemLists;
    }

    public MutableLiveData<List<CheckedList>> getCheckedListMutableLiveData() {
        return checkedListMutableLiveData;
    }

    public void setCheckedListMutableLiveData(MutableLiveData<List<CheckedList>> checkedListMutableLiveData) {
        this.checkedListMutableLiveData = checkedListMutableLiveData;
    }

    public List<CheckedList> getCheckedLists() {
        return checkedLists;
    }

    public void setCheckedLists(List<CheckedList> checkedLists) {
        this.checkedLists = checkedLists;
        this.checkedListMutableLiveData.postValue(checkedLists);

    }

    public void setItemLists(List<ItemList> itemLists) {
        this.itemLists = itemLists;
        itemListMutableLiveData.postValue(itemLists);

    }

    public void setHeaderLists(List<HeaderList> headerLists) {
        this.headerLists = headerLists;
        this.headerlistMutableLiveData.postValue(headerLists);
    }

    public MutableLiveData<List<HeaderList>> getHeaderlistMutableLiveData() {
        return headerlistMutableLiveData;
    }

    public List<FunList> getFunLists() {
        return funLists;
    }

    public void setFunLists(List<FunList> funLists) {
        this.funLists = funLists;
    }





    public void getheaderFragment(HeaderList headerList1, int position, List<LinkedHashMap<String, List<ItemList>>> linkedHashMaps) {

        this.headerMutableLiveData.postValue(headerList1);
            List<ItemList> itemLists=linkedHashMaps.get(0).get(headerList1.getHeader());
            //itemLists=itemLists1;
        itemListMutableLiveData.postValue(itemLists);

            if(itemLists.size()>0) {
                i_value=2;
                this.value.postValue(i_value);
            }
            else
            {
                Toast.makeText(getApplication(), "Empty Response", Toast.LENGTH_SHORT).show();
            }


        }

}
*/

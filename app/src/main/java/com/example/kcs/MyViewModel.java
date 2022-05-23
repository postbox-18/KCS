package com.example.kcs;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Func.FunAdapter;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Header.HeaderAdapter;
import com.example.kcs.Fragment.Header.HeaderList;
import com.example.kcs.Fragment.Items.ItemList;
import com.example.kcs.Fragment.Items.CheckedList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> value = new MutableLiveData<>();
    private Integer i_value;

    private Fragment fragment;
    private MutableLiveData<Fragment> fragmentMutableLiveData = new MutableLiveData<>();

    private Context context;

    private HeaderList headerList;
    private MutableLiveData<HeaderList> headerListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<FunList> funListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<CheckedList>> checkedListMutableLiveData = new MutableLiveData<List<CheckedList>>();
    private FunList funList;
    private List<HeaderList> headerLists = new ArrayList<>();
    private List<ItemList> itemLists = new ArrayList<>();
    private List<FunList> funLists = new ArrayList<>();
    private List<CheckedList> checkedLists = new ArrayList<>();
    private HeaderAdapter.GetHeaderFragment getHeaderFragment;
    private FunAdapter.GetFunFragment getfunFragment;

    public MyViewModel(@NonNull Application application) {
        super(application);

        this.headerListMutableLiveData.postValue(headerList);
        this.funListMutableLiveData.postValue(funList);
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

    public void setHeaderList(HeaderList headerList) {
        this.headerList = headerList;
        this.headerListMutableLiveData.postValue(headerList);
    }

    public FunList getFunList() {
        return funList;
    }

    public MutableLiveData<HeaderList> getHeaderListMutableLiveData() {
        return headerListMutableLiveData;
    }

    public MutableLiveData<FunList> getFunListMutableLiveData() {
        return funListMutableLiveData;
    }

    public void setValue(MutableLiveData<Integer> value) {
        this.value = value;
    }

    public void setHeaderListMutableLiveData(MutableLiveData<HeaderList> headerListMutableLiveData) {
        this.headerListMutableLiveData = headerListMutableLiveData;
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

    public void setHeaderLists(List<HeaderList> headerLists) {
        this.headerLists = headerLists;
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
    }

    public List<FunList> getFunLists() {
        return funLists;
    }

    public void setFunLists(List<FunList> funLists) {
        this.funLists = funLists;
    }

    public HeaderAdapter.GetHeaderFragment getGetHeaderFragment() {
        return getHeaderFragment;
    }

    public void setGetHeaderFragment(HeaderAdapter.GetHeaderFragment getHeaderFragment) {
        this.getHeaderFragment = getHeaderFragment;
    }

    public FunAdapter.GetFunFragment getGetfunFragment() {
        return getfunFragment;
    }

    public void setGetfunFragment(FunAdapter.GetFunFragment getfunFragment) {
        this.getfunFragment = getfunFragment;
    }
}

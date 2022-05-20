package com.example.kcs.Fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.kcs.Fragment.Header.HeaderFragment;
import com.example.kcs.Fragment.Items.ItemFragment;
import com.example.kcs.R;

public class MyViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> value=new MutableLiveData<>();
    private Integer i_value;

    private Fragment fragment;
    private MutableLiveData<Fragment> fragmentMutableLiveData=new MutableLiveData<>();
    public MyViewModel(@NonNull Application application) {
        super(application);

    }


    public MutableLiveData<Integer> getValue() {
        return value;
    }

    public void setValue(MutableLiveData<Integer> value) {
        this.value = value;
    }

    public Integer getI_value() {
        return i_value;
    }

    public void setI_value(Integer i_value, Fragment fragment) {
        this.i_value = i_value;
        this.fragment = fragment;
        this.value.postValue(i_value);
        this.fragmentMutableLiveData.postValue(fragment);

    }

    public void setI_value(Integer i_value) {
        this.i_value = i_value;
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
}

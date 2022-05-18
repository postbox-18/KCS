package com.example.kcs;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Interface.MyConstants;

public class BaseActivity extends AppCompatActivity {
    public String TAG=BaseActivity.class.getSimpleName();
    Context context;
    public String getREST_DataSetOneEndPoint(){
        MyLog.i(TAG,"getDataSetOneEndPoint:"+getString(R.string.dataset_one));
        return getString(R.string.dataset_one);
    }

    public String getSOAPActionDatasetONE(){
        return  getString(R.string.dataset_one_str);
    }
    public String getSoapActionchkLoginNew(Context context){
        return getString(R.string.chkLoginNew);
    }

    public void makeToast(String message){
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }
    public void testToast(String message){
        if(MyConstants.IS_IN_DEBUG){
            Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
        }
    }
}
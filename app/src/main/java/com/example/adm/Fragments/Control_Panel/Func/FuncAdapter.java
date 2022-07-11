package com.example.adm.Fragments.Control_Panel.Func;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.List;

public class FuncAdapter extends RecyclerView.Adapter<FuncAdapter.ViewHolder> {
private List<FuncList> funcList;
private Context context;
private String TAG="FuncAdapter";
private List<UpdatedList> updatedListFuncs=new ArrayList<>();
private GetViewModel getViewModel;
public FuncAdapter(Context context, List<FuncList> funcList, GetViewModel getViewModel) {
        this.funcList = funcList;
        this.context = context;
        this.getViewModel = getViewModel;
        }

@NonNull
@Override
public FuncAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.func_cardview, parent, false);
        return new FuncAdapter.ViewHolder(view);
        //return view;
        }

@Override
public void onBindViewHolder(@NonNull FuncAdapter.ViewHolder holder, int position) {
final FuncList funcList1 = funcList.get(position);
        holder.func_title.setText(funcList1.getFunc());
        UpdatedList updatedListFunc=new UpdatedList(
                "Category",
                holder.func_title.getText().toString(),
                position
        );
        updatedListFuncs.add(updatedListFunc);
        getViewModel.setUpdatedLists(updatedListFuncs);

        }


@Override
public int getItemCount() {
        MyLog.e(TAG, "funcList>>49>>" + funcList.size());
        return funcList.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {

    private EditText func_title;

    public ViewHolder(View view) {
        super(view);
        func_title = view.findViewById(R.id.func_title);
    }
}
}



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

import java.util.List;

public class FuncAdapter extends RecyclerView.Adapter<FuncAdapter.ViewHolder> {
private List<FuncList> funcList;
private Context context;
private String TAG="FuncAdapter";
public FuncAdapter(Context context, List<FuncList> funcList) {
        this.funcList = funcList;
        this.context = context;
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



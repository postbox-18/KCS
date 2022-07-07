package com.example.kcs.Fragment.Items;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;

import com.example.kcs.Fragment.Header.SessionDateTime;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ItemListAdapater extends RecyclerView.Adapter<ItemListAdapater.ViewHolder> {
    private Context context;
    private List<ItemList> itemLists;
    private List<CheckedList> checkedLists = new ArrayList<>();
    //private List<CheckedList> selected_checkedLists = new ArrayList<>();
    private List<SessionDateTime> sessionDateTimes = new ArrayList<>();
    private String TAG = "ItemListAdapater";
    //private MyViewModel myViewModel;
    private GetViewModel getViewModel;
    //private LinkedHashMap<String, List<CheckedList>> f_map = new LinkedHashMap<>();
    //private List<LinkedHashMap<String,List<CheckedList>>> s_map=new ArrayList<>();
    private List<LinkedHashMap<String, List<CheckedList>>> selected_s_map = new ArrayList<>();
    //item map
    private LinkedHashMap<String, List<CheckedList>> itemMap = new LinkedHashMap<>();
    //header map
    private LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> headerMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> sessionMap = new LinkedHashMap<>();
    //fun map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>> funcMap = new LinkedHashMap<>();
    private List<SelectedSessionList> selectedSessionLists = new ArrayList<>();
    private String header, funcTitle, sessionTitle, s_count;
    ItemListAdapater.Unchecked unchecked;

    public interface Unchecked {
        void getUnchecked(String item);
    }

    public ItemListAdapater(Context context, List<ItemList> itemLists, String header, GetViewModel getViewModel, List<LinkedHashMap<String, List<CheckedList>>> linkedHashMaps, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> headerMap) {
        this.context = context;
        this.itemLists = itemLists;
        this.getViewModel = getViewModel;
        this.header = header;
        this.selected_s_map = new ArrayList<>(linkedHashMaps);
        this.headerMap = headerMap;
        //cartViewModel = ViewModelProviders.of((FragmentActivity) context).get(CartViewModel.class);
        //myViewModel= new ViewModelProvider((FragmentActivity)context).get(MyViewModel.class);

    }

    @NonNull
    @Override
    public ItemListAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.item_cardview_list, parent, false);
        return new ItemListAdapater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapater.ViewHolder holder, int position) {
        final ItemList itemList = itemLists.get(position);

        holder.header_title.setText(itemList.getItem());
        if (itemList.getSelected().equals("true")) {
            String[] str = (itemList.getItem()).split("-");
            if (str.length > 1) {
                Spannable word = new SpannableString(str[0]);
                word.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorSecondary)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.header_title.setText(word);
                Spannable wordTwo = new SpannableString(str[1]);
                wordTwo.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.header_title.append(wordTwo);
            } else {
                holder.header_title.setText(itemList.getItem());
                holder.header_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
            }
        } else if (itemList.getSelected().equals("false")) {
            holder.header_title.setTextColor(context.getResources().getColor(R.color.text_silver));
            holder.header_title_q.setTextColor(context.getResources().getColor(R.color.text_silver));
            holder.header_img.setImageResource(R.drawable.logo);

        }

        //onclick
        holder.header_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemList.getSelected().equals("true")) {
                    getViewModel.setItem_title(itemList.getItem()+"_"+itemList.getSelected());
                    getViewModel.setI_value(7);
                    getViewModel.setHeader_title(header);
                    getViewModel.SetBreadCrumsList(itemList.getItem(), 3);
                } else if (itemList.getSelected().equals("false")) {
                    Toast.makeText(context, "Please select another Item", Toast.LENGTH_SHORT).show();

                }
            }
            });



        /*
        if(headerMap==null)
        {
            headerMap=new LinkedHashMap<>();
            MyLog.e(TAG,"headerMap is null");
        }
        MyLog.e(TAG, "count>>headerMap>>" + new GsonBuilder().setPrettyPrinting().create().toJson(headerMap));

        final ItemList itemList1 = itemLists.get(position);

        //img update soon
        //holder.header_img.setText(funList1.getImg());


        //get func title
        getViewModel.getFunc_title_Mutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                funcTitle=s;
            }
        });
        //get session title
        getViewModel.getSession_titleMutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                sessionTitle=s;
            }
        });
        //get Session Date Time
        getViewModel.getF_mapsdtMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SessionDateTime>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SessionDateTime>> stringListLinkedHashMap) {
                sessionDateTimes = stringListLinkedHashMap.get(funcTitle + "-" + sessionTitle);
            }
        });
        //get session map
        getViewModel.getSessionMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> stringLinkedHashMapLinkedHashMap) {
                sessionMap=stringLinkedHashMapLinkedHashMap;
            }
        });
        //get head count
        getViewModel.getS_countLiveData().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                s_count=s;
            }
        });

        //check if checked list item already selected
        if (selected_s_map.size() > 0) {
            for (int k = 0; k < selected_s_map.size(); k++) {
                checkedLists = selected_s_map.get(k).get(header);
                if (checkedLists != null) {
                    for (int i = 0; i < checkedLists.size(); i++) {
                        final CheckedList checkedLists1 = checkedLists.get(i);
                        MyLog.e(TAG, "checked>>" + checkedLists1.getPosition());
                        MyLog.e(TAG, "checked>>" + position);
                        if (checkedLists1.getPosition() == position) {
                            //MyLog.e(TAG, "checked>>" + checkedLists1.getItemList());
                            holder.item_check.setChecked(true);

                        }
                    }
                } else {
                    checkedLists=new ArrayList<>();
                    MyLog.e(TAG, "checked>> selected size is null>>");
                }
            }
        }
        else {
            MyLog.e(TAG, "checked>>selected  map size is null>>");
        }
        MyLog.e(TAG, "hashmap>>size>>" + selected_s_map.size());


        holder.item_check.setText(itemList1.getItem());
        if(itemList1.getSelected().equals("true"))
        {
            holder.item_check.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else {
            holder.item_check.setTextColor(context.getResources().getColor(R.color.light_gray));
            holder.item_check.setEnabled(false);
        }

        holder.item_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (itemList1.getItem() != null) {
                    if (checkedLists == null) {
                        MyLog.e(TAG, "placeorders>>date_time checkedLists null");
                        checkedLists = new ArrayList<>();
                    }
                    else
                    {
                    if (holder.item_check.isChecked()) {

                            CheckedList checkedLists1 = new CheckedList(
                                    itemList1.getItem(),
                                    position
                            );
                            checkedLists.add(checkedLists1);



                        //notifyDataSetChanged();
                    } else {
                        //unchecked.getUnchecked(itemList1.getItem());
                        GetUncheckList(itemList1.getItem());

                    }
                }
                    getViewModel.setCheckedLists(checkedLists);
                    headerMap.put(header, checkedLists);
                    String date=(sessionDateTimes.get(0).getDate()).replace("/","-");
                    String s=sessionTitle+"!"+(date+" "+sessionDateTimes.get(0).getTime())+"/"+s_count;
                    MyLog.e(TAG, "placeorders>>date_time>>" + s);
                    sessionMap.put(s,headerMap);
                    funcMap.put(funcTitle,sessionMap);

                    //set session list
                    Set<String> stringSet = sessionMap.keySet();
                    List<String> aList = new ArrayList<String>(stringSet.size());
                    for (String x : stringSet)
                        aList.add(x);

                    //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                    selectedSessionLists.clear();
                    for (int i = 0; i < aList.size(); i++) {
                        String[] scb = (aList.get(i)).split("/");
                        String count=scb[1];
                        String[] arr = (scb[0]).split("!");

                        //set selected session list and session date and time
                        MyLog.e(TAG, "chs>>list header>> " + arr[0]);
                        SelectedSessionList list = new SelectedSessionList();
                        list.setBolen(null);
                        list.setSession_title(arr[0]);
                        list.setTime(arr[1]);
                        list.setS_count(count);
                        selectedSessionLists.add(list);
                    }

                    //set header map
                    getViewModel.setHeaderMap(headerMap);
                    //set session map
                    getViewModel.setSessionMap(sessionMap);
                    //set func map
                    getViewModel.setFuncMap(funcMap);
                    MyLog.e(TAG, "selected_s_map>>size>>" + selected_s_map.size());
                    selected_s_map.add(headerMap);
                    getViewModel.setCheck_s_map(selected_s_map);
                    //set selected session
                    getViewModel.setSelectedSessionLists(selectedSessionLists);

                    Gson gson = new Gson();
                    String json = gson.toJson(checkedLists);
                    new SharedPreferences_data(context).setChecked_item_list(json);
                    MyLog.e(TAG,"count>>selected_s_map>>"+new GsonBuilder().setPrettyPrinting().create().toJson(selected_s_map));
                    MyLog.e(TAG,"count>>funcMap>>"+new GsonBuilder().setPrettyPrinting().create().toJson(funcMap));


                }
            }
        });
*/

        }

        private void GetUncheckList (String item){
            for (int i = 0; i < checkedLists.size(); i++) {
                if (item.equals(checkedLists.get(i).getItemList())) {
                    checkedLists.remove(i);
                    break;
                }
            }
        }

        @Override
        public int getItemCount () {
            return itemLists.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            private CardView header_card;
            private ImageView header_img;
            private TextView header_title, header_title_q;

            public ViewHolder(View view) {
                super(view);
                header_img = view.findViewById(R.id.header_img);
                header_title = view.findViewById(R.id.header_title);
                header_card = view.findViewById(R.id.header_card);
                header_title_q = view.findViewById(R.id.header_title_q);

            }
        }
    }

package com.example.kcs.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.kcs.BreadCrumbs.BreadCrumbList;
import com.example.kcs.Classes.CheckEmail;
import com.example.kcs.Classes.ImgFunList;
import com.example.kcs.Classes.ImgList;
import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Header.SessionDateTime;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrderFuncList;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Header.HeaderList;
import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.Fragment.Items.ItemList;
import com.example.kcs.Fragment.Items.ItemSelectedList.UserItemList;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.OrderItemLists;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrdersItems.MyOrdersList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.MyOrdersItems.SelectedDateList;
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


    //session list
    private MutableLiveData<List<SessionList>> sessionListMutable = new MutableLiveData<>();
    private List<SessionList> sessionList = new ArrayList<>();

    //sessionTime list
    private List<TimeList> timeList = new ArrayList<>();
    private LinkedHashMap<String, List<TimeList>> timeListF_Map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<TimeList>>> timeListF_MapMutableLiveData = new MutableLiveData<>();

    //selected session list
    //private MutableLiveData<List<SessionList>> s_sessionListMutable = new MutableLiveData<>();
    /*private List<SelectedSessionList> s_sessionList = new ArrayList<>();
    private LinkedHashMap<String, List<SelectedSessionList>> ss_f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<SelectedSessionList>>> ss_f_mapMutableLiveData = new MutableLiveData<>();*/

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
    //date time
    private String date_time;
    private MutableLiveData<String> date_timeLive = new MutableLiveData<>();

    //Img List
    private List<ImgList> imgLists = new ArrayList<>();
    private MutableLiveData<List<ImgList>> imgListsMutableLiveData = new MutableLiveData<>();

    //Img func List
    private MutableLiveData<List<ImgFunList>> img_funMutableList = new MutableLiveData<>();
    private List<ImgFunList> img_funLists = new ArrayList<>();

    //Img Func title
    private String img_func_title;
    private MutableLiveData<String> img_func_titleMutable = new MutableLiveData<>();

    //Img Func Hash map
    private LinkedHashMap<String, List<ImgList>> if_f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<ImgList>>> if_f_mapMutableLiveData = new MutableLiveData<>();

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

    //Session Alert
    private Integer alert;
    private MutableLiveData<Integer> alertMutable = new MutableLiveData<>();

    //user selected list
    private List<UserItemList> userItemLists = new ArrayList<>();
    private MutableLiveData<List<UserItemList>> userItemListsMutableLiveData = new MutableLiveData<>();

    //my orders list
    private List<MyOrdersList> myOrdersList = new ArrayList<>();
    private MutableLiveData<List<MyOrdersList>> myOrdersListsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<LinkedHashMap<String, List<MyOrdersList>>> myordersHashMapMutable = new MutableLiveData<>();
    /*private LinkedHashMap<String, List<MyOrdersList>> f_mapMyorders = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<MyOrdersList>>> f_mapMyordersMutableLiveData = new MutableLiveData<>();*/
    private List<MyOrderFuncList> myOrderFuncLists = new ArrayList<>();
    private MutableLiveData<List<MyOrderFuncList>> myOrderFuncListsMutableLiveData = new MutableLiveData<>();
    private String func;

    //selected header and item list to view list
    private List<SelectedHeader> selectedHeadersList = new ArrayList<>();
    private MutableLiveData<List<SelectedHeader>> selectedHeadersListMutableLiveData = new MutableLiveData<>();

    //selected session and header in hash map
    /*private LinkedHashMap<String, List<SelectedHeader>> sh_f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<SelectedHeader>>> sh_f_mapMutableLiveData = new MutableLiveData<>();*/

    //selected Item list
    private List<OrderItemLists> orderItemLists = new ArrayList<>();
    private MutableLiveData<List<OrderItemLists>> orderItemListsMutableLiveData = new MutableLiveData<>();
    /*private LinkedHashMap<String, List<OrderItemLists>> orderItemList_f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<OrderItemLists>>> orderItemList_f_mapMutableLiveData = new MutableLiveData<>();*/


    //breadcrumbs
    private List<BreadCrumbList> breadCrumbLists = new ArrayList<>();
    private MutableLiveData<List<BreadCrumbList>> breadCrumbListsMutableLiveData = new MutableLiveData<>();
    private String TAG = "ViewClassModel";

    //Email check
    private List<CheckEmail> checkEmails = new ArrayList<>();
    private MutableLiveData<List<CheckEmail>> checkEmailsMutableLiveData = new MutableLiveData<>();

    //date picker alert
    private MutableLiveData<String> date_pickerMutable = new MutableLiveData<>();
    private String date_picker;
    //Time picker alert
    private MutableLiveData<String> time_pickerMutable = new MutableLiveData<>();
    private String timepicker;

    //SessionDateTime
    private List<SessionDateTime> sessionDateTimes = new ArrayList<>();
    private MutableLiveData<List<SessionDateTime>> sessionDateTimesMutableLiveData = new MutableLiveData<>();
    private LinkedHashMap<String, List<SessionDateTime>> f_mapsdt = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<SessionDateTime>>> f_mapsdtMutableLiveData = new MutableLiveData<>();
    //header map
    private LinkedHashMap<String, List<CheckedList>> headerMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<CheckedList>>> headerMapMutableLiveData = new MutableLiveData<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> sessionMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> sessionMapMutableLiveData = new MutableLiveData<>();
    //fun map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> funcMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>> funcMapMutableLiveData = new MutableLiveData<>();
    //selected Session list
    private List<SelectedSessionList> selectedSessionLists = new ArrayList<>();
    private MutableLiveData<List<SelectedSessionList>> selectedSessionListsMutableLiveData = new MutableLiveData<>();
    //when click session to (func) to show item list
    private String Func_Session;
    private MutableLiveData<String> Func_SessionMutable = new MutableLiveData<>();
    //Edit HashMap
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> editFunc_Map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>>> editFunc_MapMutableLiveData = new MutableLiveData<>();
    //date map
    private List<SelectedDateList> e_dateLists = new ArrayList<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editDateMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> editDateMapMutableLiveData = new MutableLiveData<>();
    //header map
    private List<SelectedHeader> e_selectedHeaders = new ArrayList<>();
    private MutableLiveData<List<SelectedHeader>> e_selectedHeadersLive = new MutableLiveData<>();
    private LinkedHashMap<String, List<SelectedHeader>> editHeaderMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<SelectedHeader>>> editHeaderMapMutableLiveData = new MutableLiveData<>();
    //session map
    private List<SelectedSessionList> e_sessionLists = new ArrayList<>();
    private MutableLiveData<List<SelectedSessionList>> e_sessionListsLive = new MutableLiveData<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> editSessionMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> editSessionMapMutableLiveData = new MutableLiveData<>();
    //get selected map in place order
    private List<LinkedHashMap<String, List<CheckedList>>> edit_selected_s_map = new ArrayList<>();
    private LinkedHashMap<String, List<CheckedList>> e_headerMap = new LinkedHashMap<>();
    private List<CheckedList> e_checkedLists = new ArrayList<>();

    //Cancel
    //Session Alert
    private Integer ecd;
    private MutableLiveData<Integer> ecdLive = new MutableLiveData<>();
    private List<SelectedHeader> c_selectedHeaders = new ArrayList<>();

    //My Order HashMap
    private List<OrderItemLists> o_orderItemLists = new ArrayList<>();
    //private List<SelectedHeader> o_selectedHeadersList=new ArrayList<>();
    //private List<MyOrdersList> o_myOrdersList=new ArrayList<>();
    //private List<MyOrderFuncList> o_myOrderFuncLists=new ArrayList<>();
    //private List<SelectedSessionList> o_selectedSessionLists=new ArrayList<>();
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>>> orderFunc_Map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>>>> orderFunc_MapMutableLiveData = new MutableLiveData<>();
    //date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> orderDateMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>>> orderDateMapMutableLiveData = new MutableLiveData<>();
    //header map
    private LinkedHashMap<String, List<OrderItemLists>> orderHeaderMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<OrderItemLists>>> orderHeaderMapMutableLiveData = new MutableLiveData<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> orderSessionMapMutableLiveData = new MutableLiveData<>();
    //head count
    private String s_count;
    private MutableLiveData<String> s_countLiveData=new MutableLiveData<>();

    public GetViewModel(@NonNull Application application) {
        super(application);
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        CheckUserDetails();


    }

    public MutableLiveData<String> getS_countLiveData() {
        return s_countLiveData;
    }

    public void setS_count(String s_count) {
        this.s_count = s_count;
        this.s_countLiveData.postValue(s_count);
    }

    public void setOrderFunc_Map(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>>> orderFunc_Map) {
        this.orderFunc_Map = orderFunc_Map;
        this.orderFunc_MapMutableLiveData.postValue(orderFunc_Map);
    }

    public void setOrderHeaderMap(LinkedHashMap<String, List<OrderItemLists>> orderHeaderMap) {
        this.orderHeaderMap = orderHeaderMap;
        this.orderHeaderMapMutableLiveData.postValue(orderHeaderMap);
    }

    public void setOrderSessionMap(LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap) {
        this.orderSessionMap = orderSessionMap;
        this.orderSessionMapMutableLiveData.postValue(orderSessionMap);
    }

    public MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>>> getOrderDateMapMutableLiveData() {
        return orderDateMapMutableLiveData;
    }

    public MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>>>> getOrderFunc_MapMutableLiveData() {
        return orderFunc_MapMutableLiveData;
    }

    public MutableLiveData<LinkedHashMap<String, List<OrderItemLists>>> getOrderHeaderMapMutableLiveData() {
        return orderHeaderMapMutableLiveData;
    }

    public MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> getOrderSessionMapMutableLiveData() {
        return orderSessionMapMutableLiveData;
    }

    public MutableLiveData<Integer> getEcdLive() {
        return ecdLive;
    }

    public void setEcd(Integer ecd) {
        this.ecd = ecd;
        this.ecdLive.postValue(ecd);
    }

    public MutableLiveData<String> getDate_timeLive() {
        return date_timeLive;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
        this.date_timeLive.postValue(date_time);
    }

    public void setE_selectedHeaders(List<SelectedHeader> e_checkedLists) {
        this.e_selectedHeaders = e_checkedLists;
        this.e_selectedHeadersLive.postValue(e_checkedLists);
    }

    public MutableLiveData<List<SelectedHeader>> getE_selectedHeadersLive() {
        return e_selectedHeadersLive;
    }

    public void setE_sessionLists(List<SelectedSessionList> e_sessionLists) {
        this.e_sessionLists = e_sessionLists;
        this.e_sessionListsLive.postValue(e_sessionLists);

    }

    public MutableLiveData<List<SelectedSessionList>> getE_sessionListsLive() {
        return e_sessionListsLive;
    }

    public void setEditHeaderMap(LinkedHashMap<String, List<SelectedHeader>> editHeaderMap) {
        this.editHeaderMap = editHeaderMap;
        this.editHeaderMapMutableLiveData.postValue(editHeaderMap);
    }


    public MutableLiveData<LinkedHashMap<String, List<SelectedHeader>>> getEditHeaderMapMutableLiveData() {
        return editHeaderMapMutableLiveData;
    }

    public void setEditSessionMap(LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>> editSessionMap) {
        this.editSessionMap = editSessionMap;
        this.editSessionMapMutableLiveData.postValue(editSessionMap);
    }

    public MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>> getEditSessionMapMutableLiveData() {
        return editSessionMapMutableLiveData;
    }

    public void setEditFuncMap(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> editMap) {
        this.editFunc_Map = editMap;
        this.editFunc_MapMutableLiveData.postValue(editMap);

    }


    public MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>>> getEditFuncMapMutableLiveData() {
        return editFunc_MapMutableLiveData;
    }


    public void setFunc_Session(String func_Session) {

        Func_Session = func_Session;
        this.Func_SessionMutable.postValue(Func_Session);
    }

    public MutableLiveData<String> getFunc_SessionMutable() {
        return Func_SessionMutable;
    }


    public void setHeaderMap(LinkedHashMap<String, List<CheckedList>> headerMap) {
        this.headerMap = headerMap;
        this.headerMapMutableLiveData.postValue(headerMap);
    }

    public MutableLiveData<LinkedHashMap<String, List<CheckedList>>> getHeaderMapMutableLiveData() {
        return headerMapMutableLiveData;
    }

    public void setTimepicker(String timepicker) {
        this.timepicker = timepicker;
        this.time_pickerMutable.postValue(timepicker);
    }

    public void setSelectedSessionLists(List<SelectedSessionList> selectedSessionLists) {
        this.selectedSessionLists = selectedSessionLists;
        this.selectedSessionListsMutableLiveData.postValue(selectedSessionLists);
    }

    public MutableLiveData<List<SelectedSessionList>> getSelectedSessionListsMutableLiveData() {
        return selectedSessionListsMutableLiveData;
    }

    public void setAlert(Integer alert) {
        this.alert = alert;
        this.alertMutable.postValue(alert);
    }

    public MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> getSessionMapMutableLiveData() {
        return sessionMapMutableLiveData;
    }

    public void setSessionMap(LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> sessionMap) {
        this.sessionMap = sessionMap;
        this.sessionMapMutableLiveData.postValue(sessionMap);
    }

    public MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>> getFuncMapMutableLiveData() {
        return funcMapMutableLiveData;
    }


    public void setFuncMap(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> funcMap) {
        this.funcMap = funcMap;
        this.funcMapMutableLiveData.postValue(funcMap);
    }

    public MutableLiveData<Integer> getAlertMutable() {
        return alertMutable;
    }

    public MutableLiveData<String> getTime_pickerMutable() {
        return time_pickerMutable;
    }


    private void CheckUserDetails() {
        checkEmails = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("Users-Id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "snap>>" + snapshot);
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "error>>at firebase  emails " + datas.child("email").getValue().toString());
                    CheckEmail checkEmails1 = new CheckEmail(
                            datas.child("email").getValue().toString()
                    );
                    checkEmails.add(checkEmails1);
                }
                MyLog.e(TAG, "errors>>at firebase  emails out " + check_email);
                checkEmailsMutableLiveData.postValue(checkEmails);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "fail to get data " + error.getMessage());
            }
        });
    }


    public void setDate_picker(String date_picker) {
        this.date_picker = date_picker;
        this.date_pickerMutable.postValue(date_picker);
    }

    public MutableLiveData<String> getDate_pickerMutable() {
        return date_pickerMutable;
    }


    public MutableLiveData<List<CheckEmail>> getCheckEmailsMutableLiveData() {
        return checkEmailsMutableLiveData;
    }

    public void setSessionDateTimes(List<SessionDateTime> sessionDateTimes) {
        this.sessionDateTimes = sessionDateTimes;
        this.sessionDateTimesMutableLiveData.postValue(sessionDateTimes);
    }

    public MutableLiveData<List<SessionDateTime>> getSessionDateTimesMutableLiveData() {
        return sessionDateTimesMutableLiveData;
    }

    public MutableLiveData<LinkedHashMap<String, List<ImgList>>> getIf_f_mapMutableLiveData() {
        return if_f_mapMutableLiveData;
    }

    public MutableLiveData<LinkedHashMap<String, List<SessionDateTime>>> getF_mapsdtMutableLiveData() {
        return f_mapsdtMutableLiveData;
    }


/*    public MutableLiveData<LinkedHashMap<String, List<SelectedHeader>>> getSh_f_mapMutableLiveData() {
        return sh_f_mapMutableLiveData;
    }

    public MutableLiveData<LinkedHashMap<String, List<SelectedSessionList>>> getSs_f_mapMutableLiveData() {
        return ss_f_mapMutableLiveData;
    }

    public MutableLiveData<LinkedHashMap<String, List<MyOrdersList>>> getF_mapMyordersMutableLiveData() {
        return f_mapMyordersMutableLiveData;
    }*/

    public void setBreadCrumbLists(List<BreadCrumbList> breadCrumbLists) {
        this.breadCrumbLists = breadCrumbLists;
        this.breadCrumbListsMutableLiveData.postValue(breadCrumbLists);
    }

    public MutableLiveData<List<SessionList>> getSessionListMutable() {
        return sessionListMutable;
    }

    public MutableLiveData<LinkedHashMap<String, List<TimeList>>> getTimeListF_MapMutableLiveData() {
        return timeListF_MapMutableLiveData;
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

    public void GetSessionTime() {
        databaseReference = firebaseDatabase.getReference("Items").child("Selected&UnSelected").child("SessionTime");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "Session>>snap>>fun>>" + snapshot);
                int size = 0;

                for (DataSnapshot datas : snapshot.getChildren()) {
                    timeList = new ArrayList<>();
                    session_title = datas.getKey().toString();
                    TimeList timeList1 = new TimeList(
                            datas.getValue().toString());
                    timeList.add(timeList1);
                    size++;
                    timeListF_Map.put(session_title, timeList);

                }
                timeListF_MapMutableLiveData.postValue(timeListF_Map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "fail to get data " + error.getMessage());
            }
        });
    }

    public void GetSession() {

        databaseReference = firebaseDatabase.getReference("Items").child("Selected&UnSelected").child("Session");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "Session>>snap>>fun>>" + snapshot);
                int size = 0;
                sessionList = new ArrayList<>();
                for (DataSnapshot datas : snapshot.getChildren()) {
                    SessionList sessionList1 = new SessionList(
                            datas.getValue().toString());
                    sessionList.add(sessionList1);
                    size++;

                }
                sessionListMutable.postValue(sessionList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "fail to get data " + error.getMessage());
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
                //o_myOrderFuncLists = new ArrayList<>();
                for (DataSnapshot datas : snapshot.getChildren()) {
                    //o_selectedSessionLists = new ArrayList<>();
                    MyLog.e(TAG, "onData>>datas>>" + datas);
                    MyLog.e(TAG, "MyOrdersAdapter>>func>>" + datas.getKey().toString());
                    func = datas.getKey().toString();
                    orderDateMap = new LinkedHashMap<>();
                    for (DataSnapshot dataTime : datas.getChildren()) {
                        String date = dataTime.getKey().toString();
                        orderSessionMap = new LinkedHashMap<>();
                        for (DataSnapshot ondata : dataTime.getChildren()) {
                            String ss = ondata.getKey().toString();
                            String[] cs = ss.split("-");
                            String[] str = cs[1].split("_");
                            session_title = cs[0];
                            MyLog.e(TAG, "MyOrdersAdapter>>session_title>>" + ondata.getKey().toString());
                            //o_myOrdersList = new ArrayList<>();
                            //o_selectedHeadersList = new ArrayList<>();
                            orderHeaderMap = new LinkedHashMap<>();
                            for (DataSnapshot dataSnapshot : ondata.getChildren()) {
                                //get item list
                                o_orderItemLists = new ArrayList<>();

                                header_title = dataSnapshot.getKey().toString();
                                MyLog.e(TAG, "onData>>dataonData>>" + dataSnapshot);
                                MyLog.e(TAG, "onData>>header_title>>" + dataSnapshot.getKey().toString());
                                size = 0;
                                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                                    MyLog.e(TAG, "onData>>shots>>" + shot);
                                    MyLog.e(TAG, "onData>>shots>>" + shot.getKey().toString());
                                    //get item list
                                    OrderItemLists itemLists = new OrderItemLists(
                                            shot.getValue().toString()
                                    );
                                    o_orderItemLists.add(itemLists);
                                    size++;
                                }

                                MyLog.e(TAG, "onData>>size>" + size);
                                orderHeaderMap.put(header_title, o_orderItemLists);


                            }
                            orderSessionMap.put(ss, orderHeaderMap);

                        }
                        orderDateMap.put(date, orderSessionMap);
                    }
                    orderFunc_Map.put(func, orderDateMap);
                }

                //set func map
                orderFunc_MapMutableLiveData.postValue(orderFunc_Map);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "fail to get data " + error.getMessage());
            }
        });

    }

   /* public MutableLiveData<LinkedHashMap<String, List<OrderItemLists>>> getOrderItemList_f_mapMutableLiveData() {
        return orderItemList_f_mapMutableLiveData;
    }*/

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
        MyLog.e(TAG, "func_title>>set>>" + func_title);
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

    public MutableLiveData<List<ImgFunList>> getImg_funMutableList() {
        return img_funMutableList;
    }

    public void GetImg() {

        databaseReference = firebaseDatabase.getReference("Items").child("Selected&UnSelected").child("Img");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //MyLog.e(TAG, "img>>" + snapshot);

                for (DataSnapshot snap : snapshot.getChildren()) {

                    img_funLists = new ArrayList<>();
                    for (DataSnapshot shot : snap.getChildren()) {
                        imgLists = new ArrayList<>();
                        img_func_title = shot.getKey().toString();
                        //MyLog.e(TAG, "img>>shot::key>>" + shot.getKey().toString());
                        ImgList list = new ImgList(
                                shot.getValue().toString()
                        );

                        //set img url list
                        imgLists.add(list);
                        if_f_map.put(img_func_title, imgLists);
                        //set img func list
                        ImgFunList funList = new ImgFunList(
                                img_func_title
                        );
                        img_funLists.add(funList);
                    }


                }

                //set img fun list
                img_funMutableList.postValue(img_funLists);
                //set img url list
                if_f_mapMutableLiveData.postValue(if_f_map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // we are showing that error message in toast
                Toast.makeText(getApplication(), "Error Loading Image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void GetItem() {
        databaseReference = firebaseDatabase.getReference("Items").child("Selected&UnSelected").child("List");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               /* MyLog.e(TAG, "list>>snap>>" + snapshot);
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
                    f_maps.put(headerList.get(k).getHeader(), itemLists);
                    size++;
                }

                s_map.add(f_maps);
                s_mapMutable.postValue(s_map);*/

                headerList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //MyLog.e(TAG, "list>>datasnapshot>>" + dataSnapshot);
                    MyLog.e(TAG, "list>>datasnapshot>>key>>" + dataSnapshot.getKey().toString());
                    header_title = dataSnapshot.getKey().toString();
                    itemLists = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        //MyLog.e(TAG, "list>>data>>" + data);
                        MyLog.e(TAG, "list>>data>>key>>" + data.getKey().toString());
                        ItemList itemLists1 = new ItemList();
                        itemLists1.setItem(data.getKey().toString());
                        itemLists1.setSelected(data.getValue().toString());
                        itemLists.add(itemLists1);
                        f_maps.put(header_title, itemLists);
                    }
                    HeaderList headerList1 = new HeaderList(
                            header_title
                    );
                    headerList.add(headerList1);
                }
                headerListMutableList.postValue(headerList);

                s_map.add(f_maps);
                s_mapMutable.postValue(s_map);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "fail to get data " + error.getMessage());
            }
        });
    }

    private void GetUserDeatils(String email) {
        MyLog.e(TAG, "errors>> GetUserDeatils");
        check_email = false;
        databaseReference = firebaseDatabase.getReference("Users-Id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "snap>>" + snapshot);
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "error>>at firebase  emails " + datas.child("email").getValue().toString());
                    if (Objects.equals(email, datas.child("email").getValue().toString())) {
                        MyLog.e(TAG, "errors>>at firebase  emails if " + check_email);
                        String email = datas.child("email").getValue().toString();
                        String username = datas.child("username").getValue().toString();
                        String phone_number = datas.child("phone_number").getValue().toString();
                        if (email != null) {
                            new SharedPreferences_data(getApplication()).setS_email(email);
                        }
                        if (username != null) {
                            new SharedPreferences_data(getApplication()).setS_user_name(datas.child("username").getValue().toString());
                        }
                        if (phone_number != null) {
                            new SharedPreferences_data(getApplication()).setS_phone_number(datas.child("phone_number").getValue().toString());
                        }
                        check_email = true;


                        break;
                    } else {
                        check_email = false;
                        continue;

                    }

                }
                MyLog.e(TAG, "errors>>at firebase  emails out " + check_email);
                EmailMutable.postValue(check_email);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "fail to get data " + error.getMessage());
            }
        });

    }



    public MutableLiveData<List<FunList>> getFunMutableList() {
        return funMutableList;
    }

    public void GetFun() {
        databaseReference = firebaseDatabase.getReference("Items").child("Selected&UnSelected").child("Function");

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
                MyLog.e(TAG, "fail to get data " + error.getMessage());
            }
        });
    }

    public void GetHeader() {
        databaseReference = firebaseDatabase.getReference("Items").child("Selected&UnSelected").child("Category");

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
                MyLog.e(TAG, "fail to get data " + error.getMessage());
            }
        });
    }


    public void getheaderFragment(String header, int position, List<LinkedHashMap<String, List<ItemList>>> linkedHashMaps) {

        header_title_Mutable.postValue(header);
        List<ItemList> itemLists = s_map.get(0).get(header);
        //itemLists=itemLists1;
        this.itemHeaderMutable.postValue(itemLists);
        MyLog.e(TAG, "itm>nut>>" + itemLists.size());
        if (itemLists.size() > 0) {
            value.postValue(2);

        } else {

            Toast.makeText(getApplication(), "Empty Response", Toast.LENGTH_SHORT).show();
        }
    }

    public void getfunFragment(String fun) {
        MyLog.e(TAG, "func_title>>get>>" + func_title);
        this.setI_value(6);
        this.func_title_Mutable.postValue(fun);
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


    public void CheckTime(String s_session_title, String s_date_picker_actions, int hourOfDay, int minute, String funcTitle) {
        String t_time;

        t_time = (String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));

        String date_time = s_date_picker_actions + " " + t_time;
        MyLog.e(TAG, "dateTime>>date_time>>" + date_time);
        MyLog.e(TAG, "dateTime>>s_session_title>>" + s_session_title);
        String start_dt, end_dt;
        if (s_session_title.equals("Breakfast") || s_session_title.equals("Break Fast")) {

            start_dt = s_date_picker_actions + " 06:00";
            end_dt = s_date_picker_actions + " 11:00";

            alertTime(s_session_title, start_dt, end_dt, date_time, hourOfDay, minute, s_date_picker_actions, funcTitle);


        } else if (s_session_title.equals("Lunch")) {
            start_dt = s_date_picker_actions + " 12:00";
            end_dt = s_date_picker_actions + " 16:00";
            alertTime(s_session_title, start_dt, end_dt, date_time, hourOfDay, minute, s_date_picker_actions, funcTitle);
        } else if (s_session_title.equals("Dinner")) {
            start_dt = s_date_picker_actions + " 17:00";
            end_dt = s_date_picker_actions + " 22:00";
            alertTime(s_session_title, start_dt, end_dt, date_time, hourOfDay, minute, s_date_picker_actions, funcTitle);

        }


    }

    private void alertTime(String s_session_title, String start_dt, String end_dt, String date_time, int hourOfDay, int minute, String s_date_picker_actions, String funcTitle) {

          /* MyLog.e(TAG,"dateTime>>startdate_time>>"+start_dt);
            MyLog.e(TAG,"dateTime>>endDateTime>>"+end_dt);
            MyLog.e(TAG,"dateTime>>start>>"+date_time.compareTo(start_dt));
            MyLog.e(TAG,"dateTime>>end>>"+date_time.compareTo(end_dt));*/

        if (date_time.compareTo(start_dt) >= 0 && date_time.compareTo(end_dt) <= 0) {
            boolean isPM = (hourOfDay >= 12);
            String time = String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM");
            time_pickerMutable.postValue(time);
            sessionDateTimes = new ArrayList<>();
            MyLog.e(TAG, "time>>" + time + "\tdate>>" + s_date_picker_actions);
            SessionDateTime list = new SessionDateTime(
                    s_date_picker_actions,
                    time
            );
            sessionDateTimes.add(list);
            sessionDateTimesMutableLiveData.postValue(sessionDateTimes);
            String d = funcTitle + "-" + s_session_title;
            f_mapsdt.put(d, sessionDateTimes);
            f_mapsdtMutableLiveData.postValue(f_mapsdt);


        } else {
            alertMutable.postValue(0);

        }


    }

    public void EditMap(String func_title,  String header, String item, int position, int n, String username, String sess, String date) {
        MyLog.e(TAG, "cancel>>value " + n);
        MyLog.e(TAG, "s_count>>sess>>" + sess);
        String[] scb=sess.split("-");
        String[] cb=(scb[1]).split("_");
        String session_title=scb[0];
        String s_count=cb[0];

        MyLog.e(TAG, "s_count>>title>>" + session_title);
        MyLog.e(TAG, "s_count>>count>>" + s_count);
        /////////*****Edit **********//////////////////
        //set header map
        SelectedHeader selectedHeader = new SelectedHeader(
                item
        );
        e_selectedHeaders.add(selectedHeader);
        editHeaderMap.put(header, e_selectedHeaders);
        //set
        editHeaderMapMutableLiveData.postValue(editHeaderMap);


        //set SelectedSessionList map
        SelectedSessionList sessionList = new SelectedSessionList(

        );
        String[] li = session_title.split("!");
        sessionList.setSession_title(li[0]);
        sessionList.setBolen(null);
        sessionList.setTime(li[1]);
        sessionList.setS_count(s_count);
        e_sessionLists.add(sessionList);
        String s=session_title+"/"+s_count;
        editSessionMap.put(s, editHeaderMap);
        //set
        editSessionMapMutableLiveData.postValue(editSessionMap);

        //set date map
        editDateMap.put(date, editSessionMap);
        //set func map
        editFunc_Map.put(func_title, editDateMap);
        //set
        editFunc_MapMutableLiveData.postValue(editFunc_Map);


    }

    //date,ses,dTime,b
    public void getSelecteds_map(String date, String ses, String dTime, String b, String count) {

        //get header map
        String sd = ses + "!" + dTime+"/"+count;

        editHeaderMap = new LinkedHashMap<>(editSessionMap).get(sd);

        //set session title live
        session_titleMutable.postValue(ses);
        //set date picker
        date = date.replace("-", "/");
        date_pickerMutable.postValue(date);
        //set time picker
        time_pickerMutable.postValue(dTime);
        //set head count
        s_countLiveData.postValue(count);


        //get header list
        Set<String> stringSet = editHeaderMap.keySet();
        List<String> aList = new ArrayList<String>(stringSet.size());
        for (String x : stringSet)
            aList.add(x);

        //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
        e_selectedHeaders.clear();
        for (int i = 0; i < aList.size(); i++) {
            //set selected header list
            SelectedHeader list = new SelectedHeader(
                    aList.get(i)
            );
            e_selectedHeaders.add(list);
        }
        /*selectedHeadersList.clear();
        for (int i = 0; i < e_selectedHeaders.size(); i++) {
            header_title = e_selectedHeaders.get(i).getHeader();
            MyLog.e(TAG, "edit>>header_title>>" + header_title);
            //set item list from edit header map
            selectedHeadersList = editHeaderMap.get(header_title);
            //set item list from header map in data base
            itemLists = f_maps.get(header_title);

        }
        edit_selected_s_map.clear();
        e_checkedLists.clear();
        e_headerMap = new LinkedHashMap<>();
        for (int s = 0; s < selectedHeadersList.size(); s++) {
            for (int i = 0; i < itemLists.size(); i++) {
                //check the item list and get position
                if ((selectedHeadersList.get(s).getHeader()).equals(itemLists.get(i).getItem())) {
                    CheckedList checkedList = new CheckedList(
                            selectedHeadersList.get(s).getHeader(),
                            i
                    );
                    e_checkedLists.add(checkedList);
                    e_headerMap.put(header_title, e_checkedLists);
                }
            }
        }*/
        //get item list using selected headers list
        itemLists=new ArrayList<>();
        selectedHeadersList=new ArrayList<>();
        edit_selected_s_map=new ArrayList<>();
        e_headerMap = new LinkedHashMap<>();
        for (int k = 0; k < e_selectedHeaders.size(); k++) {
            header_title = e_selectedHeaders.get(k).getHeader();
            selectedHeadersList = (editHeaderMap).get(header_title);
            //set item list from header map in data base
            itemLists = f_maps.get(header_title);
            e_checkedLists=new ArrayList<>();
            for (int s = 0; s < selectedHeadersList.size(); s++) {
                String sItem = selectedHeadersList.get(s).getHeader();
                for (int i = 0; i < itemLists.size(); i++) {
                    String item = itemLists.get(i).getItem();
                    //check the item list and get position
                    if ((sItem).equals(item)) {
                        CheckedList checkedList = new CheckedList(
                                item,
                                i
                        );
                        e_checkedLists.add(checkedList);
                        MyLog.e(TAG, "edit>>selected header_title>>" + header_title);
                        e_headerMap.put(header_title, e_checkedLists);

                    }
                }
            }
        }
        edit_selected_s_map.add(e_headerMap);
        check_s_mapMutable.postValue(edit_selected_s_map);


    }

    public void CancelOrders(String func_title, String session_title, int n, String s_user_name, String bolen, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedHeader>>>>> editFunc_Maps, String date) {
        MyLog.e(TAG, "cancel ");
        MyLog.e(TAG, "cancel "+session_title);
        String[] str = session_title.split("_");
        String[] scb = (str[0]).split("-");
        String count=scb[1];
        String sess = scb[0];
        String old = str[1];
        String s=sess+"/"+count;

        editDateMap = editFunc_Maps.get(func_title);
        editSessionMap = editDateMap.get(date);
        editHeaderMap = editSessionMap.get(s);
        if (editHeaderMap == null) {
            editHeaderMap = new LinkedHashMap<>();
            MyLog.e(TAG, "Cancel>>sess_date editheaderMap is null");
        } else {
            //get header list
            Set<String> stringSet = editHeaderMap.keySet();
            List<String> aList = new ArrayList<String>(stringSet.size());
            for (String x : stringSet)
                aList.add(x);

            //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
            c_selectedHeaders.clear();
            for (int i = 0; i < aList.size(); i++) {
                //set selected header list
                SelectedHeader list = new SelectedHeader(
                        aList.get(i)
                );
                c_selectedHeaders.add(list);
            }


            //remove old data
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Orders").child(s_user_name);
            MyLog.e(TAG, "cancel>>sess value  " + session_title);
            MyLog.e(TAG, "cancel>>date value  " + date);

            //remove data
            databaseReference.child(func_title).child(date).child(session_title).removeValue();
            MyLog.e(TAG, "cancel remove commit");


            if (old.equals("true")) {
                old = "false";
            } else if (old.equals("false")) {
                old = "true";
            }
            if (n == 1) {

                //add new data
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Orders").child(s_user_name);
                String newData =sess+"-"+count+ "_" + old;
                MyLog.e(TAG, "cancel>> value  " + newData);
                MyLog.e(TAG, "cancel add commit");
                //set replace bolen
                selectedHeadersList.clear();
                for (int l = 0; l < c_selectedHeaders.size(); l++) {
                    header_title = c_selectedHeaders.get(l).getHeader();
                    selectedHeadersList = editHeaderMap.get(header_title);
                    for (int k = 0; k < selectedHeadersList.size(); k++) {
                        databaseReference.child(func_title).child(date).child(newData).child(header_title).child(String.valueOf(k)).setValue(selectedHeadersList.get(k).getHeader());
                    }
                }
            }
        }

    }
    public void DeleteDate(String s_user_name, String funcTitle, String gn_date) {
        //remove old data
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");
        MyLog.e(TAG, "dates>> funcTitle  " + funcTitle);
        MyLog.e(TAG, "dates>> s_user_name  " + s_user_name);
        MyLog.e(TAG, "dates>>date   " + gn_date);

        //remove data
        databaseReference.child(s_user_name).child(funcTitle).child(gn_date).removeValue();
        MyLog.e(TAG, "dates remove commit");
    }

}


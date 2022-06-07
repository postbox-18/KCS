package com.example.kcs.Fragment.Header;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Items.ItemList;

import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.ViewModel.TimeList;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeaderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //call from FunAdapter
    private TextView session_title;
    private String s_session_title;
    //Header
    private RecyclerView recyclerview_header;
    private HeaderAdapter headerAdapter;
    private ImageView back_btn;
    //HeaderAdapter.GetHeaderFragment getheaderFragment;
    private List<HeaderList> headerList = new ArrayList<>();
    //private MyViewModel myViewModel;
    private GetViewModel getViewModel;

    //date and time
    private TextView date_picker_actions;
    private TextView time_picker;
    private String funcTitle,s_time_picker,s_date_picker_actions;
    private DatePickerDialog datePicker;
    private List<TimeList> timeLists = new ArrayList<>();

    private LinkedHashMap<String, List<TimeList>> stringListLinkedHashMap = new LinkedHashMap<>();
    private List<SessionDateTime> sessionDateTimes = new ArrayList<>();
    private LinkedHashMap<String, List<SessionDateTime>> f_mapsdtMutable = new LinkedHashMap<>();
    private int mYear, mMonth, mDay, mHour, mMinute;

    private String TAG = "HeaderFragment";

    public HeaderFragment() {


        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeaderFragment newInstance(String param1, String param2) {
        HeaderFragment fragment = new HeaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  myViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
        getViewModel = new ViewModelProvider(getActivity()).get(GetViewModel.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        date_picker_actions = view.findViewById(R.id.date_picker_actions);
        time_picker = view.findViewById(R.id.time_picker);
        recyclerview_header = view.findViewById(R.id.recyclerview_header);
        session_title = view.findViewById(R.id.session_title);
        back_btn = view.findViewById(R.id.back_btn);


        // initialising the calendar
        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        // initialising the datepickerdialog
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePicker = new DatePickerDialog(getContext());
        }

        //load time picker
        //getViewModel.GetSessionTime();
        //get fun_title
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                funcTitle = s;
                MyLog.e(TAG, "datetime>>get >>" + funcTitle + "-" + s_session_title);

            }
        });




        //get time picker
        getViewModel.getTime_pickerMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                s_time_picker=s;
                    if (s == null) {
                        time_picker.setError("please select the time");
                    } else {
                        time_picker.setError(null);
                        time_picker.setText(s);
                    }

            }
        });

        //get date picker
        getViewModel.getDate_pickerMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                s_date_picker_actions=s;
                if(s==null)
                {
                    date_picker_actions.setError("Please select the date");
                }
                else
                {
                    date_picker_actions.setError(null);
                    date_picker_actions.setText(s);
                }
            }
        });

        //get time picker hash map
        getViewModel.getTimeListF_MapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, List<TimeList>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<TimeList>> stringListLinkedHashMap1) {
                stringListLinkedHashMap = stringListLinkedHashMap1;

            }
        });


        //get alert message
        getViewModel.getAlertMutable().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (integer == 0) {
                    if(s_session_title!=null) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("You Have Selected Time is More Than " + s_session_title + " Session");
                        alert.setTitle("Problem");
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }
                    else {
                        MyLog.e(TAG,"Session has empty value");
                    }
                }

            }
        });

        //get session date and time
        getViewModel.getSessionDateTimesMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<SessionDateTime>>() {
            @Override
            public void onChanged(List<SessionDateTime> sessionDateTimes1) {
                sessionDateTimes=sessionDateTimes1;
            }
        });
        //get view model session title
        getViewModel.getSession_titleMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                s_session_title = s;
                session_title.setText(s);
                MyLog.e(TAG,"dateTime>>session>>"+s_session_title);
                MyLog.e(TAG,"dateTime>>date>>"+s_date_picker_actions);
                MyLog.e(TAG,"dateTime>>time>>"+s_time_picker);

                if ((sessionDateTimes == null || sessionDateTimes.isEmpty()) &&((s_time_picker==null)||(s_time_picker.isEmpty()))) {

                        timeLists = stringListLinkedHashMap.get(s);
                        time_picker.setText(timeLists.get(0).getTimeList());
                        MyLog.e(TAG, "dateTime>>if");


                } else {
                    //setText Date and Time
                    for (int i = 0; i < sessionDateTimes.size(); i++) {
                        date_picker_actions.setText(sessionDateTimes.get(i).getDate());
                        time_picker.setText(sessionDateTimes.get(i).getTime());
                        //set date and time
                        getViewModel.setDate_picker(sessionDateTimes.get(i).getDate());
                        getViewModel.setTimepicker(sessionDateTimes.get(i).getTime());

                        MyLog.e(TAG,"dateTime>>else");
                    }

                }

            }
        });
        //get SessionDateTime Hash Map
        /*getViewModel.getF_mapsdtMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, List<SessionDateTime>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SessionDateTime>> stringListLinkedHashMap) {
                f_mapsdtMutable = stringListLinkedHashMap;
                MyLog.e(TAG,"dateTime>> gson>>"+new GsonBuilder().setPrettyPrinting().create().toJson(f_mapsdtMutable));
                MyLog.e(TAG, "datetime>>set >>" + funcTitle + "-" + s_session_title);
                sessionDateTimes = f_mapsdtMutable.get(funcTitle + "-" + s_session_title);
                getViewModel.setSessionDateTimes(sessionDateTimes);
                MyLog.e(TAG, "datetime>>set sessionDateTimes>>\n" + new GsonBuilder().setPrettyPrinting().create().toJson(sessionDateTimes));


            }
        });*/


        //get header list
        getViewModel.getHeaderListMutableList().observe(getViewLifecycleOwner(), new Observer<List<HeaderList>>() {
            @Override
            public void onChanged(List<HeaderList> headerLists) {
                headerList = headerLists;
            }
        });


        //get time to print value
        /*getViewModel.getTime_pickerMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                time_picker.setText(s);
            }
        });
*/


        //list hashmap of itemlist
        getViewModel.getS_mapMutable().observe(getViewLifecycleOwner(), new Observer<List<LinkedHashMap<String, List<ItemList>>>>() {
            @Override
            public void onChanged(List<LinkedHashMap<String, List<ItemList>>> linkedHashMaps) {
                //recyclerview_header
                recyclerview_header.setHasFixedSize(true);
                recyclerview_header.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                headerAdapter = new HeaderAdapter(getContext(), headerList, getViewModel, linkedHashMaps);
                getViewModel.setI_fragment(1);
                recyclerview_header.setAdapter(headerAdapter);
            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(6);
            }
        });


        //time picker
        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //time picker wehn select time and clik on ok btn alert the time is over lunch or breafast
                if ((date_picker_actions.getText().toString()) != null) {
                    // Get Current Time
                    final Calendar c = Calendar.getInstance();

                    //set selected time to show on alert machine
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    //String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"
                                    // txtTime.setText(hourOfDay + ":" + minute);


                                    //check condition if lunch or breakfast
                                    getViewModel.CheckTime(s_session_title, (date_picker_actions.getText().toString()), hourOfDay, minute, funcTitle);

                                    boolean isPM = (hourOfDay >= 12);
                                    time_picker.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(getContext(), "Please select date first", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //on click to select the date
        date_picker_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        date_picker_actions.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        getViewModel.setDate_picker(date_picker_actions.getText().toString());
                    }
                }, year, month, day);

                // set minimum date to be selected as today
                datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());

                // show the dialog
                datePicker.show();
            }
        });

        return view;
    }
    /*private void CheckTime(String s_session_title, String s_date_picker_actions, int hourOfDay, int minute) {
        String t_time=(String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute));
        String date_time=s_date_picker_actions+" "+t_time;
        MyLog.e(TAG,"dateTime>>date_time>>"+date_time);
        String start_dt,end_dt;
        if(s_session_title.equals("Breakfast")||s_session_title.equals("Break Fast"))
        {

            start_dt=s_date_picker_actions+" 06:00";
            end_dt=s_date_picker_actions+" 11:00";
            alertTime(s_session_title,start_dt,end_dt,date_time,hourOfDay,minute,s_date_picker_actions);


        }
        else if(s_session_title.equals("Lunch"))
        {
            start_dt=s_date_picker_actions+" 12:00";
            end_dt=s_date_picker_actions+" 16:00";
            alertTime(s_session_title,start_dt,end_dt,date_time,hourOfDay,minute, s_date_picker_actions);
        }
        else if(s_session_title.equals("Dinner"))
        {
            start_dt=s_date_picker_actions+" 17:00";
            end_dt=s_date_picker_actions+" 22:00";
            alertTime(s_session_title,start_dt,end_dt,date_time,hourOfDay,minute, s_date_picker_actions);

        }



    }

    private void alertTime(String s_session_title, String start_dt, String end_dt, String date_time, int hourOfDay, int minute, String s_date_picker_actions) {

          *//* MyLog.e(TAG,"dateTime>>startdate_time>>"+start_dt);
            MyLog.e(TAG,"dateTime>>endDateTime>>"+end_dt);
            MyLog.e(TAG,"dateTime>>start>>"+date_time.compareTo(start_dt));
            MyLog.e(TAG,"dateTime>>end>>"+date_time.compareTo(end_dt));*//*
        if(date_time.compareTo(start_dt)>=0 && date_time.compareTo(end_dt)<=0)
        {
            boolean isPM = (hourOfDay >= 12);
            time_picker.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
            getViewModel.setTimepicker(time_picker.getText().toString());
            MyLog.e(TAG, "time>> btn if" + time_picker.getText().toString());
            SessionDateTime list=new SessionDateTime(
                    s_session_title,
                    s_date_picker_actions,
                    String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM")
            );
            sessionDateTimes.add(list);
            f_mapsdtMutable.put(func,sessionDateTimes);

        }
        else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setMessage("You Have Selected Time is More Than "+s_session_title+" Session");
            alert.setTitle("Problem");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();

            time_picker.setError("Please select Time");

        }




    }*/


}
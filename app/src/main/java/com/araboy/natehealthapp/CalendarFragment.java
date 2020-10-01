package com.araboy.natehealthapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    Button btnViewMeals;
    CalendarView calendarView;
    TextView txtDate;
    String dateSelected;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);
        // Inflate the layout for this fragment
        calendarView = view.findViewById(R.id.calendar);
        txtDate = view.findViewById(R.id.myDate);
        btnViewMeals = view.findViewById(R.id.btnViewMeals);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1 + 1) + "/" + i2 + "/" + i;
                String month, day;
                month = formatMonth(i1);
                day = formatNum(i2);
                dateSelected = month + "-" + day + "-" + i;
                txtDate.setText(date);
            }
        });

        btnViewMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(),
                        DailyMealsActivity.class);
                intent.putExtra("Date", dateSelected);
                getActivity().startActivity(intent);
            }
        });


        return view;

    }

    public String formatNum(int i){
        if(i < 10){
            return "0" + i;
        } else {
            return i+"";
        }
    }

    public String formatMonth(int i){
        if(i + 1 < 10){
            return "0" + i +1;
        } else {
            return i+1+"";
        }
    }
}
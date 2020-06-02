package com.ayeshapp.spends;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentPeriodically extends Fragment {
    View view;
    TextView startEditDate;
    TextView finishEditDate;

    int sDay,sYear,sMon,eDay,eYear,eMon;

    String startDate = "", endDate = "";

    DatabaseHelper mydb;

    public FragmentPeriodically() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.periodically_fragment, container, false);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mydb = new DatabaseHelper(getContext());

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startDate = sdf.format(Calendar.getInstance().getTime());
        endDate = sdf.format(Calendar.getInstance().getTime());

        sYear = Calendar.getInstance().get(Calendar.YEAR);
        eYear = sYear;
        sMon = Calendar.getInstance().get(Calendar.MONTH);
        eMon = sMon;
        sDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        eDay = sDay;


        startEditDate = view.findViewById(R.id.start_date);
        startEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(1);
            }
        });

        finishEditDate = view.findViewById(R.id.finish_date);
        finishEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(2);
            }
        });

        startEditDate.setText(startDate);
        finishEditDate.setText(endDate);
    }

    private void showDatePicker(int pos) {
        DatePickerFragment date = new DatePickerFragment();
        if(pos == 1){
            Bundle args = new Bundle();
            args.putInt("year", sYear);
            args.putInt("month", sMon);
            args.putInt("day", sDay);
            date.setArguments(args);
            date.setCallBack(ondatestart);
        } else{
            Bundle args = new Bundle();
            args.putInt("year", eYear);
            args.putInt("month", eMon);
            args.putInt("day", eDay);
            date.setArguments(args);
            date.setCallBack(ondatefinish);

        }
        date.show(getFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondatestart = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            startDate = format(year,monthOfYear+1, dayOfMonth);
            sDay = dayOfMonth;
            sMon = monthOfYear;
            sYear = year;
            startEditDate.setText(startDate);
        }
    };

    DatePickerDialog.OnDateSetListener ondatefinish = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            endDate = format(year,monthOfYear+1, dayOfMonth);
            eDay = dayOfMonth;
            eMon = monthOfYear;
            eYear = year;
            finishEditDate.setText(endDate);
        }
    };

    public String format(int year, int month, int dayOfMonth) {
        String date = "";
        date+= year + "-";
        if (month < 10) date += "0";
        date+= month + "-";
        if (dayOfMonth < 10) date += "0";
        date += dayOfMonth;
        return date;
    }

}

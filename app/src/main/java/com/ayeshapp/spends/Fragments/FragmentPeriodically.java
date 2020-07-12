package com.ayeshapp.spends.Fragments;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayeshapp.spends.Adapters.OuterAdapter;
import com.ayeshapp.spends.Database.DatabaseHelper;
import com.ayeshapp.spends.Models.SpendModel;
import com.ayeshapp.spends.Models.OuterModel;
import com.ayeshapp.spends.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class FragmentPeriodically extends Fragment {
    View view;
    TextView startEditDate;
    TextView finishEditDate;
    TextView periodicallyTotal;
    Double periodTotal = 0.0;

    //ArrayList<String> dates;
    ArrayList<OuterModel> models;
    RecyclerView recyclerView;
    OuterAdapter adapter;

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
        periodicallyTotal = view.findViewById(R.id.periodically_total);

        Calendar cc = Calendar.getInstance();
        cc.setTimeZone(TimeZone.getDefault());

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startDate = sdf.format(cc.getTime());
        endDate = sdf.format(cc.getTime());

        sYear = cc.get(Calendar.YEAR);
        eYear = sYear;
        sMon = cc.get(Calendar.MONTH);
        eMon = sMon;
        sDay = cc.get(Calendar.DAY_OF_MONTH);
        eDay = sDay;

        //recyclerview
        //dates = new ArrayList<>();
        models = new ArrayList<>();
        adapter = new OuterAdapter(models,getContext());
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fetchData();
        adapter.notifyDataSetChanged();
        periodicallyTotal.setText(String.format("%.2f", periodTotal));

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
            models.clear();
            fetchData();
            adapter.notifyDataSetChanged();
            periodicallyTotal.setText(String.format("%.2f", periodTotal));
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
            models.clear();
            fetchData();
            adapter.notifyDataSetChanged();
            periodicallyTotal.setText(String.format("%.2f", periodTotal));
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

    void fetchData() {
        periodTotal = 0.0;
        Cursor res = mydb.getDataPeriodically(startDate,endDate);
        if(res.getCount() == 0) {
            Log.i("testing", startDate + " " + endDate+ " no data");
            return;
        }
        while (res.moveToNext()) {
            //dates.add(res.getString(1));
            viewAll(res.getString(0));
            Log.i("testing", res.getString(0));
        }
    }

    public void viewAll(String date) {
        //list.clear();
        Cursor res = mydb.getAllData(date);
        if(res.getCount() == 0) {
            return;
        }

        Double total = 0.0;
        ArrayList<SpendModel> model;
        model = new ArrayList<>();
        while (res.moveToNext()) {
            model.add(new SpendModel(res.getLong(0),
                    res.getString(1),
                    res.getString(2),
                    res.getDouble(3),
                    res.getDouble(4)));
            total += res.getDouble(4);
        }
        periodTotal += total;
        models.add(new OuterModel(date,total,model));

        //adapter.notifyDataSetChanged();
    }


}

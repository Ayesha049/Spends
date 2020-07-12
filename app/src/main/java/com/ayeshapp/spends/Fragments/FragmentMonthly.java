package com.ayeshapp.spends.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayeshapp.spends.Adapters.OuterAdapter;
import com.ayeshapp.spends.Database.DatabaseHelper;
import com.ayeshapp.spends.Models.SpendModel;
import com.ayeshapp.spends.Models.OuterModel;
import com.ayeshapp.spends.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class FragmentMonthly extends Fragment {
    View view;

    DatabaseHelper mydb;

    Toolbar toolbar;
    TextView monthlyTotal;
    Double monthTotal = 0.0;

    ArrayList<OuterModel> models;
    RecyclerView recyclerView;
    OuterAdapter adapter;
    int mn;
    int yr;
    String [] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER",
                        "OCTOBER", "NOVEMBER", "DECEMBER"};

    public FragmentMonthly() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.monthly_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mydb = new DatabaseHelper(getContext());

        Calendar cc = Calendar.getInstance();
        cc.setTimeZone(TimeZone.getDefault());

        toolbar = view.findViewById(R.id.toolbar);
        TextView header = toolbar.findViewById(R.id.heading);

        monthlyTotal = view.findViewById(R.id.monthly_total);

        mn = cc.get(Calendar.MONTH);
        yr = cc.get(Calendar.YEAR);

        String year = String.valueOf(yr);


        models = new ArrayList<>();
        adapter = new OuterAdapter(models,getContext());
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        header.setText( months[mn] + " " + year);

        fetchData(year, mn+1);
        adapter.notifyDataSetChanged();
        monthlyTotal.setText(String.format("%.2f", monthTotal));


        ImageView dec = toolbar.findViewById(R.id.left_arrow);
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mn == 0) {
                    mn = 11;
                    yr--;
                } else mn--;
                models.clear();
                header.setText( months[mn] + " " + yr);
                fetchData(String.valueOf(yr), mn+1);
                adapter.notifyDataSetChanged();
                monthlyTotal.setText(String.format("%.2f", monthTotal));
            }
        });
        ImageView inc = toolbar.findViewById(R.id.right_arrow);
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mn == 11) {
                    mn = 0;
                    yr++;
                } else mn++;
                models.clear();
                header.setText( months[mn] + " " + yr);
                fetchData(String.valueOf(yr), mn+1);
                adapter.notifyDataSetChanged();
                monthlyTotal.setText(String.format("%.2f", monthTotal));
            }
        });
    }

    void fetchData(String y, int mnn) {
        monthTotal = 0.0;
        String m;
        if (mnn < 10) {
            m = "0" + String.valueOf(mnn);
        } else {
            m = String.valueOf(mnn);
        }
        Cursor res = mydb.getDataMonthly(y,m);
        if(res.getCount() == 0) {
            Log.i("testing", y + " " + m + " no data");
            return;
        }
        while (res.moveToNext()) {
            //dates.add(res.getString(1));
            viewAll(res.getString(0));
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
        monthTotal+=total;
        models.add(new OuterModel(date,total,model));

        //adapter.notifyDataSetChanged();
    }
}

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

public class FragmentYearly extends Fragment {
    View view;

    DatabaseHelper mydb;
    Toolbar toolbar;

    ArrayList<OuterModel> models;
    RecyclerView recyclerView;
    OuterAdapter adapter;
    int yr;

    TextView yearlyTotal;
    Double yearTotal = 0.0;

    public FragmentYearly() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.yearly_fragment, container, false);
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

        yearlyTotal = view.findViewById(R.id.yearly_total);

        yr = cc.get(Calendar.YEAR);

        String year = String.valueOf(yr);

        models = new ArrayList<>();
        adapter = new OuterAdapter(models,getContext());
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        header.setText("YEAR " + year);
        fetchData(year);
        adapter.notifyDataSetChanged();
        yearlyTotal.setText(String.format("%.2f", yearTotal));

        ImageView dec = toolbar.findViewById(R.id.left_arrow);
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yr--;
                models.clear();
                header.setText("YEAR " + String.valueOf(yr));
                fetchData(String.valueOf(yr));
                adapter.notifyDataSetChanged();
                yearlyTotal.setText(String.format("%.2f", yearTotal));
            }
        });
        ImageView inc = toolbar.findViewById(R.id.right_arrow);
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yr++;
                models.clear();
                header.setText("YEAR " + String.valueOf(yr));
                fetchData(String.valueOf(yr));
                adapter.notifyDataSetChanged();
                yearlyTotal.setText(String.format("%.2f", yearTotal));
            }
        });

    }


    void fetchData(String y) {
        yearTotal = 0.0;
        Cursor res = mydb.getDataYearly(y);
        if(res.getCount() == 0) {
            Log.i("testing", y + " no data");
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
        yearTotal+=total;
        models.add(new OuterModel(date,total,model));

        //adapter.notifyDataSetChanged();
    }
}

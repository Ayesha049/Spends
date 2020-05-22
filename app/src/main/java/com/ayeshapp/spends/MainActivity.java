package com.ayeshapp.spends;

import android.app.Dialog;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CalendarView calender;

    String date = "";

    EditText name, amount, price;
    Button add;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    ArrayList<SpendModel> list;
    SpendAdapter adapter;

    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DatabaseHelper(this);
        calender = findViewById(R.id.calenderview);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new SpendAdapter(list);
        recyclerView.setAdapter(adapter);

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        date = sdf.format(new Date(calender.getDate()));
        viewAll(date);
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                month++;
                date = "";
                if (dayOfMonth < 10) date += "0";
                date += dayOfMonth + "/";
                if (month < 10) date += "0";
                date += month + "/" + year;
                list.clear();
                viewAll(date);
                adapter.notifyDataSetChanged();
            }
        });

        fab = findViewById(R.id.myFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_add);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        name = dialog.findViewById(R.id.item_name);
        amount = dialog.findViewById(R.id.item_quantity);
        price = dialog.findViewById(R.id.item_price);

        add = dialog.findViewById(R.id.btn_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String n = name.getText().toString().trim();
                String q = amount.getText().toString().trim();
                String p = price.getText().toString().trim();

                if (n.equals("") || q.equals("") || p.equals("")) {
                    Toast.makeText(MainActivity.this, "Please fill up",Toast.LENGTH_LONG).show();
                } else {
                    list.add(new SpendModel(date,n,Double.parseDouble(q),Double.parseDouble(p)));
                    mydb.insertData(date,n,q,p);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        dialog.show();
    }


    public void viewAll(String date) {
        //list.clear();
        Cursor res = mydb.getAllData(date);
        if(res.getCount() == 0) {
            return;
        }
        while (res.moveToNext()) {
            list.add(new SpendModel(res.getString(1),
                    res.getString(2),
                    res.getDouble(3),
                    res.getDouble(4)));
            //Toast.makeText(MainActivity.this, res.getString(1),Toast.LENGTH_LONG).show();
        }
        //adapter.notifyDataSetChanged();
    }
}

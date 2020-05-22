package com.ayeshapp.spends;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnSpendItemClick{

    CalendarView calender;

    String date = "";

    EditText name, amount, price;
    Button add;
    Button cancel;
    TextView total;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    ArrayList<SpendModel> list;
    SpendAdapter adapter;

    DatabaseHelper mydb;
    double totalCost = 0.0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DatabaseHelper(this);
        calender = findViewById(R.id.calenderview);
        total = findViewById(R.id.total);


        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new SpendAdapter(list, this);
        recyclerView.setAdapter(adapter);

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        date = sdf.format(new Date(calender.getDate()));
        viewAll(date);
        total.setText(String.format("%.2f", totalCost));
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
                totalCost = 0.0;
                viewAll(date);
                adapter.notifyDataSetChanged();
                total.setText(String.format("%.2f", totalCost));
            }
        });

        fab = findViewById(R.id.myFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }

        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /*@Override
            /*public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }*/
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy<0){
                    fab.show();
                }else{
                    fab.hide();
                }
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String n = name.getText().toString().trim();
                String q = amount.getText().toString().trim();
                String p = price.getText().toString().trim();

                if (n.equals("") || q.equals("") || p.equals("")) {
                    Toast.makeText(MainActivity.this, "Please fill up",Toast.LENGTH_LONG).show();
                } else {
                    //list.add(new SpendModel(date,n,Double.parseDouble(q),Double.parseDouble(p)));
                    totalCost += Double.parseDouble(p);
                    total.setText(String.format("%.2f", totalCost));
                    long id = mydb.insertData(date,n,q,p);
                    if (id != -1) {
                        list.add(new SpendModel(id, date,n,Double.parseDouble(q),Double.parseDouble(p)));
                        Log.e("testingid", Long.toString(id));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        cancel = dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showEditDialog(int pos) {
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
        final SpendModel model = list.get(pos);
        name.setText(model.getItemName());
        amount = dialog.findViewById(R.id.item_quantity);
        amount.setText(model.getAmount().toString() + " Kg");
        price = dialog.findViewById(R.id.item_price);
        price.setText(model.getPrice().toString() + " TK");



        add = dialog.findViewById(R.id.btn_add);
        add.setText("UPDATE");
        add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String n = name.getText().toString().trim();
                String q = amount.getText().toString().trim();
                String p = price.getText().toString().trim();

                if (n.equals("") || q.equals("") || p.equals("")) {
                    Toast.makeText(MainActivity.this, "Please fill up",Toast.LENGTH_LONG).show();
                } else {
                    //list.add(new SpendModel(date,n,Double.parseDouble(q),Double.parseDouble(p)));
                    totalCost -= model.getPrice();
                    totalCost += Double.parseDouble(p);
                    total.setText(String.format("%.2f", totalCost));
                    mydb.updateData(model.getId().toString(),date,n,q,p);
                    model.setItemName(n);
                    model.setAmount(Double.parseDouble(q));
                    model.setPrice(Double.parseDouble(p));
                    adapter.notifyDataSetChanged();
                }
            }
        });

        cancel = dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
            list.add(new SpendModel(res.getLong(0),
                    res.getString(1),
                    res.getString(2),
                    res.getDouble(3),
                    res.getDouble(4)));
            totalCost += res.getDouble(4);
            Log.e("testingid", res.getString(0));
            //Toast.makeText(MainActivity.this, res.getString(1),Toast.LENGTH_LONG).show();
        }

        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditClicked(int pos) {
        //Toast.makeText(MainActivity.this, "edit clicked" + Integer.toString(pos),Toast.LENGTH_LONG).show();
        showEditDialog(pos);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDeleteClicked(int pos) {
        //Toast.makeText(MainActivity.this, "delete clicked" + Integer.toString(pos),Toast.LENGTH_LONG).show();
        mydb.deleteData(list.get(pos).getId().toString());
        totalCost -= list.get(pos).getPrice();
        total.setText(String.format("%.2f", totalCost));
        list.remove(pos);
        adapter.notifyDataSetChanged();

    }
}

package com.ayeshapp.spends;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.ayeshapp.spends.Adapters.SpendAdapter;
import com.ayeshapp.spends.Database.DatabaseHelper;
import com.ayeshapp.spends.Interfaces.OnSpendItemClick;
import com.ayeshapp.spends.Models.SpendModel;
import com.ayeshapp.spends.ViewModels.SpendViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSpendItemClick {

    CalendarView calender;

    String date = "";

    EditText name, amount, price;
    Button add;
    Button cancel;
    TextView total;
    RecyclerView recyclerView;

    LinearLayout statistics, addItem;

    ArrayList<SpendModel> list;
    SpendAdapter adapter;

    DatabaseHelper mydb;
    double totalCost = 0.0;

    List<EventDay> events;
    List<Calendar> cals;

    RelativeLayout calLayout;
    int toogle = 1;

    ImageView upArrow, downArrow;

    private SpendViewModel viewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(SpendViewModel.class);

        statistics = findViewById(R.id.statistics);
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mydb = new DatabaseHelper(this);
        calender = findViewById(R.id.calendarView);
        calLayout = findViewById(R.id.calender_layout);
        total = findViewById(R.id.total);

        events = new ArrayList<>();
        cals = new ArrayList<>();
        initiate();
        calender.setHighlightedDays(cals);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new SpendAdapter(list, this);
        recyclerView.setAdapter(adapter);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(Calendar.getInstance().getTime());
        //Toast.makeText(MainActivity.this, date, Toast.LENGTH_LONG).show();
        viewAll(date);
        adapter.notifyDataSetChanged();
        total.setText(String.format("%.2f", totalCost));

        calender.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                int year, month, dayOfMonth;
                year = clickedDayCalendar.get(Calendar.YEAR);
                month = clickedDayCalendar.get(Calendar.MONTH);
                dayOfMonth = clickedDayCalendar.get(Calendar.DAY_OF_MONTH);
                month++;
                date = "";
                date+= year + "-";
                if (month < 10) date += "0";
                date+= month + "-";
                if (dayOfMonth < 10) date += "0";
                date += dayOfMonth;
                list.clear();
                totalCost = 0.0;
                viewAll(date);
                adapter.notifyDataSetChanged();
                total.setText(String.format("%.2f", totalCost));
                //Toast.makeText(MainActivity.this, date, Toast.LENGTH_LONG).show();
            }
        });

        addItem = findViewById(R.id.add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }

        });

        upArrow = findViewById(R.id.up_arrow);
        downArrow = findViewById(R.id.down_arrow);

        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calLayout.setVisibility(View.GONE);
                upArrow.setVisibility(View.GONE);
                downArrow.setVisibility(View.VISIBLE);
            }
        });

        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calLayout.setVisibility(View.VISIBLE);
                upArrow.setVisibility(View.VISIBLE);
                downArrow.setVisibility(View.GONE);
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

       // add = dialog.findViewById(R.id.btn_add);
        /*add.setOnClickListener(new View.OnClickListener() {
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
                    }
                    adapter.notifyDataSetChanged();
                    cals.clear();
                    initiate();
                    calender.setHighlightedDays(cals);
                }
            }
        });*/

        //cancel = dialog.findViewById(R.id.btn_cancel);
        /*cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

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
        amount.setText(model.getAmount().toString());
        price = dialog.findViewById(R.id.item_price);
        price.setText(model.getPrice().toString());



       // add = dialog.findViewById(R.id.btn_add);
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

        //cancel = dialog.findViewById(R.id.btn_cancel);
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
            //Toast.makeText(MainActivity.this, res.getString(1),Toast.LENGTH_LONG).show();
        }

        //adapter.notifyDataSetChanged();
    }

    public void initiate() {

        Cursor res = mydb.getDistincDates();
        if(res.getCount() == 0) {
            return;
        }
        while (res.moveToNext()) {
            String d = res.getString(0);
            String[] values = d.split("-");
            Log.e("datee", values[0] + " " + values[1] + " " + values[2]);
            int day = Integer.parseInt(values[2]);
            int month = Integer.parseInt(values[1]);
            month--;
            int year = Integer.parseInt(values[0]);

            Log.e("datee", Integer.toString(day) + " " + Integer.toString(month) + " " + Integer.toString(year));

            Calendar c = Calendar.getInstance();
            c.set(year,month,day);
            //events.add( new EventDay(c, R.drawable.sample_icon,  Color.parseColor("#228B22")) );
            cals.add(c);
        }
        calender.setHighlightedDays(cals);
        //calender.setEvents(events);
    }

    public static void showAlertDialog(Context context,
                                       String message,
                                       String positiveButtonMessage,
                                       String negativeButtonMesssage,
                                       DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(positiveButtonMessage, okListener)
                .setNegativeButton(negativeButtonMesssage, okListener)
                .create()
                .show();
    }

    public void showDeleteDiolog(final int pos)
    {
        showAlertDialog(MainActivity.this, "Sure to delete this item?", "Yes", "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    mydb.deleteData(list.get(pos).getId().toString());
                    totalCost -= list.get(pos).getPrice();
                    total.setText(String.format("%.2f", totalCost));
                    list.remove(pos);
                    adapter.notifyDataSetChanged();
                    cals.clear();
                    initiate();
                    calender.setHighlightedDays(cals);
                    dialogInterface.dismiss();
                } else if (i == DialogInterface.BUTTON_NEGATIVE) {
                    dialogInterface.dismiss();
                }
            }
        });
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
        showDeleteDiolog(pos);
    }
}

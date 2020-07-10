package com.ayeshapp.spends.ViewModels;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ayeshapp.spends.Database.DatabaseHelper;
import com.ayeshapp.spends.Models.SpendModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SpendViewModel extends AndroidViewModel {

    MutableLiveData<List<SpendModel>> spends;
    DatabaseHelper helper;

    HashMap<String, MutableLiveData<List<SpendModel>>> mymap;

    public SpendViewModel(@NonNull Application application) {
        super(application);
        init();

        Date date = Calendar.getInstance().getTime();
        String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sf.format(date);
        helper = new DatabaseHelper(application.getApplicationContext());

        spends.setValue(getAll(today));
        fetchData(year,month);

    }

    public void init() {
        if(spends == null) {
            spends = new MutableLiveData<>();
            List<SpendModel> list = new ArrayList<>();
            spends.setValue(list);
            mymap = new HashMap<>();
        }
    }

    public MutableLiveData<List<SpendModel>> getSpends() {
        return spends;
    }

    public MutableLiveData<List<SpendModel>> getSpend(String date) {
        return mymap.get(date);
    }


    private List<SpendModel> getAll(String date) {
        //list.clear();
        List<SpendModel> list = new ArrayList<>();
        Cursor res = helper.getAllData(date);
        if(res.getCount() == 0) {
            return list;
        }
        while (res.moveToNext()) {
            list.add(new SpendModel(res.getLong(0),
                    res.getString(1),
                    res.getString(2),
                    res.getDouble(3),
                    res.getDouble(4)));
            //Toast.makeText(MainActivity.this, res.getString(1),Toast.LENGTH_LONG).show();
        }
        return list;
        //adapter.notifyDataSetChanged();
    }

    private void fetchData(String y, String m) {
        Cursor res = helper.getDataMonthly(y,m);
        if(res.getCount() == 0) {
            return;
        }
        while (res.moveToNext()) {
            String date = res.getString(0);
            List<SpendModel> list= getAll(date);
            MutableLiveData<List<SpendModel>> lst = new MutableLiveData<>();
            lst.setValue(list);
            mymap.put(date,lst);
        }
    }
}

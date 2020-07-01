package com.ayeshapp.spends.ViewModels;

import android.app.Application;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ayeshapp.spends.Database.DatabaseHelper;
import com.ayeshapp.spends.Models.SpendModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SpendViewModel extends AndroidViewModel {

    MutableLiveData<List<SpendModel>> spends;
    DatabaseHelper helper;

    public SpendViewModel(@NonNull Application application) {
        super(application);
        init();

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sf.format(date);
        helper = new DatabaseHelper(application.getApplicationContext());

        spends.setValue(getAll(today));

    }

    public void init() {
        if(spends == null) {
            spends = new MutableLiveData<>();
            List<SpendModel> list = new ArrayList<>();
            spends.setValue(list);
        }
    }

    public MutableLiveData<List<SpendModel>> getSpends() {
        return spends;
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
}

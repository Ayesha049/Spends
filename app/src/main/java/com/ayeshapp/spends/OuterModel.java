package com.ayeshapp.spends;

import java.util.ArrayList;

public class OuterModel {
    String date;
    Double total;
    ArrayList<SpendModel> list;

    public OuterModel() {
    }

    public OuterModel(String date, Double total, ArrayList<SpendModel> list) {
        this.date = date;
        this.total = total;
        this.list = list;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public ArrayList<SpendModel> getList() {
        return list;
    }

    public void setList(ArrayList<SpendModel> list) {
        this.list = list;
    }
}

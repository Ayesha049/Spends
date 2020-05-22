package com.ayeshapp.spends;

public class SpendModel {
    String date;
    String itemName;
    Double amount;
    Double Price;

    public SpendModel() {
    }

    public SpendModel(String date, String itemName, Double amount, Double price) {
        this.date = date;
        this.itemName = itemName;
        this.amount = amount;
        Price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }
}

package com.ayeshapp.spends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SpendAdapter extends RecyclerView.Adapter<SpendAdapter.viewHolder> {
    ArrayList<SpendModel> list;

    public SpendAdapter(ArrayList<SpendModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.layout_row_spend_item, viewGroup, false);
        return new viewHolder(contactView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        SpendModel model = list.get(i);

        viewHolder.itemName.setText(model.getItemName());
        viewHolder.quantity.setText(model.getAmount().toString() + " Kg");
        viewHolder.price.setText(model.getPrice().toString() + " TK");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView itemName, quantity, price;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.item_quantity);
            price = itemView.findViewById(R.id.item_price);
        }
    }
}

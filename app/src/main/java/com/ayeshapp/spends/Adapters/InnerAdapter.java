package com.ayeshapp.spends.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayeshapp.spends.Interfaces.OnSpendItemClick;
import com.ayeshapp.spends.Models.SpendModel;
import com.ayeshapp.spends.R;

import java.util.ArrayList;

public class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.viewHolder> {
    ArrayList<SpendModel> list;

    public InnerAdapter(ArrayList<SpendModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public InnerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.layout_row_spend_item, viewGroup, false);
        return new InnerAdapter.viewHolder(contactView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull InnerAdapter.viewHolder viewHolder, int i) {
        SpendModel model = list.get(i);

        viewHolder.itemName.setText(model.getItemName());
        viewHolder.quantity.setText(model.getAmount().toString());
        viewHolder.price.setText(model.getPrice().toString());
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

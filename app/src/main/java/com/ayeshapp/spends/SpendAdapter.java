package com.ayeshapp.spends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SpendAdapter extends RecyclerView.Adapter<SpendAdapter.viewHolder> {
    ArrayList<SpendModel> list;
    OnSpendItemClick onSpendItemClick;

    public SpendAdapter(ArrayList<SpendModel> list, OnSpendItemClick onSpendItemClick) {
        this.list = list;
        this.onSpendItemClick = onSpendItemClick;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.layout_row_spend_item, viewGroup, false);
        return new viewHolder(contactView, onSpendItemClick);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
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
        ImageView edit, delete;

        OnSpendItemClick onSpendItemClick;

        public viewHolder(@NonNull View itemView, final OnSpendItemClick onSpendItemClick) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.item_quantity);
            price = itemView.findViewById(R.id.item_price);
            edit = itemView.findViewById(R.id.edit_icon);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSpendItemClick.onEditClicked(getAdapterPosition());
                }
            });
            delete = itemView.findViewById(R.id.delete_icon);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSpendItemClick.onDeleteClicked(getAdapterPosition());
                }
            });
        }
    }
}

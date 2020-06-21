package com.ayeshapp.spends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OuterAdapter extends RecyclerView.Adapter<OuterAdapter.viewHolder> {
    ArrayList<OuterModel> list;
    //OnSpendItemClick onSpendItemClick;

    public OuterAdapter(ArrayList<OuterModel> list) {//, OnSpendItemClick onSpendItemClick) {
        this.list = list;
        //this.onSpendItemClick = onSpendItemClick;
    }

    @NonNull
    @Override
    public OuterAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.layout_row_outer, viewGroup, false);
        //return new SpendAdapter.viewHolder(contactView, onSpendItemClick);
        return new OuterAdapter.viewHolder(contactView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OuterAdapter.viewHolder viewHolder, int i) {
        OuterModel model = list.get(i);
        viewHolder.date.setText(model.getDate());
        viewHolder.total.setText(model.getTotal().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView date, total;

        //LinearLayout invisibleLayout;

        //OnSpendItemClick onSpendItemClick;

        public viewHolder(@NonNull View itemView) {//, final OnSpendItemClick onSpendItemClick) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            total = itemView.findViewById(R.id.total);

            /*invisibleLayout = itemView.findViewById(R.id.invisible_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(invisibleLayout.getVisibility() == View.GONE) {
                        invisibleLayout.setVisibility(View.VISIBLE);
                    } else {
                        invisibleLayout.setVisibility(View.GONE);
                    }
                }
            });*/
        }
    }
}

package com.ayeshapp.spends.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayeshapp.spends.Models.OuterModel;
import com.ayeshapp.spends.R;

import java.util.ArrayList;

public class OuterAdapter extends RecyclerView.Adapter<OuterAdapter.viewHolder> {
    ArrayList<OuterModel> list;
    Context context;

    public OuterAdapter(ArrayList<OuterModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OuterAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.layout_row_outer, viewGroup, false);
        return new OuterAdapter.viewHolder(contactView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OuterAdapter.viewHolder viewHolder, int i) {
        OuterModel model = list.get(i);
        viewHolder.date.setText(model.getDate());
        viewHolder.total.setText(model.getTotal().toString());

        InnerAdapter adapter = new InnerAdapter(model.getList());
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView date, total;
        RecyclerView recyclerView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            total = itemView.findViewById(R.id.total);
            recyclerView = itemView.findViewById(R.id.inner_recyclerview);

        }
    }
}

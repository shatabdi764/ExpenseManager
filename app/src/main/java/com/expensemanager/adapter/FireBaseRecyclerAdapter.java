package com.expensemanager.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;
import com.expensemanager.model.Data;

import java.util.ArrayList;

public class FireBaseRecyclerAdapter extends RecyclerView.Adapter<FireBaseRecyclerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Data> dataArrayList;

    public FireBaseRecyclerAdapter(Context context, ArrayList<Data> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public FireBaseRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.income_recycler_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FireBaseRecyclerAdapter.MyViewHolder holder, int position) {
        String type = "Income Type : " + dataArrayList.get(position).getType();
        String note = "Income Note : " + dataArrayList.get(position).getNote();
        String date = "Income Date : " + dataArrayList.get(position).getDate();
        String amount = "Income Amount :" + dataArrayList.get(position).getAmount();
        holder.type.setText(type);
        holder.note.setText(note);
        holder.date.setText(date);
        holder.amount.setText(amount);
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type, note, amount, date;

        public MyViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type_txt_income);
            note = itemView.findViewById(R.id.note_txt_income);
            amount = itemView.findViewById(R.id.amount_txt_income);
            date = itemView.findViewById(R.id.date_txt_income);
        }
    }
}


package com.expensemanager.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;
import com.expensemanager.model.Data;

import java.util.ArrayList;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Data> dataArrayList;
    private OnItemClickListener onItemClickListener;

    public IncomeAdapter(Context context, ArrayList<Data> dataArrayList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public IncomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.income_recycler_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeAdapter.MyViewHolder holder, int position) {
        String type = "Income Type : " + dataArrayList.get(position).getType();
        String note = "Income Note : " + dataArrayList.get(position).getNote();
        String date = "Income Date : " + dataArrayList.get(position).getDate();
        String amount = "Income Amount :" + dataArrayList.get(position).getAmount();
        holder.type.setText(type);
        holder.note.setText(note);
        holder.date.setText(date);
        holder.amount.setText(amount);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Here we can handle click on individuals incomes
                if (onItemClickListener != null) {
                    onItemClickListener.onClicked();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type, note, amount, date;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type_txt_income);
            note = itemView.findViewById(R.id.note_txt_income);
            amount = itemView.findViewById(R.id.amount_txt_income);
            date = itemView.findViewById(R.id.date_txt_income);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public interface OnItemClickListener {
        void onClicked();
    }
}


package com.expensemanager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class IncomeFragment extends Fragment {
    private DatabaseReference mIncomeDatabase;
    //TextView
    private TextView incomeTotalSum;
    private FireBaseRecyclerAdapter fireBaseRecyclerAdapter;
    private ArrayList<Data> dataArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_income2, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = null;
        if (mUser != null) {
            uid = mUser.getUid();
        }
        if (uid != null) {
            mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeDatabase").child(uid);
        }
        incomeTotalSum = myView.findViewById(R.id.income_txt_result);
        RecyclerView recyclerView = myView.findViewById(R.id.recycler_id_income);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalValue = 0;
                dataArrayList.clear();
                for (DataSnapshot mySnapshot : dataSnapshot.getChildren()) {
                    Data data = mySnapshot.getValue(Data.class);
                    if (data != null) {
                        totalValue += data.getAmount();
                        String stTotalValue = String.valueOf(totalValue);
                        incomeTotalSum.setText(stTotalValue);
                        dataArrayList.add(data);
                    }
                }
                fireBaseRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        fireBaseRecyclerAdapter = new FireBaseRecyclerAdapter(getContext(), dataArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        fireBaseRecyclerAdapter.notifyDataSetChanged();
        return myView;
    }

    public static class FireBaseRecyclerAdapter extends RecyclerView.Adapter<FireBaseRecyclerAdapter.MyViewHolder> {
        private Context context;
        private ArrayList<Data> dataArrayList;

        FireBaseRecyclerAdapter(Context context, ArrayList<Data> dataArrayList) {
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
            holder.type.setText(dataArrayList.get(position).getType());
            holder.note.setText(dataArrayList.get(position).getNote());
            holder.date.setText(dataArrayList.get(position).getDate());
            holder.amount.setText(dataArrayList.get(position).getAmount());
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
}

        

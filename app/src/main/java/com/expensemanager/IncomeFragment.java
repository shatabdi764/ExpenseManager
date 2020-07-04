package com.expensemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;



import androidx.annotation.NonNull;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.widget.TextView;


import com.expensemanager.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



   public class IncomeFragment extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private RecyclerView recyclerView;
    //TextView
    private TextView incomeTotalSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_income2, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeDatabase").child(uid);
        incomeTotalSum=myView.findViewById(R.id.income_txt_result);
        recyclerView = myView.findViewById(R.id.recycler_id_income);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totaltvalue=0;
                for(DataSnapshot mysanapshot:dataSnapshot.getChildren()){

                    Data data=mysanapshot.getValue(Data.class);
                    totaltvalue+=data.getAmount();
                    String stTotalvalue=String.valueOf(totaltvalue);
                    incomeTotalSum.setText(stTotalvalue);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(){

            {
            Data.class,
                    R.layout.income_recycler_data,
                    MyViewHolder.class,
                    mIncomeDatabase
        } {
            protected void populateViewHolder (MyViewHolder viewHolder,  Data model,int position)
            populateViewHolder.setType(model.getType());
            populateViewHolder.setType(model.getNote());
            populateViewHolder.setType(model.getDate());
            populateViewHolder.setType(model.getAmount());


        }
    };


}
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            mView=itemView;
        }
        private void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        private void setNote(String note)
        {
            TextView mNote=mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        private void setDate(String date)
        {
            TextView mDate=mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }
        private void setAmount(int amount)
        {
            TextView mAmount =mView.findViewById(R.id.amount_txt_income);
            String stamount=String.valueOf(amount);
            mAmount.setText(stamount);
        }
    }


}

        

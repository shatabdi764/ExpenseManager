package com.expensemanager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.adapter.IncomeAdapter;
import com.expensemanager.model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class IncomeFragment extends Fragment implements IncomeAdapter.OnItemClickListener {
    private DatabaseReference mIncomeDatabase;
    //TextView
    private TextView incomeTotalSum;
    private IncomeAdapter incomeAdapter;
    private ArrayList<Data> dataArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        recyclerView = myView.findViewById(R.id.recycler_id_income);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot mySnapshot : dataSnapshot.getChildren()) {
                    Data data = mySnapshot.getValue(Data.class);
                    if (data != null) {
                        dataArrayList.add(data);
                    } else {
                        Toast.makeText(getContext(), "null data", Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
                incomeAdapter = new IncomeAdapter(getContext(), dataArrayList, IncomeFragment.this);
                recyclerView.setAdapter(incomeAdapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                incomeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return myView;
    }

    @Override
    public void onClicked(int pos) {
        //AlertBox
        
    }
}

        

package com.expensemanager;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.expensemanager.adapter.FireBaseRecyclerAdapter;
import com.expensemanager.model.Data;
import com.google.auto.value.AutoAnnotation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class IncomeFragment extends Fragment {
    private DatabaseReference mIncomeDatabase;
    //TextView
    private TextView incomeTotalSum;
    private FireBaseRecyclerAdapter fireBaseRecyclerAdapter;
    private ArrayList<Data> dataArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

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
//        incomeTotalSum = myView.findViewById(R.id.income_txt_result);
        recyclerView = myView.findViewById(R.id.recycler_id_income);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalValue = 0.0;
                String stTotalValue = null;
//                dataArrayList.clear();
                for (DataSnapshot mySnapshot : dataSnapshot.getChildren()) {
                    Data data = mySnapshot.getValue(Data.class);
                    if (data != null) {
                        totalValue += data.getAmount();
                        stTotalValue = String.valueOf(totalValue);
                        dataArrayList.add(data);
                    } else {
                        Toast.makeText(getContext(), "null data", Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
                Objects.requireNonNull(getContext()).getSharedPreferences("PREF",Context.MODE_PRIVATE).edit().putString("Sum_Income", String.valueOf(stTotalValue)).apply();
                fireBaseRecyclerAdapter = new FireBaseRecyclerAdapter(getContext(), dataArrayList);
                recyclerView.setAdapter(fireBaseRecyclerAdapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                fireBaseRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return myView;
    }
}

        

package com.expensemanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.adapter.IncomeAdapter;
import com.expensemanager.model.Data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
        incomeAdapter = new IncomeAdapter(getContext(), dataArrayList, IncomeFragment.this);
        recyclerView = myView.findViewById(R.id.recycler_id_income);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        fetchData();
        return myView;
    }

    @Override
    public void onClicked(int pos, final String amount, final String type, final String note, final String date, final String id) {
        //AlertBox
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();


        final EditText edtAmount = myView.findViewById(R.id.amount_edt);
        final EditText ediType = myView.findViewById(R.id.type_edt);
        final EditText edtNote = myView.findViewById(R.id.note_edt);

        Button btnSave = myView.findViewById(R.id.btnSave);
        Button btnCancel = myView.findViewById(R.id.btnCancel);

        //Put the existing data on views
        edtAmount.setText(amount);
        ediType.setText(type);
        edtNote.setText(note);

        btnSave.setText("Update");
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String type_ed = ediType.getText().toString().trim();
                String amount_ed = edtAmount.getText().toString().trim();
                String note_ed = edtNote.getText().toString().trim();
                if (!type_ed.isEmpty() && !amount_ed.isEmpty() && !note_ed.isEmpty()) {
                    if (!type_ed.equals(type) || !amount_ed.equals(amount) || !note_ed.equals(note)) {
                        Data data = new Data(Integer.parseInt(amount_ed), type_ed, note_ed, id, date);
                        if (id != null) {
                            mIncomeDatabase.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Data updated Successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    progressDialog.show();
                                    fetchData();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "No data changed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void fetchData () {
        dataArrayList.clear();
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
    }
}


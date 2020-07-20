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
                for (DataSnapshot mySnapshot : dataSnapshot.getChildren()) {
                    Data data = mySnapshot.getValue(Data.class);
                    if (data != null) {
                        dataArrayList.add(data);
                    } else {
                        Toast.makeText(getContext(), "null data", Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
                fireBaseRecyclerAdapter = new FireBaseRecyclerAdapter(getContext(), dataArrayList);
                recyclerView.setAdapter(fireBaseRecyclerAdapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                fireBaseRecyclerAdapter.notifyDataSetChanged();
            }
              recyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    post_key=getRef(position).getKey();

                    type=model.getType();
                    note=model.getNote();
                    amount=model.getAmount();


                    updateDataItem();
                      }
            });

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return myView;
    }
}

        private void updateDataItem()
    {
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myview);

        edtAmount=myview.findViewById(R.id.amount_edt);
        edtType=myview.findViewById(R.id.type_edt);
        edtNote=myview.findViewById(R.id.note_edt);
///Set data to edit text
        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setText(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection((String.valueOf(amount).length()));

        btnUpdate=myview.findViewById(R.id.btnUpdate);
        btnDelete=myview.findViewById(R.id.btn_Delete);
        final AlertDialog dialog=mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            type=edtType.getText().toString().trim();
            note=edtNote.getText().toString().trim();
            String mamount=String.valueOf(amount);
            mamount=edtAmount.getText().toString().trim();

            int myamount=Integer.parseInt(mamount);
            String mDate= DateFormat.getDateInstance().format(new Date());
            Data data=new Data(myamount,type,note,post_key,mDate);

            mIncomeDatabase.child(post_key).setValue(data);
            dialog.dismiss();

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            mIncomeDatabase.child(post_key).removeValue();
                dialog.dismiss();
        }
        });
        dialog.show();
    }
}
        


package com.expensemanager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.expensemanager.model.Data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    //Floating Button
    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    // Floating button  textview...

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    //boolean
    private boolean isOpen = false;

    //Animation
    private Animation FadOpen, FadClose;
///Dashboard Income and expense
    private TextView totalIncomeresult;
    private TextView totalExpenseResult;
    ///Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;
    private TextView sumIncome;
    private ProgressBar progressBar;
    //run
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.fragment_dashboard, container, false);

        progressBar = myview.findViewById(R.id.progress_bar);
        sumIncome = myview.findViewById(R.id.income_set_result);
        sumIncome.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = null;
        if (mUser != null) {
            uid = mUser.getUid();
        }
        if (uid != null) {
            mIncomeDatabase = FirebaseDatabase.getInstance().getReference("IncomeDatabase").child(uid);
        }
        if (uid != null) {
            mExpenseDatabase = FirebaseDatabase.getInstance().getReference("ExpenseDatabase").child(uid);
        }
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalValue = 0.0;
                String stTotalValue = null;
                for (DataSnapshot mySnapshot : dataSnapshot.getChildren()) {
                    Data data = mySnapshot.getValue(Data.class);
                    if (data != null) {
                        totalValue += data.getAmount();
                        stTotalValue = String.valueOf(totalValue);
                    } else {
                        Toast.makeText(getContext(), "null data", Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
                sumIncome.setVisibility(View.VISIBLE);
                sumIncome.setText(String.valueOf(stTotalValue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//
        //Connect floating button to layout
        fab_main_btn = myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myview.findViewById(R.id.income_ft_btn);
        fab_expense_btn = myview.findViewById(R.id.expense_ft_btn);


        //connect floating text
        fab_income_txt = myview.findViewById(R.id.income_ft_text);
        fab_expense_txt = myview.findViewById(R.id.expense_ft_text);

        //Total income and expense result.....
        totalIncomeresult =myview.findViewById(R.id.income_set_result);
        totalExpenseResult=myview.findViewById(R.id.expense_set_result);

//Animation
        FadOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        FadClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
                if (isOpen) {
                    fab_income_btn.startAnimation(FadClose);
                    fab_expense_btn.startAnimation(FadClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);
                    fab_income_txt.startAnimation(FadClose);
                    fab_expense_txt.startAnimation(FadClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isOpen = false;

                } else {
                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);
                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);

                    isOpen = true;
                }
            }
        });

        //Calculate total income
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum=0;
                for(DataSnapshot mysnap:snapshot.getChildren()){
                    Data data=mysnap.getValue(Data.class);
                    totalsum+=+data.getAmount();
                    String stResult=String.valueOf(totalsum);
                    totalIncomeresult.setText(stResult);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); 
        //Calculate total expense
        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum=0;
                for(DataSnapshot mysnap:snapshot.getChildren()){
                    Data data=mysnap.getValue(Data.class);
                    totalsum+=data.getAmount();
                    String stResult=String.valueOf(totalsum);
                    totalExpenseResult.setText(stResult);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return myview;
        //

    }

    private void addData() {
        //FAB BUTTON
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                incomeDataInsert();
            }
        });
        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseDataInsert();
            }
        });

    }

    public void incomeDataInsert() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();


        final EditText edtAmount = myview.findViewById(R.id.amount_edt);
        final EditText ediType = myview.findViewById(R.id.type_edt);
        final EditText edtNote = myview.findViewById(R.id.note_edt);

        Button btnSave = myview.findViewById(R.id.btnSave);
        Button btnCancel = myview.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String type = ediType.getText().toString().trim();
                String amount = edtAmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();
                if (!type.isEmpty() && !amount.isEmpty() && !note.isEmpty()) {
                    String id = mIncomeDatabase.push().getKey();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyy", Locale.US);
                    String date = formatter.format(new Date());
                    Data data = new Data(Integer.parseInt(amount), type, note, id, date);
                    if (id != null) {
                        mIncomeDatabase.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Data Stored Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
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

public void expenseDataInsert()
{
    AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = LayoutInflater.from(getActivity());

    View myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
    mydialog.setView(myview);
    final AlertDialog dialog = mydialog.create();
    final EditText edtAmount = myview.findViewById(R.id.amount_edt);
    final EditText ediType = myview.findViewById(R.id.type_edt);
    final EditText edtNote = myview.findViewById(R.id.note_edt);

    Button btnSave = myview.findViewById(R.id.btnSave);
    Button btnCancel = myview.findViewById(R.id.btnCancel);

    btnSave.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            String type = ediType.getText().toString().trim();
            String amount = edtAmount.getText().toString().trim();
            String note = edtNote.getText().toString().trim();
            if (!type.isEmpty() && !amount.isEmpty() && !note.isEmpty()) {
                String id = mExpenseDatabase.push().getKey();
                SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyy", Locale.US);
                String date = formatter.format(new Date());
                Data data = new Data(Integer.parseInt(amount), type, note, id, date);
                if (id != null) {
                    mExpenseDatabase.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Data Stored Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
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


}

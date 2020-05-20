package com.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResetActivity extends AppCompatActivity {
    private EditText newpass;
    private EditText cnfpass;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        setPass();
    }
    public void setPass()
    {
        newpass=findViewById(R.id.new_pass);
        cnfpass=findViewById(R.id.cnf_pass);
        submit=findViewById(R.id.btn_regis);
        submit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                String npass = newpass.getText().toString().trim();
                String cpass = cnfpass.getText().toString();
                if (TextUtils.isEmpty(npass)) {
                    newpass.setError("New Password Required...");
                    return;
                }
                if (TextUtils.isEmpty(cpass)) {
                    cnfpass.setError("Confirm Password Required");
                }
            }
    });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
}

});
    }
}

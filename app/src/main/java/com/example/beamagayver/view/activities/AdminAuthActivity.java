package com.example.beamagayver.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.beamagayver.R;
import com.example.beamagayver.pojo.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminAuthActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.admin_code_ET)
    EditText adminCodeET;
    @BindView(R.id.email_ET)
    EditText emailET;
    @BindView(R.id.submit)
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_auth);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == submit) {
            if (adminCodeET.getText().toString().equals("")) {
                adminCodeET.setError("Required");
            } else if (emailET.getText().toString().equals("")) {
                emailET.setError("Required");
            } else getAdmin();
        }
    }

    private void getAdmin() {
        String email = emailET.getText().toString();
        String code = adminCodeET.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("admin").get().addOnCompleteListener(task -> {
            if (task.getResult() != null && task.isComplete() && task.isSuccessful()) {
                List<Admin> admins = task.getResult().toObjects(Admin.class);
                for (Admin a : admins) {
                    if(a.getCode().equals(code) && a.getEmail().equals(email)){
                        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminAuthActivity.this , HomeActivity.class));
                    }
                }
            }

        });
    }
}

package com.example.beamagayver.view.fragments.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.beamagayver.R;
import com.example.beamagayver.pojo.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAdminDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "AddAdminDialog";


    @BindView(R.id.email_ET)
    EditText emailET;
    @BindView(R.id.admin_code_ET)
    EditText adminCodeET;
    @BindView(R.id.submit)
    Button submit;
    private String email;
    private String code;

    public AddAdminDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_admin_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        inti();
    }

    private void inti() {
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == submit) {
            email = emailET.getText().toString();
            code = adminCodeET.getText().toString();
            if(email.equals("")){
                emailET.setError("Required");
            }else if(code.equals("")){
                adminCodeET.setError("Required");
            }else addAdmin();
        }
    }

    private void addAdmin() {
        Admin admin = new Admin(code , email);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("admin").add(admin).addOnCompleteListener(task -> {
            if(task.isComplete() && task.isSuccessful() && task.getResult() != null){
                Log.i(TAG, "addAdmin: "+ task.getResult());
                Toast.makeText(getContext(), "Successful added admin", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}

package com.example.beamagayver.Utilities;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.beamagayver.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Dialog extends BottomSheetDialogFragment {
    private static final String TAG = "Dialog";

    private EditText editText;
    private DialogListener listener;
    private String title;

    public Dialog(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment , container , false);
        editText = v.findViewById(R.id.car_brand_edit_text);
        Button okButton = v.findViewById(R.id.ok_button);
        Button cancelButton = v.findViewById(R.id.cancel_button);
        switch (title) {
            case "Car Details":
                editText.setHint("e.g toyota corolla 2018");
                okButton.setOnClickListener(view -> {
                    listener.applyCarDetails(editText.getText().toString());
                    dismiss();
                });
                break;
            case "Phone Number":
                editText.setHint("Enter active phone number");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                okButton.setOnClickListener(view -> {
                    listener.applyPhoneNumber(editText.getText().toString());
                    dismiss();
                });
                break;
            case "Session Duration":
                editText.setHint("Enter Duration in hours");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                okButton.setOnClickListener(view -> {
                    listener.applyDurationText(editText.getText().toString());
                    dismiss();
                });
                break;
        }
        cancelButton.setOnClickListener(view -> dismiss());
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener =(DialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "onAttach: " + e.getMessage());
        }
    }


    public interface DialogListener{
        void applyCarDetails(String brand );
        void applyPhoneNumber(String number );
        void applyDurationText(String duration);
    }
}

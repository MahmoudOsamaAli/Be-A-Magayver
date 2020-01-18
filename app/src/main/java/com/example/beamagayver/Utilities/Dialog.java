package com.example.beamagayver.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.beamagayver.R;

import java.util.Objects;

public class Dialog extends DialogFragment {
    private static final String TAG = "Dialog";

    private EditText carBrand;
    private EditText carModel;
    private EditText carYear;
    DialogListener listener;
    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_fragment , null);
        carBrand = v.findViewById(R.id.car_brand_edit_text);
        carModel = v.findViewById(R.id.car_model_edit_text);
        carYear = v.findViewById(R.id.car_model_year_edit_text);
        builder.setView(v)
                .setTitle("Car Details")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    String brand = carBrand.getText().toString();
                    String model = carModel.getText().toString();
                    String year = carYear.getText().toString();
                    listener.applyText(brand , model , year);
                });
        return builder.create();
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
        void applyText(String brand , String model , String year);
    }
}

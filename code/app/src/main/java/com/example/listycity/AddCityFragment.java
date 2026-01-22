package com.example.listycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    private static final String ARG_CITY = "city_arg";

    interface AddCityDialogListener {
        void addCity(City city);
        void onCityUpdated();
    }
    private AddCityDialogListener listener;

    public AddCityFragment() {}

    public static AddCityFragment newInstance(City city) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        }
        else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        City cityToEdit = null;
        Bundle args = getArguments();
        if (args != null) {
            cityToEdit = (City) args.getSerializable(ARG_CITY);
        }

        boolean isEditing = (cityToEdit != null);

        if (isEditing) {
            editCityName.setText(cityToEdit.getName());
            editProvinceName.setText(cityToEdit.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        City finalCityToEdit = cityToEdit;

        return builder
                .setView(view)
                .setTitle("Add a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    if (cityName.isEmpty() || provinceName.isEmpty()) return;
                    if (finalCityToEdit == null) {
                        listener.addCity(new City(cityName, provinceName));
                    }
                    else {
                        finalCityToEdit.setName(cityName);
                        finalCityToEdit.setProvince(provinceName);
                        listener.onCityUpdated();
                    }
                })
                .create();
    }
}

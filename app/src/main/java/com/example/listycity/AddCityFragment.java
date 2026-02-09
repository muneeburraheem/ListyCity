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

    interface AddCityDialogListener {
        void addCity(City city);
        void updateCity();
    }

    private AddCityDialogListener listener;
    private City city;   // null = ADD, not null = EDIT

    public static AddCityFragment newInstance(City city) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        fragment.setArguments(args);
        return fragment;
    }

    public AddCityFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_add_city, null);

        EditText editCity = view.findViewById(R.id.edit_text_city_text);
        EditText editProvince = view.findViewById(R.id.edit_text_province_text);

        if (getArguments() != null) {
            city = (City) getArguments().getSerializable("city");
            editCity.setText(city.getName());
            editProvince.setText(city.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(city == null ? "Add a city" : "Edit city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {

                    String name = editCity.getText().toString();
                    String province = editProvince.getText().toString();

                    if (city == null) {
                        listener.addCity(new City(name, province));
                    } else {
                        city.setName(name);
                        city.setProvince(province);
                        listener.updateCity();
                    }
                })
                .create();
    }
}
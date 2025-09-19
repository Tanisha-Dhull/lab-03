package com.example.listycitylab3;

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

// Dialog used for both adding and editing cities
public class AddCityFragment extends DialogFragment {

    // Keys to pass info when editing
    private static final String ARG_CITY_NAME = "arg_city_name";
    private static final String ARG_PROV_NAME = "arg_prov_name";
    private static final String ARG_POS       = "arg_pos";

    // Method to open dialog in edit mode
    public static AddCityFragment newInstance(String cityName, String provinceName, int position) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CITY_NAME, cityName);
        args.putString(ARG_PROV_NAME, provinceName);
        args.putInt(ARG_POS, position);
        fragment.setArguments(args);
        return fragment;
    }

    // Listener for sending data back
    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(int position, String newCityName, String newProvinceName);
    }

    private AddCityDialogListener listener;

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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // Check if we are editing
        boolean isEdit = false;
        String preCity = null, preProv = null;
        int editingPos = -1;

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_CITY_NAME) && args.containsKey(ARG_PROV_NAME)) {
            preCity = args.getString(ARG_CITY_NAME);
            preProv = args.getString(ARG_PROV_NAME);
            editingPos = args.getInt(ARG_POS, -1);
            if (preCity != null && preProv != null && editingPos >= 0) {
                isEdit = true;
                editCityName.setText(preCity);
                editProvinceName.setText(preProv);
            }
        }

        final boolean isEditMode = isEdit;
        final int editingPosFinal = editingPos;

        // Dialog shows Add or Edit depending on mode
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(isEditMode ? "Add/Edit City" : "Add a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(isEditMode ? "Save" : "Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString().trim();
                    String provinceName = editProvinceName.getText().toString().trim();

                    if (isEditMode) {
                        if (listener != null) {
                            listener.editCity(editingPosFinal, cityName, provinceName);
                        }
                    } else {
                        if (listener != null) {
                            listener.addCity(new City(cityName, provinceName));
                        }
                    }
                })
                .create();
    }
}

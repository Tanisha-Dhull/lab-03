package com.example.listycitylab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

// Main screen: shows the list and handles add/edit callbacks
public class MainActivity extends AppCompatActivity
        implements AddCityFragment.AddCityDialogListener {

    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Starter cities
        String[] cities = { "Edmonton", "Vancouver", "Toronto" };
        String[] provinces = { "AB", "BC", "ON" };

        dataList = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }

        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        // Add button -> Original add dialog
        FloatingActionButton addButton = findViewById(R.id.button_add_city);
        addButton.setOnClickListener(v ->
                new AddCityFragment().show(getSupportFragmentManager(), "ADD_CITY")
        );

        // Click item -> Edit dialog
        cityList.setOnItemClickListener((parent, view, position, id) -> {
            City current = dataList.get(position);
            AddCityFragment.newInstance(current.getName(), current.getProvince(), position)
                    .show(getSupportFragmentManager(), "EDIT_CITY");
        });
    }

    // Add city callback
    @Override
    public void addCity(City city) {
        cityAdapter.add(city);
        cityAdapter.notifyDataSetChanged();
    }

    // Edit city callback
    @Override
    public void editCity(int position, String newName, String newProv) {
        if (position >= 0 && position < dataList.size()) {
            City c = dataList.get(position);
            c.setName(newName);
            c.setProvince(newProv);
            cityAdapter.notifyDataSetChanged();
        }
    }
}

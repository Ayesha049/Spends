package com.ayeshapp.spends;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddIncomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        Spinner catagotyIncomeSpinner = (Spinner) findViewById(R.id.catagory_income_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddIncomeActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.CatagoryIncome) );
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        catagotyIncomeSpinner.setAdapter(adapter);
    }
}
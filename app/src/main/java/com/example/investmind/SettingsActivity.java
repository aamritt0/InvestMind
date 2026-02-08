package com.example.investmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    private SettingsManager settingsManager;
    private Spinner spinnerCurrency;
    private Spinner spinnerDecimalSep;
    private Spinner spinnerThousandsSep;
    private MaterialSwitch switchTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        settingsManager = new SettingsManager(this);

        // Setup toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Initialize views
        spinnerCurrency = findViewById(R.id.spinnerCurrency);
        spinnerDecimalSep = findViewById(R.id.spinnerDecimalSep);
        spinnerThousandsSep = findViewById(R.id.spinnerThousandsSep);
        switchTextInput = findViewById(R.id.switchTextInput);

        setupCurrencySpinner();
        setupDecimalSeparatorSpinner();
        setupThousandsSeparatorSpinner();
        setupTextInputSwitch();
        
        // About button
        findViewById(R.id.btnAbout).setOnClickListener(v -> {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void setupCurrencySpinner() {
        String[] currencies = {"EUR (€)", "USD ($)", "GBP (£)", "JPY (¥)", "CHF (Fr)", "CAD ($)", "AUD ($)", "CNY (¥)", "INR (₹)", "BRL (R$)"};
        String[] currencyCodes = {"EUR", "USD", "GBP", "JPY", "CHF", "CAD", "AUD", "CNY", "INR", "BRL"};
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);

        // Set current selection
        String currentCurrency = settingsManager.getCurrencyCode();
        for (int i = 0; i < currencyCodes.length; i++) {
            if (currencyCodes[i].equals(currentCurrency)) {
                spinnerCurrency.setSelection(i);
                break;
            }
        }

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingsManager.setCurrencyCode(currencyCodes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupDecimalSeparatorSpinner() {
        String[] separators = {"Virgola (,)", "Punto (.)"};
        String[] separatorValues = {",", "."};
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, separators);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDecimalSep.setAdapter(adapter);

        // Set current selection
        String currentSep = settingsManager.getDecimalSeparator();
        for (int i = 0; i < separatorValues.length; i++) {
            if (separatorValues[i].equals(currentSep)) {
                spinnerDecimalSep.setSelection(i);
                break;
            }
        }

        spinnerDecimalSep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingsManager.setDecimalSeparator(separatorValues[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupThousandsSeparatorSpinner() {
        String[] separators = {"Punto (.)", "Virgola (,)", "Spazio ( )"};
        String[] separatorValues = {".", ",", " "};
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, separators);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThousandsSep.setAdapter(adapter);

        // Set current selection
        String currentSep = settingsManager.getThousandsSeparator();
        for (int i = 0; i < separatorValues.length; i++) {
            if (separatorValues[i].equals(currentSep)) {
                spinnerThousandsSep.setSelection(i);
                break;
            }
        }

        spinnerThousandsSep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingsManager.setThousandsSeparator(separatorValues[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupTextInputSwitch() {
        switchTextInput.setChecked(settingsManager.isTextInputEnabled());
        switchTextInput.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setTextInputEnabled(isChecked);
        });
    }
}

package com.example.investmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
        setContentView(R.layout.activity_settings);

        settingsManager = new SettingsManager(this);

        // Setup Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.getMenu().findItem(R.id.nav_settings).setChecked(true);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_history) {
                Intent intent = new Intent(this, HistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                return true;
            }
            return false;
        });

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
    
    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.getMenu().findItem(R.id.nav_settings).setChecked(true);
    }


    private void setupCurrencySpinner() {
        String[] currencies = {"EUR (€)", "USD ($)", "GBP (£)", "JPY (¥)", "CHF (Fr)", "CAD ($)", "AUD ($)", "CNY (¥)", "INR (₹)", "BRL (R$)"};
        String[] currencyCodes = {"EUR", "USD", "GBP", "JPY", "CHF", "CAD", "AUD", "CNY", "INR", "BRL"};
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, currencies);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, separators);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, separators);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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

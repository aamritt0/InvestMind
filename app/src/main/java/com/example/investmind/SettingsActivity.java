package com.example.investmind;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    private SettingsManager settingsManager;
    private AutoCompleteTextView dropdownCurrency;
    private AutoCompleteTextView dropdownDecimal;
    private AutoCompleteTextView dropdownThousands;
    private MaterialSwitch switchTextInput;
    
    private boolean isInitialized = false; // Cache flag to prevent re-setup

    private final String[] currencies = {"EUR (€)", "USD ($)", "GBP (£)", "JPY (¥)", "CHF (Fr)", "CAD ($)", "AUD ($)", "CNY (¥)", "INR (₹)", "BRL (R$)"};
    private final String[] currencyCodes = {"EUR", "USD", "GBP", "JPY", "CHF", "CAD", "AUD", "CNY", "INR", "BRL"};
    
    private final String[] decimalSeparators = {"Virgola (,)", "Punto (.)"};
    private final String[] decimalValues = {",", "."};
    
    private final String[] thousandsSeparators = {"Punto (.)", "Virgola (,)", "Spazio ( )"};
    private final String[] thousandsValues = {".", ",", " "};

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
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else if (item.getItemId() == R.id.nav_history) {
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                return true;
            }
            return false;
        });

        // Initialize views
        dropdownCurrency = findViewById(R.id.dropdownCurrency);
        dropdownDecimal = findViewById(R.id.dropdownDecimal);
        dropdownThousands = findViewById(R.id.dropdownThousands);
        switchTextInput = findViewById(R.id.switchTextInput);

        // Only setup if not already initialized (expensive operations)
        if (!isInitialized) {
            setupCurrencyDropdown();
            setupDecimalDropdown();
            setupThousandsDropdown();
            setupTextInputSwitch();
            isInitialized = true;
        }
        
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


    private void setupCurrencyDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, currencies);
        dropdownCurrency.setAdapter(adapter);

        // Set current selection
        String currentCurrency = settingsManager.getCurrencyCode();
        for (int i = 0; i < currencyCodes.length; i++) {
            if (currencyCodes[i].equals(currentCurrency)) {
                dropdownCurrency.setText(currencies[i], false);
                break;
            }
        }

        dropdownCurrency.setOnItemClickListener((parent, view, position, id) -> {
            settingsManager.setCurrencyCode(currencyCodes[position]);
        });
    }

    private void setupDecimalDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, decimalSeparators);
        dropdownDecimal.setAdapter(adapter);

        // Set current selection
        String currentSep = settingsManager.getDecimalSeparator();
        for (int i = 0; i < decimalValues.length; i++) {
            if (decimalValues[i].equals(currentSep)) {
                dropdownDecimal.setText(decimalSeparators[i], false);
                break;
            }
        }

        dropdownDecimal.setOnItemClickListener((parent, view, position, id) -> {
            settingsManager.setDecimalSeparator(decimalValues[position]);
        });
    }

    private void setupThousandsDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, thousandsSeparators);
        dropdownThousands.setAdapter(adapter);

        // Set current selection
        String currentSep = settingsManager.getThousandsSeparator();
        for (int i = 0; i < thousandsValues.length; i++) {
            if (thousandsValues[i].equals(currentSep)) {
                dropdownThousands.setText(thousandsSeparators[i], false);
                break;
            }
        }

        dropdownThousands.setOnItemClickListener((parent, view, position, id) -> {
            settingsManager.setThousandsSeparator(thousandsValues[position]);
        });
    }

    private void setupTextInputSwitch() {
        switchTextInput.setChecked(settingsManager.isTextInputEnabled());
        switchTextInput.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setTextInputEnabled(isChecked);
        });
    }
}

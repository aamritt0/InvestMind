package com.example.investmind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.util.Locale;

public class SimpleCalcActivity extends AppCompatActivity {

    private TextView tvPrincipalValue;
    private TextView tvRateValue;
    private TextView tvYearsValue;
    private SeekBar sliderPrincipal;
    private SeekBar sliderRate;
    private SeekBar sliderYears;
    private ExtendedFloatingActionButton btnCalculate;

    private TextInputLayout textInputLayoutPrincipal;
    private TextInputLayout textInputLayoutRate;
    private TextInputLayout textInputLayoutYears;
    private TextInputEditText etPrincipal;
    private TextInputEditText etRate;
    private TextInputEditText etYears;

    private SettingsManager settingsManager;
    private boolean isUpdatingFromSlider = false;

    private double principal = 10000.0;
    private double rate = 5.0;
    private int years = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_simple_calc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        settingsManager = new SettingsManager(this);

        initViews();
        setupTextInputVisibility();
        setupListeners();
        updateValues();
    }

    private void initViews() {
        tvPrincipalValue = findViewById(R.id.tvPrincipalValue);
        tvRateValue = findViewById(R.id.tvRateValue);
        tvYearsValue = findViewById(R.id.tvYearsValue);
        sliderPrincipal = findViewById(R.id.sliderPrincipal);
        sliderRate = findViewById(R.id.sliderRate);
        sliderYears = findViewById(R.id.sliderYears);
        btnCalculate = findViewById(R.id.btnCalculate);

        textInputLayoutPrincipal = findViewById(R.id.textInputLayoutPrincipal);
        textInputLayoutRate = findViewById(R.id.textInputLayoutRate);
        textInputLayoutYears = findViewById(R.id.textInputLayoutYears);
        etPrincipal = findViewById(R.id.etPrincipal);
        etRate = findViewById(R.id.etRate);
        etYears = findViewById(R.id.etYears);
    }

    private void setupTextInputVisibility() {
        boolean isTextInputEnabled = settingsManager.isTextInputEnabled();
        int visibility = isTextInputEnabled ? View.VISIBLE : View.GONE;
        textInputLayoutPrincipal.setVisibility(visibility);
        textInputLayoutRate.setVisibility(visibility);
        textInputLayoutYears.setVisibility(visibility);
    }

    private void setupListeners() {
        sliderPrincipal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Only update principal if the change came from user interaction
                // This prevents overwriting text input values > 100k
                if (fromUser) {
                    // 0 to 100000, step 500. progress 0-200
                    principal = progress * 500.0;
                    updateValues();
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sliderRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 0.1 to 15.0, step 0.1. progress 0-149
                rate = 0.1 + (progress * 0.1);
                updateValues();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sliderYears.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 1 to 30, step 1. progress 0-29
                years = 1 + progress;
                updateValues();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Handle back button on toolbar
        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        btnCalculate.setOnClickListener(v -> calculateAndShowResult());

        // Text input listeners
        etPrincipal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdatingFromSlider && s.length() > 0) {
                    try {
                        double value = settingsManager.parseNumber(s.toString());
                        if (value >= 0 && value <= 500000) {
                            principal = value;
                            // Only update slider if value is within slider range (0-100k)
                            if (value <= 100000) {
                                int progress = (int) (value / 500.0);
                                sliderPrincipal.setProgress(progress);
                            } else {
                                // Set slider to maximum when value exceeds slider range
                                sliderPrincipal.setProgress(200);
                            }
                            tvPrincipalValue.setText(settingsManager.formatCurrencyNoDecimals(principal));
                            textInputLayoutPrincipal.setError(null);
                        } else {
                            textInputLayoutPrincipal.setError("Valore tra 0 e " + settingsManager.formatNumber(500000, 0));
                        }
                    } catch (NumberFormatException e) {
                        textInputLayoutPrincipal.setError("Valore non valido");
                    }
                }
            }
        });

        etRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdatingFromSlider && s.length() > 0) {
                    try {
                        double value = settingsManager.parseNumber(s.toString());
                        if (value >= 0.1 && value <= 15.0) {
                            rate = value;
                            int progress = (int) ((value - 0.1) / 0.1);
                            sliderRate.setProgress(progress);
                            tvRateValue.setText(settingsManager.formatPercentage(rate));
                            textInputLayoutRate.setError(null);
                        } else {
                            textInputLayoutRate.setError("Valore tra " + settingsManager.formatPercentage(0.1) + " e " + settingsManager.formatPercentage(15.0));
                        }
                    } catch (NumberFormatException e) {
                        textInputLayoutRate.setError("Valore non valido");
                    }
                }
            }
        });

        etYears.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdatingFromSlider && s.length() > 0) {
                    try {
                        int value = Integer.parseInt(s.toString());
                        if (value >= 1 && value <= 30) {
                            years = value;
                            sliderYears.setProgress(value - 1);
                            tvYearsValue.setText(String.format(Locale.getDefault(), "%d Anni", years));
                            if (years == 1) {
                                tvYearsValue.setText("1 Anno");
                            }
                            textInputLayoutYears.setError(null);
                        } else {
                            textInputLayoutYears.setError("Valore tra 1 e 30");
                        }
                    } catch (NumberFormatException e) {
                        textInputLayoutYears.setError("Valore non valido");
                    }
                }
            }
        });
    }

    private void updateValues() {
        isUpdatingFromSlider = true;
        
        tvPrincipalValue.setText(settingsManager.formatCurrencyNoDecimals(principal));
        tvRateValue.setText(settingsManager.formatPercentage(rate));
        
        tvYearsValue.setText(String.format(Locale.getDefault(), "%d Anni", years));
        if (years == 1) {
             tvYearsValue.setText("1 Anno");
        }

        // Only update text inputs when they're not focused (to avoid cursor jumping)
        // When user is typing, the TextWatcher handles validation
        if (settingsManager.isTextInputEnabled()) {
            if (!etPrincipal.hasFocus()) {
                etPrincipal.setText(settingsManager.getNumberFormat(0).format(principal));
            }
            if (!etRate.hasFocus()) {
                etRate.setText(settingsManager.formatNumber(rate, 1));
            }
            if (!etYears.hasFocus()) {
                etYears.setText(String.valueOf(years));
            }
        }
        
        isUpdatingFromSlider = false;
    }

    private void calculateAndShowResult() {
        // Show Material 3 Expressive dialog to optionally name the calculation
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_calculation_name, null);
        com.google.android.material.textfield.TextInputEditText input = dialogView.findViewById(R.id.etCalculationName);
        
        com.google.android.material.dialog.MaterialAlertDialogBuilder builder = 
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(this, com.google.android.material.R.style.ThemeOverlay_Material3_Dark);
        builder.setView(dialogView);
        
        builder.setPositiveButton("Calcola", (dialog, which) -> {
            String calculationName = input.getText().toString().trim();
            performCalculation(calculationName);
        });
        
        builder.setNegativeButton("Annulla", (dialog, which) -> dialog.cancel());
        
        builder.show();
    }
    
    private void performCalculation(String calculationName) {
        // Simple Interest Formula: A = P(1 + rt)
        double r = rate / 100.0;
        double amount = principal * (1 + (r * years));

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("CALC_TYPE", "Interesse Semplice");
        intent.putExtra("RESULT_AMOUNT", amount);
        intent.putExtra("PRINCIPAL", principal);
        intent.putExtra("RATE", rate);
        intent.putExtra("YEARS", years);
        if (calculationName != null && !calculationName.isEmpty()) {
            intent.putExtra("CALCULATION_NAME", calculationName);
        }
        startActivity(intent);
    }
}
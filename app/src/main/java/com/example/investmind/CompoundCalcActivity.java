package com.example.investmind;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.NumberFormat;
import java.util.Locale;

public class CompoundCalcActivity extends AppCompatActivity {

    private TextView tvPrincipalValue;
    private TextView tvRateValue;
    private TextView tvYearsValue;
    private SeekBar sliderPrincipal;
    private SeekBar sliderRate;
    private SeekBar sliderYears;
    private MaterialButtonToggleGroup toggleGroupFreq;
    private ExtendedFloatingActionButton btnCalculate;

    private double principal = 10000.0;
    private double rate = 5.0;
    private int years = 10;
    private int frequency = 1; // 1 = Annual, 4 = Quarterly, 12 = Monthly

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_compound_calc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
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
        toggleGroupFreq = findViewById(R.id.toggleGroupFreq);
        btnCalculate = findViewById(R.id.btnCalculate);
    }

    private void setupListeners() {
        sliderPrincipal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 0 to 50000, step 500. progress 0-100
                principal = progress * 500.0;
                updateValues();
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

        toggleGroupFreq.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnAnnual) {
                    frequency = 1;
                } else if (checkedId == R.id.btnQuarterly) {
                    frequency = 4;
                } else if (checkedId == R.id.btnMonthly) {
                    frequency = 12;
                }
            }
        });
        
        // Handle back button on toolbar
        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        btnCalculate.setOnClickListener(v -> calculateAndShowResult());
    }

    private void updateValues() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        currencyFormat.setMaximumFractionDigits(0);
        tvPrincipalValue.setText(currencyFormat.format(principal));

        tvRateValue.setText(String.format(Locale.getDefault(), "%.1f%%", rate));
        
        tvYearsValue.setText(String.format(Locale.getDefault(), "%d Anni", years));
        if (years == 1) {
             tvYearsValue.setText("1 Anno");
        }
    }

    private void calculateAndShowResult() {
        // A = P(1 + r/n)^(nt)
        double r = rate / 100.0;
        double amount = principal * Math.pow(1 + (r / frequency), frequency * years);

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("RESULT_AMOUNT", amount);
        intent.putExtra("PRINCIPAL", principal);
        intent.putExtra("RATE", rate);
        intent.putExtra("YEARS", years);
        startActivity(intent);
    }
}
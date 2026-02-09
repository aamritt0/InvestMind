package com.example.investmind;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SettingsManager settings = new SettingsManager(this);
        
        double amount = getIntent().getDoubleExtra("RESULT_AMOUNT", 0.0);
        TextView tvResultAmount = findViewById(R.id.tvResultAmount);
        
        tvResultAmount.setText(settings.formatCurrency(amount));

        // Save History
        String type = getIntent().getStringExtra("CALC_TYPE");
        if (type == null) type = "Calcolo";

        double principal = getIntent().getDoubleExtra("PRINCIPAL", 0);
        double rate = getIntent().getDoubleExtra("RATE", 0);
        int years = getIntent().getIntExtra("YEARS", 0);
        String calculationName = getIntent().getStringExtra("CALCULATION_NAME");

        String details = settings.formatCurrencyNoDecimals(principal) + " + " + 
                         settings.formatPercentage(rate) + " per " + years + " anni";

        HistoryItem item = new HistoryItem(
            type,
            type,
            System.currentTimeMillis(),
            settings.formatCurrency(amount),
            details,
            calculationName,
            principal
        );
        HistoryManager.saveHistoryItem(this, item);
    }
}
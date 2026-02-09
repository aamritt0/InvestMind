package com.example.investmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout indicatorContainer;
    private MaterialButton btnNext;
    private MaterialButton btnSkip;
    private OnboardingAdapter adapter;
    private View[] indicators;

    private final int[] icons = {
            R.drawable.ic_wallet,
            R.drawable.ic_trending,
            R.drawable.ic_history
    };

    private final String[] titles = {
            "Benvenuto in\nInvestMind",
            "Calcola i tuoi\nInvestimenti",
            "Salva i tuoi\nCalcoli"
    };

    private final String[] descriptions = {
            "L'app che ti aiuta a calcolare\nfacilmente i tuoi investimenti",
            "Scopri quanto crescerà il tuo capitale\ncon l'interesse semplice e composto",
            "Ritrova facilmente i risultati\nnella cronologia"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        indicatorContainer = findViewById(R.id.indicatorContainer);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);

        adapter = new OnboardingAdapter(icons, titles, descriptions);
        viewPager.setAdapter(adapter);

        setupIndicators();
        updateIndicators(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicators(position);
                updateButtons(position);
            }
        });

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(current + 1);
            } else {
                finishOnboarding();
            }
        });

        btnSkip.setOnClickListener(v -> finishOnboarding());
    }

    private void setupIndicators() {
        indicators = new View[adapter.getItemCount()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                24, 24
        );
        params.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new View(this);
            indicators[i].setLayoutParams(params);
            indicators[i].setBackgroundResource(R.drawable.bg_circle);
            indicatorContainer.addView(indicators[i]);
        }
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            if (i == position) {
                indicators[i].setBackgroundTintList(
                        getColorStateList(R.color.primary)
                );
                indicators[i].setScaleX(1.2f);
                indicators[i].setScaleY(1.2f);
            } else {
                indicators[i].setBackgroundTintList(
                        getColorStateList(R.color.surface_variant)
                );
                indicators[i].setScaleX(1.0f);
                indicators[i].setScaleY(1.0f);
            }
        }
    }

    private void updateButtons(int position) {
        if (position == adapter.getItemCount() - 1) {
            btnNext.setText("Inizia");
            btnSkip.setVisibility(View.INVISIBLE);
        } else {
            btnNext.setText("Avanti");
            btnSkip.setVisibility(View.VISIBLE);
        }
    }

    private void finishOnboarding() {
        SettingsManager settings = new SettingsManager(this);
        settings.setOnboardingCompleted(true);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.example.investmind;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnCompound = findViewById(R.id.btnCompound);
        btnCompound.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CompoundCalcActivity.class);
            startActivity(intent);
        });
        
        findViewById(R.id.cardCompound).setOnClickListener(v -> {
             Intent intent = new Intent(MainActivity.this, CompoundCalcActivity.class);
             startActivity(intent);
        });

        Button btnSimple = findViewById(R.id.btnSimple);
        btnSimple.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SimpleCalcActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.cardSimple).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SimpleCalcActivity.class);
            startActivity(intent);
        });

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_history) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}
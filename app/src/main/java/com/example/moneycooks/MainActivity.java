package com.example.moneycooks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.moneycooks.budget.Budget;
import com.example.moneycooks.pricecomparison.PriceComparison;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<CardView> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.options = Arrays.asList(findViewById(R.id.budgeting), findViewById(R.id.pricecomparison), findViewById(R.id.scams), findViewById(R.id.sales));

        for (int i = 0; i < this.options.size(); i++) {
            int op = i;
            this.options.get(i).setOnClickListener(view -> {
                switch (op) {
                    case 0:
                        // Start Budgeting activity
                        startActivity(new Intent(MainActivity.this, Budget.class));
                        break;
                    case 1:
                        // Start Price Comparison activity
                        startActivity(new Intent(MainActivity.this, PriceComparison.class));
                        break;
                    case 2:
                        // Start Scams activity
                        startActivity(new Intent( MainActivity.this, Scams.class));
                        break;
                    case 3:
                        // Start Info activity
                        startActivity(new Intent( MainActivity.this, Info.class));
                        break;
                }
            });
        }
    }
}
package com.example.seriestracker;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseActivity extends AppCompatActivity {
    public BaseActivity(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public void HomeButtonClick(View view) {
        Intent homeActivity = new Intent(this, HomeActivity.class);
        startActivity(homeActivity);
    }

    public void SearchButtonClick(View view) {
        Intent searchActivity = new Intent(this, SearchActivity.class);
        startActivity(searchActivity);
    }

    public void YouButtonClick(View view) {
        Intent youActivity = new Intent(this, YouActivity.class);
        startActivity(youActivity);
    }
}

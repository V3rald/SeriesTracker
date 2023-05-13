package com.example.seriestracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        changeLanguage();

        Spinner languageSelector = findViewById(R.id.languageSpinner);

        String[] languages = getResources().getStringArray(R.array.languageItems);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPref.getString("language", "Magyar");

        languageSelector.setSelection(Arrays.asList(languages).indexOf(language));


        languageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String language = languages[position];

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("language", language);
                editor.apply();

                changeLanguage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void changeLanguage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPref.getString("language", "Magyar");

        TextView languageText = findViewById(R.id.languageText);
        Button homeButton = findViewById(R.id.home_button);
        Button searchButton = findViewById(R.id.search_button);
        Button friendsButton = findViewById(R.id.friends_button);
        Button AccountButton = findViewById(R.id.you_button);

        if(language.equals("Magyar")){
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.settings_title_hu));
            languageText.setText(getResources().getString(R.string.language_text_hu));

            homeButton.setText(getResources().getString(R.string.home_text_hu));
            searchButton.setText(getResources().getString(R.string.search_text_hu));
            friendsButton.setText(getResources().getString(R.string.friends_text_hu));
            AccountButton.setText(getResources().getString(R.string.account_text_hu));
        }else{
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.settings_title));
            languageText.setText(getResources().getString(R.string.language_text));

            homeButton.setText(getResources().getString(R.string.home_text));
            searchButton.setText(getResources().getString(R.string.search_text));
            friendsButton.setText(getResources().getString(R.string.friends_text));
            AccountButton.setText(getResources().getString(R.string.account_text));
        }
    }
}
package com.example.seriestracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.seriestracker.imdbapi.IMDBAPI;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        changeLanguage();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        CheckBox advancedSearch = findViewById(R.id.advancedSearch);
        ConstraintLayout advancedSearchField = findViewById(R.id.advancedSearchField);
        advancedSearch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                advancedSearchField.setVisibility(View.VISIBLE);
            }else{
                advancedSearchField.setVisibility(View.GONE);
            }
        });
    }

    public void SearchContentClick(View view) {
        InputMethodManager mImMan = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mImMan.hideSoftInputFromWindow(view.getWindowToken(), 0);

        String searchContentText = ((TextView)findViewById(R.id.search_content_text)).getText().toString();
        ConstraintLayout advancedSearchField = findViewById(R.id.advancedSearchField);

        if (!TextUtils.isEmpty(searchContentText) && searchContentText.length() >= 3){
            if(advancedSearchField.getVisibility() == View.GONE){
                IMDBAPI.searchAll(this, searchContentText, "", "");
            }else{
                EditText genreText = findViewById(R.id.genreText);
                Spinner typeSpinner = findViewById(R.id.typeSpinner);
                String type = "tv_series";
                if(typeSpinner.getSelectedItemPosition() == 1){
                    type = "tv_movies";
                }
                IMDBAPI.searchAll(this, searchContentText, genreText.getText().toString(), type);
            }
        }
    }

    @Override
    public void changeLanguage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPref.getString("language", "Magyar");

        Button searchContentButton = findViewById(R.id.search_content_button);
        EditText searchContentText = findViewById(R.id.search_content_text);

        Button advancedSearch = findViewById(R.id.advancedSearch);
        EditText genreText = findViewById(R.id.genreText);
        Spinner typeSpinner = findViewById(R.id.typeSpinner);

        Button homeButton = findViewById(R.id.home_button);
        Button searchButton = findViewById(R.id.search_button);
        Button friendsButton = findViewById(R.id.friends_button);
        Button AccountButton = findViewById(R.id.you_button);

        if(language.equals("Magyar")){
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.search_text_hu));

            searchContentButton.setText(getResources().getString(R.string.search_text_hu));
            searchContentText.setHint(getResources().getString(R.string.search_content_hint_hu));
            advancedSearch.setText(getResources().getString(R.string.advanced_search_hu));
            genreText.setHint(getResources().getString(R.string.genre_hint_hu));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.type_hu));
            typeSpinner.setAdapter(adapter);

            homeButton.setText(getResources().getString(R.string.home_text_hu));
            searchButton.setText(getResources().getString(R.string.search_text_hu));
            friendsButton.setText(getResources().getString(R.string.friends_text_hu));
            AccountButton.setText(getResources().getString(R.string.account_text_hu));
        }else{
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.search_text));

            searchContentButton.setText(getResources().getString(R.string.search_text));
            searchContentText.setHint(getResources().getString(R.string.search_content_hint));
            advancedSearch.setText(getResources().getString(R.string.advanced_search));
            genreText.setHint(getResources().getString(R.string.genre_hint));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.type));
            typeSpinner.setAdapter(adapter);

            homeButton.setText(getResources().getString(R.string.home_text));
            searchButton.setText(getResources().getString(R.string.search_text));
            friendsButton.setText(getResources().getString(R.string.friends_text));
            AccountButton.setText(getResources().getString(R.string.account_text));
        }
    }
}
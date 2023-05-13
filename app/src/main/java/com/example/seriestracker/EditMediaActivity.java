package com.example.seriestracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.imdbapi.models.SearchResult;
import com.example.seriestracker.user.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditMediaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_media);

        changeLanguage();

        Intent receivedIntent = getIntent();
        String mediaJson = receivedIntent.getStringExtra("media");

        Type mediaType = new TypeToken<SearchResult>(){}.getType();
        SearchResult media = new Gson().fromJson(mediaJson, mediaType);

        TextView title = findViewById(R.id.titText);
        TextView description = findViewById(R.id.descText);

        Spinner ratingSpinner = findViewById(R.id.ratingSpinner);
        Spinner statusSpinner = findViewById(R.id.statusSpinner);

        ratingSpinner.setSelection(media.getRating() - 1);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPref.getString("language", "Magyar");



        if(media.getStatus().equals("PTW")){
            statusSpinner.setSelection(0);
        }else if(media.getStatus().equals("C")){
            statusSpinner.setSelection(1);
        }else{
            statusSpinner.setSelection(2);
        }

        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new FB().changeRating(EditMediaActivity.this, User.userLoggedIn(EditMediaActivity.this), media, position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> status_hu = Arrays.asList(getResources().getStringArray(R.array.statuses_hu));
                List<String> status_en = Arrays.asList(getResources().getStringArray(R.array.statuses));
                String[] statuses = {"PTW", "C", "W"};
                String selected = (String) parent.getSelectedItem();

                int index = 0;

                if(status_hu.contains(selected)){
                    index = status_hu.indexOf(selected);
                }else{
                    index = status_en.indexOf(selected);
                }

                new FB().changeStatus(EditMediaActivity.this, User.userLoggedIn(EditMediaActivity.this), media, statuses[index]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        title.setText(media.getTitle());
        description.setText(media.getDescription());

    }

    @Override
    public void changeLanguage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPref.getString("language", "Magyar");

        Button homeButton = findViewById(R.id.home_button);
        Button searchButton = findViewById(R.id.search_button);
        Button friendsButton = findViewById(R.id.friends_button);
        Button AccountButton = findViewById(R.id.you_button);

        Spinner statusSpinner = findViewById(R.id.statusSpinner);

        if(language.equals("Magyar")){
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.edit_media_hu));

            homeButton.setText(getResources().getString(R.string.home_text_hu));
            searchButton.setText(getResources().getString(R.string.search_text_hu));
            friendsButton.setText(getResources().getString(R.string.friends_text_hu));
            AccountButton.setText(getResources().getString(R.string.account_text_hu));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.statuses_hu));
            statusSpinner.setAdapter(adapter);
        }else{
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.edit_media));

            homeButton.setText(getResources().getString(R.string.home_text));
            searchButton.setText(getResources().getString(R.string.search_text));
            friendsButton.setText(getResources().getString(R.string.friends_text));
            AccountButton.setText(getResources().getString(R.string.account_text));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.statuses));
            statusSpinner.setAdapter(adapter);
        }
    }
}
package com.example.seriestracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.user.Friend;
import com.example.seriestracker.imdbapi.models.SearchResult;
import com.example.seriestracker.user.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if (TextUtils.isEmpty(User.userLoggedIn(this))){
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivity(loginActivity);
            return;
        }

        changeLanguage();
    }

    public void LogoutButtonClick(View view) {
        User.logoutUser(this);
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
    }

    public void SettingsButtonClick(View view) {
        Intent settingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(settingsActivity);
    }

    @Override
    public void changeLanguage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPref.getString("language", "Magyar");

        Button logoutButton = findViewById(R.id.logoutButton);
        Button settingsButton = findViewById(R.id.settingsButton);

        Button homeButton = findViewById(R.id.home_button);
        Button searchButton = findViewById(R.id.search_button);
        Button friendsButton = findViewById(R.id.friends_button);
        Button AccountButton = findViewById(R.id.you_button);

        TextView friendNumber = findViewById(R.id.friendNumber);
        TextView favouriteGenre = findViewById(R.id.favouriteGenre);

        TextView dateJoinedText = findViewById(R.id.dateJoinedText);
        Type searchResultListType = new TypeToken<ArrayList<SearchResult>>(){}.getType();

        if(language.equals("Magyar")){
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.account_text_hu));

            logoutButton.setText(getResources().getString(R.string.logout_text_hu));
            settingsButton.setText(getResources().getString(R.string.settings_title_hu));

            homeButton.setText(getResources().getString(R.string.home_text_hu));
            searchButton.setText(getResources().getString(R.string.search_text_hu));
            friendsButton.setText(getResources().getString(R.string.friends_text_hu));
            AccountButton.setText(getResources().getString(R.string.account_text_hu));


            new FB().getDateJoined(this, User.userLoggedIn(this), callbackValue -> {
                String date = (String) callbackValue.get("date_joined");
                dateJoinedText.setText(MessageFormat.format("{0}: {1}", getResources().getString(R.string.date_joined_hu), date));
            });


            new FB().getFriends(this, User.userLoggedIn(this), callback -> {
                List<Friend> friends = callback.get("value");
                friends.removeIf(f -> !f.getStatus().equals("Friend"));
                friendNumber.setText(MessageFormat.format("{0}: {1}", getResources().getString(R.string.number_of_friends_hu), friends.size()));
            });

            new FB().getContents(this, User.userLoggedIn(this), callback -> {
                String watchlistJson  = (String) callback.get("watchlist");

                Map<String, Integer> genreMapped = new HashMap<>();
                List<SearchResult> contents = new Gson().fromJson(watchlistJson, searchResultListType);

                if(contents == null || contents.size() == 0){
                    favouriteGenre.setText(MessageFormat.format("{0}: {1}", getResources().getString(R.string.favourite_genre_hu), ""));
                }else{
                    for(SearchResult search : contents){
                        for(String genre : search.getGenres().split(", ")){
                            if(genreMapped.containsKey(genre)){
                                genreMapped.put(genre, genreMapped.get(genre) + 1);
                            }else{
                                genreMapped.put(genre, 1);
                            }
                        }
                    }

                    int max = 0;
                    String genre = "";
                    for(Map.Entry<String, Integer> entry : genreMapped.entrySet()){
                        if(entry.getValue() > max){
                            max = entry.getValue();
                            genre = entry.getKey();
                        }
                    }

                    favouriteGenre.setText(MessageFormat.format("{0}: {1}", getResources().getString(R.string.favourite_genre_hu), genre));
                }
            });
        }else{
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.account_text));

            logoutButton.setText(getResources().getString(R.string.logout_text));
            settingsButton.setText(getResources().getString(R.string.settings_title));

            homeButton.setText(getResources().getString(R.string.home_text));
            searchButton.setText(getResources().getString(R.string.search_text));
            friendsButton.setText(getResources().getString(R.string.friends_text));
            AccountButton.setText(getResources().getString(R.string.account_text));


            new FB().getDateJoined(this, User.userLoggedIn(this), callbackValue -> {
                String date = (String) callbackValue.get("date_joined");
                dateJoinedText.setText(MessageFormat.format("{0}: {1}", getResources().getString(R.string.date_joined), date));
            });

            new FB().getFriends(this, User.userLoggedIn(this), callback -> {
                List<Friend> friends = callback.get("value");
                friends.removeIf(f -> !f.getStatus().equals("Friend"));
                friendNumber.setText(MessageFormat.format("{0}: {1}", getResources().getString(R.string.number_of_friends), friends.size()));
            });

            new FB().getContents(this, User.userLoggedIn(this), callback -> {
                String watchlistJson  = (String) callback.get("watchlist");

                Map<String, Integer> genreMapped = new HashMap<>();
                List<SearchResult> contents = new Gson().fromJson(watchlistJson, searchResultListType);

                if(contents == null || contents.size() == 0) {
                    favouriteGenre.setText(MessageFormat.format("{0}: {1}", getResources().getString(R.string.favourite_genre), ""));
                }else{
                    for(SearchResult search : contents){
                        for(String genre : search.getGenres().split(", ")){
                            if(genreMapped.containsKey(genre)){
                                genreMapped.put(genre, genreMapped.get(genre) + 1);
                            }else{
                                genreMapped.put(genre, 1);
                            }
                        }
                    }

                    int max = 0;
                    String genre = "";
                    for(Map.Entry<String, Integer> entry : genreMapped.entrySet()){
                        if(entry.getValue() > max){
                            max = entry.getValue();
                            genre = entry.getKey();
                        }
                    }

                    favouriteGenre.setText(MessageFormat.format("{0}: {1}", getResources().getString(R.string.favourite_genre), genre));
                }
            });
        }
    }
}
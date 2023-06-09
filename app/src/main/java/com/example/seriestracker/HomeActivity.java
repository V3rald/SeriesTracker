package com.example.seriestracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seriestracker.adapters.SearchRecyclerAdapter;
import com.example.seriestracker.adapters.WatchlistRecyclerAdapter;
import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.imdbapi.models.SearchResult;
import com.example.seriestracker.user.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        changeLanguage();

        String userLoggedIn = User.userLoggedIn(this);

        if (TextUtils.isEmpty(userLoggedIn)){
            return;
        }


        new FB().getContents(this, userLoggedIn, callbackValue -> {
            String json = (String) callbackValue.get("watchlist");

            Type searchResultListType = new TypeToken<ArrayList<SearchResult>>(){}.getType();
            List<SearchResult> watchlist = new Gson().fromJson(json, searchResultListType);

            if (watchlist == null){
                watchlist = new ArrayList<>();
            }

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            WatchlistRecyclerAdapter adapter = new WatchlistRecyclerAdapter(this, watchlist);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });
    }

    @Override
    public void changeLanguage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPref.getString("language", "Magyar");

        TextView watchlistText = findViewById(R.id.watchlistText);
        Button homeButton = findViewById(R.id.home_button);
        Button searchButton = findViewById(R.id.search_button);
        Button friendsButton = findViewById(R.id.friends_button);
        Button AccountButton = findViewById(R.id.you_button);

        if(language.equals("Magyar")){
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.home_text_hu));
            watchlistText.setText(getResources().getString(R.string.watchlist_text_hu));

            homeButton.setText(getResources().getString(R.string.home_text_hu));
            searchButton.setText(getResources().getString(R.string.search_text_hu));
            friendsButton.setText(getResources().getString(R.string.friends_text_hu));
            AccountButton.setText(getResources().getString(R.string.account_text_hu));
        }else{
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.home_text));
            watchlistText.setText(getResources().getString(R.string.watchlist_text));

            homeButton.setText(getResources().getString(R.string.home_text));
            searchButton.setText(getResources().getString(R.string.search_text));
            friendsButton.setText(getResources().getString(R.string.friends_text));
            AccountButton.setText(getResources().getString(R.string.account_text));
        }
    }
}
package com.example.seriestracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

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

        setTitle(getTitle() + " - Home");

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
}
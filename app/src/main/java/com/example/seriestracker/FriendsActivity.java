package com.example.seriestracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seriestracker.adapters.FriendsRecyclerAdapter;
import com.example.seriestracker.adapters.RequestFriendRecyclerAdapter;
import com.example.seriestracker.adapters.PendingFriendsRecyclerAdapter;
import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.user.Friend;
import com.example.seriestracker.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FriendsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);


        String userLoggedIn = User.userLoggedIn(this);


        if (TextUtils.isEmpty(userLoggedIn)){
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivity(loginActivity);
            return;
        }

        changeLanguage();

        new FB().getFriends(this, userLoggedIn, friendsCallbackValue -> {
            new FB().getFriendRecommendations(this, userLoggedIn, friendsCallbackValue.get("value"), callbackValue -> {
                HashMap<String, Integer> friendRecommendations = (HashMap<String, Integer>) callbackValue;
                List<Friend> potentialFriends = new ArrayList<>();

                friendRecommendations.entrySet().stream()
                        .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                        .forEach(k -> potentialFriends.add(new Friend(k.getKey(), k.getValue())));

                List<Friend> pendingFriends = friendsCallbackValue.get("value").stream().filter(s -> s.getStatus().equals("Pending: Requested")).collect(Collectors.toList());

                RecyclerView pendingRecyclerView = findViewById(R.id.pending_recyclerView);
                PendingFriendsRecyclerAdapter pendingAdapter = new PendingFriendsRecyclerAdapter(this, pendingFriends);
                pendingRecyclerView.setAdapter(pendingAdapter);
                pendingRecyclerView.setLayoutManager(new LinearLayoutManager(this));


                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                RequestFriendRecyclerAdapter adapter = new RequestFriendRecyclerAdapter(this, potentialFriends);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            });

            List<Friend> actualFriends = new ArrayList<>();

            for(Friend f : friendsCallbackValue.get("value")){
                if(f.getStatus().equals("Friend")){
                    actualFriends.add(f);
                }
            }

            RecyclerView friends_recyclerView = findViewById(R.id.friends_recyclerView);
            FriendsRecyclerAdapter friends_adapter = new FriendsRecyclerAdapter(this, actualFriends);
            friends_recyclerView.setAdapter(friends_adapter);
            friends_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });
    }

    @Override
    public void changeLanguage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPref.getString("language", "Magyar");

        TextView friendsText = findViewById(R.id.friends_text);
        TextView pendingText = findViewById(R.id.pending_text);
        TextView suggestionsText = findViewById(R.id.suggestions_text);


        Button homeButton = findViewById(R.id.home_button);
        Button searchButton = findViewById(R.id.search_button);
        Button friendsButton = findViewById(R.id.friends_button);
        Button AccountButton = findViewById(R.id.you_button);

        if(language.equals("Magyar")){
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.friends_text_hu));
            friendsText.setText(getResources().getString(R.string.friends_text_hu));
            pendingText.setText(getResources().getString(R.string.pending_text_hu));
            suggestionsText.setText(getResources().getString(R.string.suggestions_text_hu));


            homeButton.setText(getResources().getString(R.string.home_text_hu));
            searchButton.setText(getResources().getString(R.string.search_text_hu));
            friendsButton.setText(getResources().getString(R.string.friends_text_hu));
            AccountButton.setText(getResources().getString(R.string.account_text_hu));
        }else{
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.friends_text));
            friendsText.setText(getResources().getString(R.string.friends_text));
            pendingText.setText(getResources().getString(R.string.pending_text));
            suggestionsText.setText(getResources().getString(R.string.suggestions_text));

            homeButton.setText(getResources().getString(R.string.home_text));
            searchButton.setText(getResources().getString(R.string.search_text));
            friendsButton.setText(getResources().getString(R.string.friends_text));
            AccountButton.setText(getResources().getString(R.string.account_text));
        }
    }
}
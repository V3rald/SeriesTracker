package com.example.seriestracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.seriestracker.adapters.ChatRecyclerAdapter;
import com.example.seriestracker.adapters.WatchlistRecyclerAdapter;
import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.user.Chat;
import com.example.seriestracker.user.User;

import java.util.List;

public class MessageActivity extends BaseActivity {
    String friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent receivedIntent = getIntent();
        String friend = receivedIntent.getStringExtra("friend");
        this.friend = friend;

        setTitle(getTitle() + " - " + friend);

        changeLanguage();

        new FB().updateChat(this, User.userLoggedIn(this), friend, callback -> {
            List<Chat> chatList = callback.get("value");

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            ChatRecyclerAdapter adapter = new ChatRecyclerAdapter(this, chatList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });
    }

    public void sendChat(View view){
        TextView text = findViewById(R.id.chatText);

        if(!TextUtils.isEmpty(text.getText().toString())){
            new FB().sendChat(this, User.userLoggedIn(this), friend, text.getText().toString());
            text.setText("");
        }
    }

    @Override
    public void changeLanguage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPref.getString("language", "Magyar");

        Button sendButton = findViewById(R.id.SendChatButton);

        Button homeButton = findViewById(R.id.home_button);
        Button searchButton = findViewById(R.id.search_button);
        Button friendsButton = findViewById(R.id.friends_button);
        Button AccountButton = findViewById(R.id.you_button);

        if(language.equals("Magyar")){
            sendButton.setText(getResources().getString(R.string.send_button_hu));

            homeButton.setText(getResources().getString(R.string.home_text_hu));
            searchButton.setText(getResources().getString(R.string.search_text_hu));
            friendsButton.setText(getResources().getString(R.string.friends_text_hu));
            AccountButton.setText(getResources().getString(R.string.account_text_hu));
        }else{
            sendButton.setText(getResources().getString(R.string.send_button));

            homeButton.setText(getResources().getString(R.string.home_text));
            searchButton.setText(getResources().getString(R.string.search_text));
            friendsButton.setText(getResources().getString(R.string.friends_text));
            AccountButton.setText(getResources().getString(R.string.account_text));
        }
    }
}
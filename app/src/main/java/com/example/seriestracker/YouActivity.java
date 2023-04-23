package com.example.seriestracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.user.User;

import java.text.MessageFormat;
import java.util.Map;

public class YouActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you);

        setTitle(getTitle() + " - You");

        if (User.userLoggedIn(this).equals("")){
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivity(loginActivity);
        }else{
            TextView dateJoinedText = findViewById(R.id.dateJoinedText);

            new FB().getDateJoined(this, User.userLoggedIn(this), callbackValue -> {
                String date = (String) callbackValue.get("date_joined");
                dateJoinedText.setText(MessageFormat.format("Date joined: {0}", date));
            });
        }
    }

    public void LogoutButtonClick(View view) {
        User.logoutUser(this);
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
    }
}
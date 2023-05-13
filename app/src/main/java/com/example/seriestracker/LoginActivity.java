package com.example.seriestracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.user.User;

public class LoginActivity extends BaseActivity {

    TextView emailText;
    TextView passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        changeLanguage();

        if (!User.userLoggedIn(this).equals("")){
            Intent youActivity = new Intent(this, AccountActivity.class);
            startActivity(youActivity);
        }

        emailText = findViewById(R.id.LoginEmailText);
        passwordText = findViewById(R.id.LoginPasswordText);
    }

    public void LoginButtonClick(View view) {
        if (!TextUtils.isEmpty(emailText.getText().toString()) && !TextUtils.isEmpty(passwordText.getText().toString())){
            FB.auth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    User.saveUser(this, emailText.getText().toString());
                    Intent youActivity = new Intent(this, AccountActivity.class);
                    startActivity(youActivity);
                }else{
                    Toast.makeText(this, "Wrong email or password!", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(this, "Wrong email or password!", Toast.LENGTH_LONG).show();
        }
    }

    public void RegisterActivityButtonClick(View view) {
        Intent registerActivity = new Intent(this, RegisterActivity.class);
        startActivity(registerActivity);
    }

    @Override
    public void changeLanguage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPref.getString("language", "Magyar");

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.switchRegisterActvityButton);
        TextView emailText = findViewById(R.id.LoginEmailText);
        TextView passwordText = findViewById(R.id.LoginPasswordText);


        Button homeButton = findViewById(R.id.home_button);
        Button searchButton = findViewById(R.id.search_button);
        Button friendsButton = findViewById(R.id.friends_button);
        Button AccountButton = findViewById(R.id.you_button);

        if(language.equals("Magyar")){
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.login_title_hu));
            loginButton.setText(getResources().getString(R.string.login_title_hu));
            registerButton.setText(getResources().getString(R.string.register_title_hu));
            emailText.setHint(getResources().getString(R.string.email_text));
            passwordText.setHint(getResources().getString(R.string.password_text_hu));


            homeButton.setText(getResources().getString(R.string.home_text_hu));
            searchButton.setText(getResources().getString(R.string.search_text_hu));
            friendsButton.setText(getResources().getString(R.string.friends_text_hu));
            AccountButton.setText(getResources().getString(R.string.account_text_hu));
        }else{
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.login_title));
            registerButton.setText(getResources().getString(R.string.register_title));
            emailText.setHint(getResources().getString(R.string.email_text));
            passwordText.setHint(getResources().getString(R.string.password_text));

            homeButton.setText(getResources().getString(R.string.home_text));
            searchButton.setText(getResources().getString(R.string.search_text));
            friendsButton.setText(getResources().getString(R.string.friends_text));
            AccountButton.setText(getResources().getString(R.string.account_text));
        }
    }
}
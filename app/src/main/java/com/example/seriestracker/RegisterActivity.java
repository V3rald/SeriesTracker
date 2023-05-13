package com.example.seriestracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends BaseActivity {

    TextView emailText;
    TextView passwordText;
    TextView passwordTextAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        changeLanguage();

        emailText = findViewById(R.id.registerEmailText);
        passwordText = findViewById(R.id.registerPasswordText);
        passwordTextAgain = findViewById(R.id.registerPasswordTextAgain);
    }

    public void RegisterButtonClick(View view) {
        if (!TextUtils.isEmpty(emailText.getText().toString())
                && !TextUtils.isEmpty(passwordText.getText().toString())
                && !TextUtils.isEmpty(passwordTextAgain.getText().toString())){

            if (passwordText.getText().toString().equals(passwordTextAgain.getText().toString())){
                FB.auth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        new FB().setDateJoined(this, emailText.getText().toString());
                        User.saveUser(this, emailText.getText().toString());
                        Intent loginActivity = new Intent(this, LoginActivity.class);
                        startActivity(loginActivity);
                    } else{
                        Toast.makeText(this, "Registration failed!", Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Fill in all the texts!", Toast.LENGTH_LONG).show();
        }
    }

    public void LoginActivityButtonClick(View view) {
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
    }

    @Override
    public void changeLanguage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPref.getString("language", "Magyar");

        Button loginButton = findViewById(R.id.loginButton3);
        Button registerButton = findViewById(R.id.switchRegisterActvityButton3);
        TextView emailText = findViewById(R.id.registerEmailText);
        TextView passwordText = findViewById(R.id.registerPasswordText);
        TextView passwordTextAgain = findViewById(R.id.registerPasswordTextAgain);


        Button homeButton = findViewById(R.id.home_button);
        Button searchButton = findViewById(R.id.search_button);
        Button friendsButton = findViewById(R.id.friends_button);
        Button AccountButton = findViewById(R.id.you_button);

        if(language.equals("Magyar")){
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.register_title_hu));
            loginButton.setText(getResources().getString(R.string.login_title_hu));
            registerButton.setText(getResources().getString(R.string.register_title_hu));
            emailText.setHint(getResources().getString(R.string.email_text));
            passwordText.setHint(getResources().getString(R.string.password_text_hu));
            passwordTextAgain.setHint(getResources().getString(R.string.password_text_hu));


            homeButton.setText(getResources().getString(R.string.home_text_hu));
            searchButton.setText(getResources().getString(R.string.search_text_hu));
            friendsButton.setText(getResources().getString(R.string.friends_text_hu));
            AccountButton.setText(getResources().getString(R.string.account_text_hu));
        }else{
            setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.register_title));
            registerButton.setText(getResources().getString(R.string.register_title));
            emailText.setHint(getResources().getString(R.string.email_text));
            passwordText.setHint(getResources().getString(R.string.password_text));
            passwordTextAgain.setHint(getResources().getString(R.string.password_text));

            homeButton.setText(getResources().getString(R.string.home_text));
            searchButton.setText(getResources().getString(R.string.search_text));
            friendsButton.setText(getResources().getString(R.string.friends_text));
            AccountButton.setText(getResources().getString(R.string.account_text));
        }
    }
}
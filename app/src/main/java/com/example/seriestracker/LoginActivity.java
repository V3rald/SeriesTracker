package com.example.seriestracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seriestracker.firebaseapi.FB;
import com.example.seriestracker.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends BaseActivity {

    TextView emailText;
    TextView passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(getTitle() + " - Login");

        if (!User.userLoggedIn(this).equals("")){
            Intent youActivity = new Intent(this, YouActivity.class);
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
                    Intent youActivity = new Intent(this, YouActivity.class);
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
}
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

        setTitle(getTitle() + " - Register");

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
}
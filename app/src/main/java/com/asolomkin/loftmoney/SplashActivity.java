package com.asolomkin.loftmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkToken();
    }
    private void checkToken(){
        String token = ((LoftApp) getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");
        if (TextUtils.isEmpty(token)) {
            routToLogin();
        } else {
            routToMain();
        }

    }

    private void routToMain(){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }
    private void routToLogin(){
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
    }
}
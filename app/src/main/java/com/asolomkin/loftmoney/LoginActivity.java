package com.asolomkin.loftmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Api api;
    Button loginButtonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        api = ((LoftApp)getApplication()).getApi();
        loginButtonView = findViewById(R.id.loginButtonView);

        auth();
    }

    private void auth() {
        loginButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String socialUserId = String.valueOf(new Random().nextInt());
                Call<AuthResponse> auth = api.auth(socialUserId);
                auth.enqueue(new Callback<AuthResponse>() {

                    @Override
                    public void onResponse(
                            final Call<AuthResponse> call, final Response<AuthResponse> response
                    ) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                LoginActivity.this).edit();
                        editor.putString(LoftApp.TOKEN_KEY, response.body().getToken());
                        editor.apply();
                    }

                    @Override
                    public void onFailure(final Call<AuthResponse> call, final Throwable t) {

                    }
                });
            }
        });
    }
}
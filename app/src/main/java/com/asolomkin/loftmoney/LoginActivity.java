package com.asolomkin.loftmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        api = ((LoftApp)getApplication()).getApi();
        Button authButton = findViewById(R.id.enter_button);
        authButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString(MainActivity.TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            auth();
        } else {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    private void auth() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Call<AuthResponse> auth = api.auth(androidId);
        auth.enqueue(new Callback<AuthResponse>() {

            @Override
            public void onResponse(
                    final Call<AuthResponse> call, final Response<AuthResponse> response
            ) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                        LoginActivity.this).edit();
                editor.putString(MainActivity.TOKEN, response.body().getToken());
                editor.apply();
            }

            @Override
            public void onFailure(final Call<AuthResponse> call, final Throwable t) {

            }
        });
    }
}


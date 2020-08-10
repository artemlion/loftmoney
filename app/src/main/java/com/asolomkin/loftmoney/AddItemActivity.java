package com.asolomkin.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity extends AppCompatActivity {
    public static String ANY_KEY = "any_key";


    private EditText mNameEditText;
    private EditText mPriceEditText;
    private Button addButton;

    private String activePosition;



    String name;
    String value;

    CompositeDisposable compositeDisposable = new CompositeDisposable();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mNameEditText = findViewById(R.id.name_edittext);
        mPriceEditText = findViewById(R.id.price_edittext);
        addButton = findViewById(R.id.add_button);

        try {
            Intent intent = getIntent();
            int tempPosition = intent.getIntExtra(ANY_KEY, 0);
            if (tempPosition == 0)
            {activePosition = "expense";
            }


            else {activePosition = "income";
            }
        } catch (Exception exception){
            Log.e("error", exception.getLocalizedMessage());
        }

        configureInputViews();
        configureExpenseAdding();

    }

    private void configureExpenseAdding(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                String token = getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");
                compositeDisposable.add(((LoftApp) getApplication()).getApi().addMoney(token, value, name, activePosition)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                Toast.makeText(getApplicationContext(), "Expense was added successfully", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(getApplicationContext(), "Expense was not added because"
                                        + throwable.getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }));

            }
        });
    }

    private void configureInputViews() {
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = editable.toString();
                checkEditTextHasText();

            }
        });
        mPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value = editable.toString();
                checkEditTextHasText();

            }
        });

    }
    private void checkEditTextHasText() {
        addButton.setEnabled(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.static_animation, R.anim.zoom_out);
    }
}

package com.asolomkin.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;


public class AddItemActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mPriceEditText;
    private Button addButton;

    private String name;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mNameEditText = findViewById(R.id.name_edittext);
        mNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void onTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                name = editable.toString();
                checkEditTextHasText();
            }
        });
        mPriceEditText = findViewById(R.id.price_edittext);
        mPriceEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void onTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                value = editable.toString();
                checkEditTextHasText();
            }
        });

        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
                    setResult(
                            RESULT_OK,
                            new Intent().putExtra("name", name).putExtra("price", value));
                    finish();
                }
            }
        });
    }

    public void checkEditTextHasText() {
        addButton.setEnabled(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.static_animation, R.anim.zoom_out);
    }
}

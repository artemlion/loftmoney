package com.asolomkin.loftmoney;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100 ;
    RecyclerView recyclerView;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button callAddButton = findViewById(R.id.call_add_item_activity);
        callAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivityForResult(new Intent(MainActivity.this, AddItemActivity.class),
                        REQUEST_CODE);
            }
        });

        recyclerView = findViewById(R.id.costsRecyclerView);
        itemsAdapter = new ItemsAdapter();
                recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));

        itemsAdapter.setData(generateExpenses());
        itemsAdapter.addData(generateIncomes());

    }

    private List<Item> generateExpenses() {
        List<Item> moneyCellModels = new ArrayList<>();
        moneyCellModels.add(new Item("Молоко", 70, R.color.expenseColor));
        moneyCellModels.add(new Item("Зубная щётка", 70, R.color.expenseColor));
        moneyCellModels.add(new Item("Сковородка с антипригарным покрытием",
                1670, R.color.expenseColor));

        return moneyCellModels;
    }

    private List<Item> generateIncomes() {
        List<Item> moneyCellModels = new ArrayList<>();
        moneyCellModels.add(new Item("Зарплата.Июнь", 70000, R.color.incomeColor));
        moneyCellModels.add(new Item("Премия", 7000, R.color.incomeColor));
        moneyCellModels.add(new Item("Олег наконец-то вернул долг",
                300000, R.color.incomeColor));

        return moneyCellModels;
    }
    @Override
    protected void onActivityResult(
            final int requestCode, final int resultCode, @Nullable final Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        int price;
        try {
            price = Integer.parseInt(data.getStringExtra("price"));
        } catch (NumberFormatException e) {
            price = 0;
        }
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            itemsAdapter.addItem(new Item(data.getStringExtra("name"), price, R.color.colorPrimary));
        }
    }
}


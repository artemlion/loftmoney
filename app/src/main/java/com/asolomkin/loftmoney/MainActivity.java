package com.asolomkin.loftmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import com.asolomkin.loftmoney.cells.money.MoneyAdapter;
import com.asolomkin.loftmoney.cells.money.MoneyAdapterClick;
import com.asolomkin.loftmoney.cells.money.MoneyCellModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MoneyAdapter moneyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.costsRecyclerView);
        moneyAdapter = new MoneyAdapter();
        moneyAdapter.setMoneyAdapterClick(new MoneyAdapterClick() {
            @Override
            public void onMoneyClick(MoneyCellModel moneyCellModel) {
                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                startActivity(intent);
            }

            });

        recyclerView.setAdapter(moneyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));

        moneyAdapter.setData(generateExpenses());


    }

    private List<MoneyCellModel> generateExpenses() {
        List<MoneyCellModel> moneyCellModels = new ArrayList<>();
        moneyCellModels.add(new MoneyCellModel("Молоко", "70 ₽", R.color.expenseColor));
        moneyCellModels.add(new MoneyCellModel("Зубная щётка", "70 ₽", R.color.expenseColor));
        moneyCellModels.add(new MoneyCellModel("Сковородка с антипригарным покрытием",
                "1670 ₽", R.color.expenseColor));

        return moneyCellModels;
    }

    private List<MoneyCellModel> generateIncomes() {
        List<MoneyCellModel> moneyCellModels = new ArrayList<>();
        moneyCellModels.add(new MoneyCellModel("Зарплата.Июнь", "70000 ₽", R.color.incomeColor));
        moneyCellModels.add(new MoneyCellModel("Премия", "7000 ₽", R.color.incomeColor));
        moneyCellModels.add(new MoneyCellModel("Олег наконец-то вернул долг",
                "300000 ₽", R.color.incomeColor));

        return moneyCellModels;
    }
}


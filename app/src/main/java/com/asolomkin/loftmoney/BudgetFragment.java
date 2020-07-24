package com.asolomkin.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class BudgetFragment extends Fragment {

    private static final int REQUEST_CODE = 100 ;
    RecyclerView recyclerView;
    ItemsAdapter itemsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);
        Button callAddButton = view.findViewById(R.id.call_add_item_activity);
        callAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivityForResult(new Intent(getActivity(), AddItemActivity.class),
                        REQUEST_CODE);
            }
        });

        recyclerView = view.findViewById(R.id.costsRecyclerView);
        itemsAdapter = new ItemsAdapter();
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(null,
                LinearLayoutManager.VERTICAL, false));

        itemsAdapter.setData(generateExpenses());
        itemsAdapter.addData(generateIncomes());
        return view;
    }
    private List<Item> generateExpenses() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Молоко", 70, R.color.expenseColor));
        items.add(new Item("Зубная щётка", 70, R.color.expenseColor));
        items.add(new Item("Сковородка с антипригарным покрытием",
                1670, R.color.expenseColor));

        return items;
    }

    private List<Item> generateIncomes() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Зарплата.Июнь", 70000, R.color.incomeColor));
        items.add(new Item("Премия", 7000, R.color.incomeColor));
        items.add(new Item("Олег наконец-то вернул долг",
                300000, R.color.incomeColor));

        return items;
    }
    @Override
    public void onActivityResult(
            final int requestCode, final int resultCode, @Nullable final Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        int price;
        try {
            price = Integer.parseInt(data.getStringExtra("price"));
        } catch (NumberFormatException e) {
            price = 0;
        }
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            itemsAdapter.addItem(new Item(data.getStringExtra("name"), price, R.color.colorPrimary));
        }
    }
}

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class BudgetFragment extends Fragment {

    public static final int REQUEST_CODE = 100 ;
    RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);


        recyclerView = view.findViewById(R.id.costsRecyclerView);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // loadItems(); //метод не прописан
            }
        });

        itemsAdapter = new ItemsAdapter();
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        generateExpenses();

        return view;

    }

    private void generateExpenses() {
        List<Item> items = new ArrayList<>(); //тут дальше не знаю что делать


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
            itemsAdapter.addItem(new Item(data.getStringExtra("name"), "price", R.color.colorPrimary));
        }
    }
}

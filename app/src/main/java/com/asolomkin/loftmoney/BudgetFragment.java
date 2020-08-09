package com.asolomkin.loftmoney;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class BudgetFragment extends Fragment {


    RecyclerView recyclerView;
    private static String ARG_1 = "arg_1";
    private int position;
    private ItemsAdapter itemsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_1);
             }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);


        recyclerView = view.findViewById(R.id.costsRecyclerView);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               generateExpenses();
            }
        });

        itemsAdapter = new ItemsAdapter();
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        generateExpenses();
    }

    private void generateExpenses() {
        final List<Item> mItemsList = new ArrayList<>();
        String token = ((LoftApp) getActivity().getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");

        Disposable disposable = ((LoftApp) getActivity().getApplication()).getApi().getMoney(token, String.valueOf(position))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer <List <MoneyItem>>() {
                    @Override
                    public void accept(List <MoneyItem> moneyItems) throws Exception {
                        itemsAdapter.clearItems();
                        mSwipeRefreshLayout.setRefreshing(false);
                        for (MoneyItem moneyItem : moneyItems) {
                            mItemsList.add(Item.getInstance(moneyItem));
                        }

                        itemsAdapter.setData(mItemsList);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        compositeDisposable.add(disposable);

    }
        public static BudgetFragment newInstance(int position) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_1, position);
        budgetFragment.setArguments(args);
        return budgetFragment;
    }

}


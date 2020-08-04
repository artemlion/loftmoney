package com.asolomkin.loftmoney;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ItemsAdapter itemsAdapter;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);


        recyclerView = view.findViewById(R.id.costsRecyclerView);


//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//               generateExpenses();
//            }
//        });

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
        Disposable disposable = ((LoftApp) getActivity().getApplication()).getApi().getMoney("expense")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoneyResponse>() {
                    @Override
                    public void accept(MoneyResponse moneyResponse) throws Exception {
//                        itemsAdapter.clearItems();
//                        mSwipeRefreshLayout.setRefreshing(false);
                        for (MoneyItem moneyItem : moneyResponse.getMoneyItemList()) {
                            mItemsList.add(Item.getInstance(moneyItem));
                        }

                        itemsAdapter.setData(mItemsList);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        mSwipeRefreshLayout.setRefreshing(false);

                    }
                });

        compositeDisposable.add(disposable);

    }

}

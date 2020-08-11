package com.asolomkin.loftmoney;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BudgetFragment extends Fragment implements ItemsAdapterListener, ActionMode.Callback{


    RecyclerView recyclerView;
    private static String ARG_1 = "arg_1";
    private int position;
    private ItemsAdapter itemsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ActionMode mActionMode;
    private Api mApi;
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
        itemsAdapter.setListener(this);
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

    @Override
    public void onItemClick(Item item, int position) {
        itemsAdapter.clearItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(itemsAdapter.getSelectedSize())));
        }
    }

    @Override
    public void onItemLongClick(Item item, int position) {
        if (mActionMode == null) {
            getActivity().startActionMode(this);
        }
        itemsAdapter.toggleItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(itemsAdapter.getSelectedSize())));
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        mActionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.remove) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.comfirmation)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removeItems();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }
        return true;
    }

    private void removeItems() {
        String token = ((LoftApp) getActivity().getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");
        //String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, ""); //или MainActivity??
        List<Integer> selectedItems = itemsAdapter.getSelectedItemsIds();
        for (Integer itemId : selectedItems) {
            Call<AuthResponse> call = mApi.removeItem(String.valueOf(itemId.intValue()), token);
            call.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    generateExpenses();
                    itemsAdapter.clearSelections();
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {

                }
            });//?
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mActionMode = null;
        itemsAdapter.clearSelections();

    }
}


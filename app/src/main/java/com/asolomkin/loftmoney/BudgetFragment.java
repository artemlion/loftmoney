package com.asolomkin.loftmoney;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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


    public static final int REQUEST_CODE = 100;
    private static final String COLOR_ID = "colorId";
    private static final String TYPE = "fragmentType";
    private ItemsAdapter itemsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ActionMode mActionMode;
    private Api api;

    public static BudgetFragment newInstance(final int colorId, final String type) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COLOR_ID, colorId);
        bundle.putString(TYPE, type);
        budgetFragment.setArguments(bundle);
        return budgetFragment;
    }


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ((LoftApp)getActivity().getApplication()).getApi();
        loadItems();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_budget, null);

        RecyclerView recyclerView = view.findViewById(R.id.costsRecyclerView);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        itemsAdapter = new ItemsAdapter(getArguments().getInt(COLOR_ID));
        itemsAdapter.setListener(this);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        return view;
    }

    @Override
    public void onActivityResult(
            final int requestCode, final int resultCode, @Nullable final Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            int price;
            try {
                price = Integer.parseInt(data.getStringExtra("price"));
            } catch (NumberFormatException e) {
                price = 0;
            }
            final int realPrice = price;
            final String name = data.getStringExtra("name");

            final String token = ((LoftApp) getActivity().getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");

            Call<AuthResponse> call = api.addItem(new AddItemRequest(name, getArguments().getString(TYPE), price), token);
            call.enqueue(new Callback<AuthResponse>() {

                @Override
                public void onResponse(
                        final Call<AuthResponse> call, final Response<AuthResponse> response
                ) {
                    if (response.body().getStatus().equals("success")) {
                        loadItems();
                    }
                }

                @Override
                public void onFailure(final Call<AuthResponse> call, final Throwable t) {
                    t.printStackTrace();
                }
            });

        }
    }

    public void loadItems() {
        final String token = ((LoftApp) getActivity().getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");

        Call<List<Item>> items = api.getItems(getArguments().getString(TYPE), token);
        items.enqueue(new Callback<List<Item>>() {

            @Override
            public void onResponse(
                    final Call<List<Item>> call, final Response<List<Item>> response
            ) {
                itemsAdapter.clearItems();
                mSwipeRefreshLayout.setRefreshing(false);
                List<Item> items = response.body();
                for (Item item : items) {
                    itemsAdapter.addItem(item);
                }
                ((MainActivity)getActivity()).loadBalance();
            }

            @Override
            public void onFailure(final Call<List<Item>> call, final Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    @Override
    public void onItemClick(final Item item, final int position) {
        itemsAdapter.clearItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(itemsAdapter.getSelectedSize())));
        }
    }

    @Override
    public void onItemLongClick(final Item item, final int position) {
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
        List<Integer> selectedItems = itemsAdapter.getSelectedItemsIds();
        for (Integer itemId : selectedItems) {
            Call<AuthResponse> call = api.removeItem(String.valueOf(itemId.intValue()), token);
            call.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(final Call<AuthResponse> call, final Response<AuthResponse> response) {
                    loadItems();
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


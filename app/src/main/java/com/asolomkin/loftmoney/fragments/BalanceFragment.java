package com.asolomkin.loftmoney.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.asolomkin.loftmoney.LoftApp;
import com.asolomkin.loftmoney.MainActivity;
import com.asolomkin.loftmoney.R;
import com.asolomkin.loftmoney.remote.Api;
import com.asolomkin.loftmoney.remote.BalanceResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceFragment extends Fragment {

    private Api mApi;
    private TextView myExpenses;
    private TextView myIncome;
    private TextView totalFinances;
    private BalanceView mDiagramView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static BalanceFragment newInstance() {
        return new BalanceFragment();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = ((LoftApp) getActivity().getApplication()).getApi();
        loadBalance();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_balance, null);
        myExpenses = view.findViewById(R.id.my_expences);
        myIncome = view.findViewById(R.id.my_income);
        totalFinances = view.findViewById(R.id.txtBalanceFinanceValue);
        mDiagramView = view.findViewById(R.id.balanceView);
        mSwipeRefreshLayout = view.findViewById(R.id.balance_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadBalance();
            }
        });
        return view;
    }

    public void loadBalance() {
        String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(MainActivity.TOKEN, "");
        final Call<BalanceResponse> responseCall = mApi.getBalance(token);
        responseCall.enqueue(new Callback<BalanceResponse>() {

            @Override
            public void onResponse(
                    final Call<BalanceResponse> call, final Response<BalanceResponse> response
            ) {

                final float totalExpenses = response.body().getTotalExpenses();
                final float totalIncome = response.body().getTotalIncome();

                myExpenses.setText(String.valueOf(totalExpenses));
                myIncome.setText(String.valueOf(totalIncome));
                totalFinances.setText(String.valueOf(totalIncome - totalExpenses));
                mDiagramView.update(totalExpenses, totalIncome);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(final Call<BalanceResponse> call, final Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}

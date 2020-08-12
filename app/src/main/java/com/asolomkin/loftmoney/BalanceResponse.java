package com.asolomkin.loftmoney;

import com.google.gson.annotations.SerializedName;

public class BalanceResponse {

    private String status;

    @SerializedName("total_expenses")
    private float totalExpenses;

    @SerializedName("total_income")
    private float totalIncome;

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public float getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(final float totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public float getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(final float totalIncome) {
        this.totalIncome = totalIncome;
    }
}

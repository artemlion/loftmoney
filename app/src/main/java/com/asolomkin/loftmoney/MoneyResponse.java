package com.asolomkin.loftmoney;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoneyResponse {

    @SerializedName("status") private String status;
    @SerializedName("data") private List<MoneyItem> moneyItemList;

    public String getStatus() {
        return status;
    }

    public List<MoneyItem> getMoneyItemList() {
        return moneyItemList;
    }
}

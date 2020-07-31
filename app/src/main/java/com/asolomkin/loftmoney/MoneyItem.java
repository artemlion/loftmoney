package com.asolomkin.loftmoney;

import com.google.gson.annotations.SerializedName;

public class MoneyItem {
    @SerializedName("id") private String itemId;
    private String name;
    private int price;
    private String type;
    private String date;

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}

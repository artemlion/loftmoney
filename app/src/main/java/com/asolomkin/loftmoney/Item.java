package com.asolomkin.loftmoney;

public class Item {
    public String name;
    public String price;
    private Integer color;

    public Item(String name, String price, Integer color) {
        this.name = name;
        this.price = price;
        this.color = color;
    }

    public static Item getInstance(MoneyItem moneyItem) {
        return new Item(moneyItem.getName(), moneyItem.getPrice() + " ₽",
                moneyItem.getType().equals("expense") ? R.color.expenseColor : R.color.incomeColor);


    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public Integer getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

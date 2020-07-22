package com.asolomkin.loftmoney.cells.money;

public class MoneyCellModel {
    public String name;
    public String value;
    private Integer color;

    public MoneyCellModel(String name, String value, Integer color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Integer getColor() {
        return color;
    }
}

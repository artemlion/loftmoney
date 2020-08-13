package com.asolomkin.loftmoney.cells;

import com.asolomkin.loftmoney.cells.Item;

public interface ItemsAdapterListener {
    public void onItemClick(Item item, int position);
    public void onItemLongClick (Item item, int position);
}

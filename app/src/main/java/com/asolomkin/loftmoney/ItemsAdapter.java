package com.asolomkin.loftmoney;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> mItemsList = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view, null);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        holder.bindItem(mItemsList.get(position));
    }

    public void addItem(Item item) {
        mItemsList.add(item);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mItemsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public void setData(List<Item> mItemsList) {
        this.mItemsList.clear();
        this.mItemsList.addAll(mItemsList);
        notifyDataSetChanged();
    }
    public void addData(List<Item> mItemsList) {
        this.mItemsList.addAll(mItemsList);
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameView;
        private TextView mPriceView;

        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);

            mNameView = itemView.findViewById(R.id.name_view);
            mPriceView = itemView.findViewById(R.id.price_view);
        }

        public void bindItem(final Item item) {
            mNameView.setText(item.getName());
            mPriceView.setText(item.getPrice());
            mPriceView.setTextColor(ContextCompat.getColor(mPriceView.getContext(), item.getColor()));
        }
    }
}

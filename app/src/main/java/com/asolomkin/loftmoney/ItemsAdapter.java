package com.asolomkin.loftmoney;

import android.util.SparseBooleanArray;
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
    private ItemsAdapterListener mListener;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();
    public void clearSelections(){
        mSelectedItems.clear();
        notifyDataSetChanged();
    }
    public void toggleItem(final int position){
        mSelectedItems.put(position, !mSelectedItems.get(position));
        notifyDataSetChanged();
    }

    public void clearItem(final int position) {
        mSelectedItems.put(position, false);
        notifyDataSetChanged();
    }

    public int getSelectedSize() {
        int result = 0;
        for (int i = 0; i < mItemsList.size(); i++) {
            if (mSelectedItems.get(i)) {
                result++;
            }
        }
        return result;
    }

    public List<Integer> getSelectedItemsIds() {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        for (Item item: mItemsList) {
                if (mSelectedItems.get(i)) {
                result.add(item.getId());
            }
            i++;
        }
        return result;
    }

    public void setListener(final ItemsAdapterListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view, null);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        holder.bindItem(mItemsList.get(position), mSelectedItems.get(position));
        holder.setListener(mListener, mItemsList.get(position), position);
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

        private View mItemView;
        private TextView mNameView;
        private TextView mPriceView;

        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            mItemView = itemView;
            mNameView = itemView.findViewById(R.id.name_view);
            mPriceView = itemView.findViewById(R.id.price_view);
        }

        public void bindItem(final Item item, final boolean isSelected) {
            mItemView.setSelected(isSelected);
            mNameView.setText(item.getName());
            mPriceView.setText(item.getPrice());
            mPriceView.setTextColor(ContextCompat.getColor(mPriceView.getContext(), item.getColor()));
        }

        public void setListener(final ItemsAdapterListener listener, final Item item, final int position) {
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item, position);
                }
            });
            mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(item, position);
                    return false;

                }
            });

        }
    }
}

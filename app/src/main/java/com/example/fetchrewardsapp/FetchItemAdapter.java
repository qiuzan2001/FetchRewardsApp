package com.example.fetchrewardsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FetchItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DisplayItem> displayItemList;

    // View Types
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    public FetchItemAdapter(List<DisplayItem> displayItemList) {
        this.displayItemList = displayItemList;
    }

    @Override
    public int getItemViewType(int position) {
        if (displayItemList.get(position) instanceof DisplayItem.Header) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DisplayItem displayItem = displayItemList.get(position);
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).headerTextView.setText("List " + ((DisplayItem.Header) displayItem).getListId());
        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).nameTextView.setText(((DisplayItem.Item) displayItem).getName());
        }
    }

    @Override
    public int getItemCount() {
        return displayItemList.size();
    }

    // ViewHolder for Header
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTextView;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.headerTextView);
        }
    }

    // ViewHolder for Item
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}

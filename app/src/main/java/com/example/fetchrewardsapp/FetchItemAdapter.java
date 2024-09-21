package com.example.fetchrewardsapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FetchItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<DisplayItem> displayItemList;

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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == VIEW_TYPE_HEADER) {
            // inflate header layout
            view = inflater.inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            // inflate item layout
            view = inflater.inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(view);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // get the display item at the current position
        DisplayItem displayItem = displayItemList.get(position);

        // check if the holder is a header or item and bind the corresponding data
        if (holder instanceof HeaderViewHolder) {
            // cast the holder to HeaderViewHolder && set the header text
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            int listId = ((DisplayItem.Header) displayItem).getListId();
            headerHolder.headerTextView.setText("List " + listId);
        } else if (holder instanceof ItemViewHolder) {
            // cast the holder to ItemViewHolder && set the item name
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            String itemName = ((DisplayItem.Item) displayItem).getName();
            itemHolder.nameTextView.setText(itemName);
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

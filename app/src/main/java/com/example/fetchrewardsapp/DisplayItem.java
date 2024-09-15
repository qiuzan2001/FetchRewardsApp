package com.example.fetchrewardsapp;

public abstract class DisplayItem {

    // Header subclass
    public static class Header extends DisplayItem {
        private int listId;

        public Header(int listId) {
            this.listId = listId;
        }

        public int getListId() {
            return listId;
        }
    }

    // Item subclass
    public static class Item extends DisplayItem {
        private String name;

        public Item(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

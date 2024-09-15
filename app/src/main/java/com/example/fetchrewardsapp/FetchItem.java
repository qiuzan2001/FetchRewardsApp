package com.example.fetchrewardsapp;

import com.google.gson.annotations.SerializedName;

public class FetchItem {
    @SerializedName("id")
    private int id;

    @SerializedName("listId")
    private int listId;

    @SerializedName("name")
    private String name;

    // Getters
    public int getId() {
        return id;
    }

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public void setName(String name) {
        this.name = name;
    }
}

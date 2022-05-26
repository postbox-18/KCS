package com.example.kcs.Fragment.Items.ItemSelectedList;

public class UserItemList {
    private final int list_size;
    private final String header;
    public UserItemList(String header, int list_size) {
        this.list_size=list_size;
        this.header=header;
    }



    public int getList_size() {
        return list_size;
    }

    public String getHeader() {
        return header;
    }
}

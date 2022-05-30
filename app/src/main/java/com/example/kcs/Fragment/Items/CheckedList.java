package com.example.kcs.Fragment.Items;

public class CheckedList {
    private final String item;
    private final int position;
    public CheckedList(String item, int position) {
        this.item=item;
        this.position=position;
    }

    public int getPosition() {
        return position;
    }

    public String getItemList() {
        return item;
    }
}

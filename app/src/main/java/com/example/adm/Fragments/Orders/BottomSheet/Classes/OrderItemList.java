package com.example.adm.Fragments.Orders.BottomSheet.Classes;

public class OrderItemList {
    private  String item,selected;
    public OrderItemList() {

    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getItem() {
        return item;
    }

    public String getSelected() {
        return selected;
    }
}
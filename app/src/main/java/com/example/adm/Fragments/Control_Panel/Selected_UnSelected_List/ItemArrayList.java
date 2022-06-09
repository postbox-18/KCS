package com.example.adm.Fragments.Control_Panel.Selected_UnSelected_List;

public class ItemArrayList {
    private final String item,selected;
    public ItemArrayList(String item, String selected) {
        this.item = item;
        this.selected = selected;
    }

    public String getItem() {
        return item;
    }

    public String getSelected() {
        return selected;
    }
}

package com.example.kcs.Fragment.Dish.DishSelectedList;

public class UserDishList {
    private final int list_size;
    private final String item;
    public UserDishList(String item, int list_size) {
        this.list_size=list_size;
        this.item=item;
    }



    public int getList_size() {
        return list_size;
    }

    public String getHeader() {
        return item;
    }
}

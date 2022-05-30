package com.example.adm.Fragments.Orders;

public class OrderLists {
    private final String s_user_name,  func;
    public OrderLists(String s_user_name, String func) {
        this.s_user_name = s_user_name;
        this.func = func;

    }

    public String getS_user_name() {
        return s_user_name;
    }

    public String getFunc() {
        return func;
    }


}

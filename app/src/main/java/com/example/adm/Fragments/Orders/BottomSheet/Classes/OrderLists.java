package com.example.adm.Fragments.Orders.BottomSheet.Classes;

public class OrderLists {
    private final String s_user_name,  func,phone_number;
    public OrderLists(String s_user_name, String func, String phone_number) {
        this.s_user_name = s_user_name;
        this.func = func;
        this.phone_number = phone_number;
    }

    public String getS_user_name() {
        return s_user_name;
    }

    public String getFunc() {
        return func;
    }

    public String getPhone_number() {
        return phone_number;
    }
}

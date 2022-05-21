package com.example.adm.Fragments.Orders;

public class OrderLists {
    private final String s_user_name,  func,  header,  list;
    private final int size;
    public OrderLists(String s_user_name, String func, String header, String list, int size) {
        this.s_user_name = s_user_name;
        this.func = func;
        this.header = header;
        this.size = size;
        this.list = list;
    }

    public int getSize() {
        return size;
    }

    public String getList() {
        return list;
    }

    public String getS_user_name() {
        return s_user_name;
    }

    public String getFunc() {
        return func;
    }

    public String getHeader() {
        return header;
    }
}

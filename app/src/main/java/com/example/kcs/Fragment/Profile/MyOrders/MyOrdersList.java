package com.example.kcs.Fragment.Profile.MyOrders;

public class MyOrdersList {
    private final String   func,  header,  list;
    private final int size;
    public MyOrdersList(String func, String header, String list, int size) {

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



    public String getFunc() {
        return func;
    }

    public String getHeader() {
        return header;
    }
}

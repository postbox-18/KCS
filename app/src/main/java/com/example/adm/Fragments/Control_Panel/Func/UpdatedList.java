package com.example.adm.Fragments.Control_Panel.Func;

public class UpdatedList {
    private  final String title,update_text;
    private final int position;
    public UpdatedList(String title, String update_text, int position) {
        this.title = title;
        this.update_text = update_text;
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public int getPosition() {
        return position;
    }

    public String getUpdate_text() {
        return update_text;
    }
}

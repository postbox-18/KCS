package com.example.kcs.Classes;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferences_data {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String s_user_name="s_user_name";
    private static final String s_phone_number="s_phone_number";
    private static final String s_password="s_password";
    private static final String s_email="s_email";
    public SharedPreferences_data(Context context) {
        sharedPreferences = context.getSharedPreferences("password", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }
    public static boolean logout_User() {
        if (editor != null) {
            editor.remove("");
            editor.apply();
            return true;
        } else {
            return false;
        }
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void setSharedPreferences(SharedPreferences sharedPreferences) {
        SharedPreferences_data.sharedPreferences = sharedPreferences;
    }

    public static SharedPreferences.Editor getEditor() {
        return editor;
    }

    public static void setEditor(SharedPreferences.Editor editor) {
        SharedPreferences_data.editor = editor;
    }
    public void setS_user_name(String user_name){
        sharedPreferences.edit().putString(s_user_name, user_name).commit();

    }
    public static String getS_user_name() {
        return sharedPreferences.getString(s_user_name, null);
    }
    public void setS_phone_number(String phone_number){
        sharedPreferences.edit().putString(s_phone_number, phone_number).commit();

    }
    public static String getS_phone_number() {
        return sharedPreferences.getString(s_phone_number, null);
    }
    public void setS_password(String password){
        sharedPreferences.edit().putString(s_password, password).commit();

    }
    public static String getS_password() {
        return sharedPreferences.getString(s_password, null);
    }

   public void setS_email(String email){
        sharedPreferences.edit().putString(s_email, email).commit();

    }
    public static String getS_email() {
        return sharedPreferences.getString(s_email, null);
    }


}

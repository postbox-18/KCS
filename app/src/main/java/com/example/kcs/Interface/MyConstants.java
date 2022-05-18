package com.example.kcs.Interface;

import com.example.kcs.Classes.BuildConfig;

import java.util.Locale;

public interface MyConstants {
    boolean IS_IN_DEBUG = true && BuildConfig.DEBUG;
    int soapTimeOut=1000*15;
    Locale GLOBAL_APP_LOCALE = Locale.getDefault();
}

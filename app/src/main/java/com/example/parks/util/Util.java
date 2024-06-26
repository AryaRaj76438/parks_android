package com.example.parks.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;

public class Util {
    public static final String PARKS_URL = "https://developer.nps.gov/api/v1/parks?stateCode=wa&api_key=Ds7mE7u7giFl4JOdnIqAoP1ZkVaQL55sJ8jwPcsL";

    public static String getParksUrl(String stateCode){
        return "https://developer.nps.gov/api/v1/parks?stateCode="+stateCode+"&api_key=Ds7mE7u7giFl4JOdnIqAoP1ZkVaQL55sJ8jwPcsL";
    }

    public static void hideofSoftKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
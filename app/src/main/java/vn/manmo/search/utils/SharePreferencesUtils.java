package vn.manmo.search.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by NguyenQuocCuong on 11/18/2017.
 */

public class SharePreferencesUtils {

    private static final String PREFS_NAME = "MANMO_SEARCH";
    private static final String PREFS_HOTEL_FURNITURE = "HOTEL_FURNITURE";

    public static boolean setFurniture(Context context, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFS_HOTEL_FURNITURE, value);
        return editor.commit();
    }

    public static String getFurniture(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(PREFS_HOTEL_FURNITURE, "");
    }
}

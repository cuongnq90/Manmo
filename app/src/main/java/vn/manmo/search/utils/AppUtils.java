package vn.manmo.search.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by NguyenQuocCuong on 11/15/2017.
 */

public class AppUtils {

    public static double mLatitude = 21.0278;
    public static double mLongitude = 105.8342;
    public static boolean mLocationUpdated = false;
    public static String mJsonHotelsAround = "";
    public static String mJsonFurnitures = "";

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isGPSOn(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}

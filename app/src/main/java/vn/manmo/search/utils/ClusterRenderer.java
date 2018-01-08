package vn.manmo.search.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import vn.manmo.search.object.MarkerItem;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by NguyenQuocCuong on 11/15/2017.
 */

public class ClusterRenderer extends DefaultClusterRenderer<MarkerItem> {

    public ClusterRenderer(Context context, GoogleMap map, ClusterManager<MarkerItem> clusterManager) {
        super(context, map, clusterManager);
    }


    @Override
    protected void onBeforeClusterItemRendered(MarkerItem markerItem, MarkerOptions markerOptions) {
        markerOptions.icon(markerItem.getIcon());
        markerOptions.snippet(markerItem.getSnippet());
        markerOptions.title(markerItem.getTitle());
        super.onBeforeClusterItemRendered(markerItem, markerOptions);
    }
}

package vn.manmo.search.utils.getApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by admin on 28/04/2017.
 */

public class getListLocalGps extends StringRequest {

    private Map<String, String> params;

    public getListLocalGps(String WEBURL, Response.Listener<String> listener) {
        super(Request.Method.POST,WEBURL, listener, null);
        params = new HashMap<>();
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 100, x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public getListLocalGps(String page, String WEBURL, Response.Listener<String> listener) {
        super(Request.Method.POST,WEBURL, listener, null);
        params = new HashMap<>();
        params.put("page", page);
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 100, x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public getListLocalGps(String latitude, String longitude,
            String radius, String WEBURL, Response.Listener<String> listener) {
        super(Request.Method.POST,WEBURL, listener, null);
        params = new HashMap<>();
        params.put("lat", latitude);
        params.put("long", longitude);
        params.put("radius", radius);
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 100, x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

package vn.manmo.search.utils.getApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by admin on 03/05/2017.
 */

public class GetFurnitureInfo extends StringRequest {

    private Map<String, String> params;

    public GetFurnitureInfo(String WEBURL, Response.Listener<String> listener) {
        super(Method.POST,WEBURL, listener, null);
        params = new HashMap<>();
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 100, x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}


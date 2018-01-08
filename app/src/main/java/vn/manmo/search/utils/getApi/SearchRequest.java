package vn.manmo.search.utils.getApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by admin on 10/05/2017.
 */

public class SearchRequest extends StringRequest {

    private Map<String, String> params;

    public SearchRequest(String page,
                         String nameHotel,
                         String price,
                         String city,
                         String district,
                         String hotelType,
                         String WEBURL,
                         String tienIch,
                         Response.Listener<String> listener) {
        super(Method.POST,WEBURL, listener, null);
        params = new HashMap<>();
        params.put("page", page);
        params.put("nameHotel", nameHotel);
        params.put("price", price);
        params.put("city", city);
        params.put("district", district);
        params.put("hotelType", hotelType);
        params.put("tienIch", tienIch);
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 100, x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}


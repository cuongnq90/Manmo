package vn.manmo.search.utils.getApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by admin on 07/05/2017.
 */

public class BookRoomRequest extends StringRequest {

    private Map<String, String> params;

    public BookRoomRequest(String WEBURL, Response.Listener<String> listener) {
        super(Method.GET, WEBURL, listener, null);
        params = new HashMap<>();
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 100, x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public BookRoomRequest(String date_start,
                           String date_end,
                           String typeRoom,
                           String name,
                           String phone,
                           String email,
                           String number_room,
                           String number_people,
                           String idHotel,
                           String nameHotel,
                           String emailHotel,
                           String idManager, String WEBURL, Response.Listener<String> listener) {
        super(Method.GET, WEBURL, listener, null);
        params = new HashMap<>();
        params.put("date_start", date_start);
        params.put("date_end", date_end);
        params.put("typeRoom", typeRoom);
        params.put("name", name);
        params.put("phone", phone);
        params.put("email", email);
        params.put("number_room", number_room);
        params.put("number_people", number_people);
        params.put("idHotel", idHotel);
        params.put("nameHotel", nameHotel);
        params.put("emailHotel", emailHotel);
        params.put("idManager", idManager);
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 100, x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}



package vn.manmo.search.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.manmo.search.HotelDetailActivity;
import vn.manmo.search.R;
import vn.manmo.search.adapter.ListSearchAdapter;
import vn.manmo.search.object.HotelAroundObject;
import vn.manmo.search.utils.AppUtils;
import vn.manmo.search.utils.DialogUtils;
import vn.manmo.search.utils.getApi.getListLocalGps;

/**
 * Created by NguyenQuocCuong on 11/13/2017.
 */

public class ListFragment extends Fragment {

    public static final String TAG = ListFragment.class.getSimpleName();
    public static final String TITLE = "Danh sách";
    private List<HotelAroundObject> mArrList = new ArrayList<HotelAroundObject>();
    private ProgressDialog shoPr;
    private RecyclerView mRecycleView;
    private String name, coordinates, address, phone, id, price, pricehour, point, image, statusRoom, best;
    private ListSearchAdapter mAdapter;
    private SearchView mSearchView;

    private static ListFragment mFragment = null;

    public static ListFragment getInstance() {
        if(mFragment == null) {
            synchronized (ListFragment.class) {
                if(mFragment == null) {
                    mFragment = new ListFragment();
                }
            }
        }
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecycleView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        readData();
        mAdapter = new ListSearchAdapter(getContext(), mArrList);
        mRecycleView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) itemSearch.getActionView();
        mSearchView.setOnQueryTextListener(mOnQueryTextListener);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String str) {
            mAdapter.filterList(str);
            return false;
        }
    };

    private void readData() {
        mArrList.clear();
        shoPr = DialogUtils.loadingDialog(getContext(),"Đang tải dữ liệu");
        shoPr.show();
        try {
            shoPr.dismiss();
            JSONObject jo = new JSONObject(AppUtils.mJsonHotelsAround);

            String code = jo.getString("code");
            if (code.equals("0")) {
                JSONArray listData = jo.getJSONArray("data");
                if (listData.length() > 0) {
                    for (int i = 0; i < listData.length(); i++) {
                        JSONObject o = listData.getJSONObject(i);
                        JSONObject request = o.getJSONObject("Hotel");
                        name = request.getString("name");
                        address = request.getString("address");
                        phone = request.getString("phone");
                        coordinates = request.getString("coordinates");
                        id = request.getString("id");
                        JSONArray jsonArray = request.getJSONArray("image");
                        image = "";
                        for (int j = 0; j < jsonArray.length(); j++) {
                            if(!image.equals("")) image += ",";
                            image += getResources().getString(R.string.home_site_link)+jsonArray.getString(j);
                        }
                        price = request.getString("price");
                        pricehour = request.getString("priceHour");
                        point = request.getString("point");
                        point = request.getString("point");
                        statusRoom = request.getString("statusRoom");
                        best = request.getString("best");
                        mArrList.add(new HotelAroundObject(name, address, phone, coordinates, id, price, pricehour, image, point, statusRoom, best));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

package vn.manmo.search.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.manmo.search.R;
import vn.manmo.search.adapter.ListSearchAdapter;
import vn.manmo.search.adapter.NewsAdapter;
import vn.manmo.search.object.HotelAroundObject;
import vn.manmo.search.object.NewsObject;
import vn.manmo.search.utils.AppUtils;
import vn.manmo.search.utils.DialogUtils;
import vn.manmo.search.utils.getApi.getListLocalGps;

/**
 * Created by NguyenQuocCuong on 11/13/2017.
 */

public class NewsFragment extends Fragment {

    public static final String TAG = NewsFragment.class.getSimpleName();
    public static final String TITLE = "Tin tức";
    private List<NewsObject> mArrList = new ArrayList<NewsObject>();
    private RecyclerView mRecycleView;
    private ProgressDialog shoPr;
	String title, image, author, introductory, time, id, url, date;
    private NewsAdapter mAdapter;

    private static NewsFragment mFragment = null;

    public static NewsFragment getInstance() {
        if(mFragment == null) {
            synchronized (NewsFragment.class) {
                if(mFragment == null) {
                    mFragment = new NewsFragment();
                }
            }
        }
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mRecycleView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        readData();
        mAdapter = new NewsAdapter(getContext(), mArrList);
        mRecycleView.setAdapter(mAdapter);
    }

    private void readData() {
        mArrList.clear();
        String page = 1 + "";
        String link = getContext().getResources().getString(R.string.getNoticeNewAPI);
        shoPr = DialogUtils.loadingDialog(getContext(),"Đang tải dữ liệu");
        shoPr.show();
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    shoPr.dismiss();
                    JSONArray listData = new JSONArray(response);
                    for (int i = 0; i < listData.length(); i++) {
                        JSONObject o = listData.getJSONObject(i);
                        JSONObject request = o.getJSONObject("Notice");
                        title = request.getString("title");
                        image = request.getString("image");
                        author = request.getString("author");
                        introductory = request.getString("introductory");
                        time = request.getString("time");
                        id = request.getString("id");
                        url = request.getString("url");
                        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
                        date = sdfDate.format(new Date(Long.parseLong(time) * 1000));
                        mArrList.add(new NewsObject(title, image, author, introductory, time, id, url));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        };
        getListLocalGps gcr = new getListLocalGps(AppUtils.mLatitude + "", AppUtils.mLongitude + "", 0.045 + "", link, response);
        RequestQueue qe = Volley.newRequestQueue(getActivity());
        qe.add(gcr);
    }
}

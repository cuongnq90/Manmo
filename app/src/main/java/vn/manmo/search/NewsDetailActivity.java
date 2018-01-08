package vn.manmo.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.manmo.search.object.HotelAroundObject;
import vn.manmo.search.object.NewsObject;
import vn.manmo.search.utils.AppUtils;
import vn.manmo.search.utils.DialogUtils;
import vn.manmo.search.utils.getApi.GetDetailHotelRequest;
import vn.manmo.search.utils.getApi.getListLocalGps;


public class NewsDetailActivity extends AppCompatActivity {

    private Context mContext;
    private WebView mWebView;
    private ProgressBar mLoading;
    private Toolbar mToolbar;
    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        mContext = this;
        mToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tin tức");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mWebView = findViewById(R.id.wv_news);
        mLoading = findViewById(R.id.progress_loading);
        NewsObject newsDetail = (NewsObject) getIntent().getSerializableExtra("news_detail");

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        int fontWebview = mWebView.getSettings().getTextZoom();
        mWebView.getSettings().setTextZoom((fontWebview*2));
        mWebView.loadUrl(newsDetail.getUrl());
    }

}

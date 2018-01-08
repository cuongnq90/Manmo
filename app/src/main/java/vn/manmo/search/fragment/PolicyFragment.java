package vn.manmo.search.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import vn.manmo.search.R;

/**
 * Created by NguyenQuocCuong on 11/13/2017.
 */

public class PolicyFragment extends Fragment {

    public static final String TAG = PolicyFragment.class.getSimpleName();
    public static final String TITLE = "Chính sách bảo mật";
    private WebView mWebView;
    private RelativeLayout mLoading;

    private static PolicyFragment mFragment = null;

    public static PolicyFragment getInstance() {
        if(mFragment == null) {
            synchronized (PolicyFragment.class) {
                if(mFragment == null) {
                    mFragment = new PolicyFragment();
                }
            }
        }
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_policy, container, false);
        mWebView = view.findViewById(R.id.webview);
        mLoading = view.findViewById(R.id.layout_loading);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.setWebViewClient(mWebviewClient);
        mWebView.loadUrl(getActivity().getResources().getString(R.string.link_policy));
    }

    private WebViewClient mWebviewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            mLoading.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
        }
    };
}

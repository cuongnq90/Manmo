package vn.manmo.search.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.manmo.search.R;

/**
 * Created by NguyenQuocCuong on 11/13/2017.
 */

public class InformationFragment extends Fragment {

    public static final String TAG = InformationFragment.class.getSimpleName();
    public static final String TITLE = "Thông tin";

    private static InformationFragment mFragment = null;

    public static InformationFragment getInstance() {
        if(mFragment == null) {
            synchronized (InformationFragment.class) {
                if(mFragment == null) {
                    mFragment = new InformationFragment();
                }
            }
        }
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }
}

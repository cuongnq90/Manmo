package vn.manmo.search.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vn.manmo.search.R;
import vn.manmo.search.adapter.FavoriteAdapter;

/**
 * Created by NguyenQuocCuong on 11/13/2017.
 */

public class FavoriteFragment extends Fragment {

    public static final String TAG = FavoriteFragment.class.getSimpleName();
    public static final String TITLE = "Yêu thích";
    private List<String> mList = new ArrayList<String>();
    private RecyclerView mRecycleView;

    private static FavoriteFragment mFragment = null;

    public static FavoriteFragment getInstance() {
        if(mFragment == null) {
            synchronized (FavoriteFragment.class) {
                if(mFragment == null) {
                    mFragment = new FavoriteFragment();
                }
            }
        }
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mRecycleView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initList();
        mRecycleView.setAdapter(new FavoriteAdapter(getContext(), mList));
    }

    private void initList(){
        for(int i = 0; i<10; i++) {
            mList.add("item: "+i);
        }
    }
}

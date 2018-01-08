package vn.manmo.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import vn.manmo.search.fragment.FavoriteFragment;
import vn.manmo.search.fragment.InformationFragment;
import vn.manmo.search.fragment.ListFragment;
import vn.manmo.search.fragment.MapFragment;
import vn.manmo.search.fragment.NewsFragment;
import vn.manmo.search.fragment.PolicyFragment;


public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private MenuItem mItemSearch;
    private SearchView mSearchView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNvDrawer;
    private Toolbar mToolbar;

    private Fragment mCurrentFragment = null;

    private MapFragment mMapFragment = null;
    private ListFragment mListFragment = null;
    private NewsFragment mNewsFragment = null;
    private FavoriteFragment mFavoriteFragment = null;
    private InformationFragment mInformationFragment = null;
    private PolicyFragment mPolicyFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        setupView();
    }

    private void setupView() {
        mToolbar = findViewById(R.id.map_bar);
        mToolbar.setTitle(getResources().getString(R.string.map));
        setSupportActionBar(mToolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNvDrawer = findViewById(R.id.navigation);
        mNvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
        mNvDrawer.getMenu().performIdentifierAction(R.id.map, 0);
    }



    private SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String str) {
            //implement map search
            return false;
        }
    };

    private void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass = null;
        String fTitle = "";
        boolean isShow = false;
        switch (menuItem.getItemId()){
            case R.id.map:
                if(mMapFragment == null) {
                    mMapFragment = MapFragment.getInstance();
                } else {
                    isShow = true;
                }
                fragment = mMapFragment;
                fragmentClass = MapFragment.class;
                fTitle = MapFragment.TITLE;
                break;
            case R.id.list:
                if(mListFragment == null) {
                    mListFragment = ListFragment.getInstance();
                } else {
                    isShow = true;
                }
                fragment = mListFragment;
                fragmentClass = ListFragment.class;
                fTitle = ListFragment.TITLE;
                break;
            case R.id.news:
                if(mNewsFragment == null) {
                    mNewsFragment = NewsFragment.getInstance();
                } else {
                    isShow = true;
                }
                fragment = mNewsFragment;
                fragmentClass = NewsFragment.class;
                fTitle = NewsFragment.TITLE;
                break;
            case R.id.favorite:
                if(mFavoriteFragment == null) {
                    mFavoriteFragment = FavoriteFragment.getInstance();
                } else {
                    isShow = true;
                }
                fragment = mFavoriteFragment;
                fragmentClass = FavoriteFragment.class;
                fTitle = FavoriteFragment.TITLE;
                break;
            case R.id.share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.home_site_link));
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Chia sẻ ứng dụng:"));
                break;
            case R.id.info:
                if(mInformationFragment == null) {
                    mInformationFragment = InformationFragment.getInstance();
                } else {
                    isShow = true;
                }
                fragment = mInformationFragment;
                fragmentClass = InformationFragment.class;
                fTitle = InformationFragment.TITLE;
                break;
            case R.id.policy:
                if(mPolicyFragment == null) {
                    mPolicyFragment = PolicyFragment.getInstance();
                } else {
                    isShow = true;
                }
                fragment = mPolicyFragment;
                fragmentClass = PolicyFragment.class;
                fTitle = PolicyFragment.TITLE;
                break;
        }
        if(fragmentClass != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if(mCurrentFragment != null) {
                fragmentTransaction.hide(mCurrentFragment);
            }
            if(isShow)
                fragmentTransaction.show(fragment);
            else
                fragmentTransaction.add(R.id.flContent, fragment);
            fragmentTransaction.commit();
            mCurrentFragment = fragment;
            mNvDrawer.getMenu().findItem(R.id.map).setChecked(false);
            menuItem.setChecked(true);
            mToolbar.setTitle(fTitle);
        }
        mDrawerLayout.closeDrawers();
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Ấn BACK lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}

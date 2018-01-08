package vn.manmo.search.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import vn.manmo.search.HotelDetailActivity;
import vn.manmo.search.R;
import vn.manmo.search.object.HotelAroundObject;
import vn.manmo.search.object.MarkerItem;
import vn.manmo.search.utils.AppUtils;
import vn.manmo.search.utils.ClusterRenderer;
import vn.manmo.search.utils.DialogUtils;
import vn.manmo.search.utils.getApi.GetFurnitureInfo;
import vn.manmo.search.view.WrapSliding;
import vn.manmo.search.utils.getApi.getListLocalGps;

/**
 * Created by NguyenQuocCuong on 11/13/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, LocationListener {

    public static final String TAG = MapFragment.class.getSimpleName();
    public static final String TITLE = "Bản đồ";
    private Context mContext;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private WrapSliding wrap;
    private ProgressDialog shoPr;
    private String name, coordinates, address, phone, id, price, pricehour, point, image, statusRoom, best;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6666;
    private static final int DEFAULT_ZOOM = 12;
    private LatLng mDefaultLocation;

    private ImageView mImghandle;
    private TextView mTvHandle;
    private FlexboxLayout mFlexFurniture;
    private TextView mTvRadius;
    private SeekBar mSeekBarRadius;
    private TextView mTvPrice;
    private SeekBar mSeekBarPrice;
    private TextView mBtnFilter;

    private float mRadius = 9000;
    private double mDistance = 100000000; //10000
    private int mPrice = 1000000000; //100000
    private String[] mArrFurniture;
    private int[] mFurniture;
    private ClusterManager<MarkerItem> mClusterManager;
    private SearchView mSearchView;

    ArrayList<HotelAroundObject> mArrList = new ArrayList<>();
    ArrayList<HotelAroundObject> mArrListBackup = new ArrayList<>();

    private static MapFragment mFragment = null;

    public MapFragment() {
    }

    public static MapFragment getInstance() {
        if (mFragment == null) {
            synchronized (MapFragment.class) {
                if (mFragment == null) {
                    mFragment = new MapFragment();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        wrap = view.findViewById(R.id.slide);
        mImghandle = view.findViewById(R.id.imgflit);
        mTvHandle = view.findViewById(R.id.textflit);
        mFlexFurniture = view.findViewById(R.id.filter_furniture);
        mTvRadius = view.findViewById(R.id.tv_radius);
        mTvPrice = view.findViewById(R.id.tv_price);
        mSeekBarRadius = view.findViewById(R.id.seekbar_radius);
        mSeekBarPrice = view.findViewById(R.id.seekbar_price);
        mBtnFilter = view.findViewById(R.id.btnFilter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);

        FragmentManager fm = getChildFragmentManager();
        mDefaultLocation = new LatLng(AppUtils.mLatitude, AppUtils.mLongitude);
        getLocationPermission();
        mMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        setupFilter();
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
            filterList(str);
            return false;
        }
    };

    private void filterList(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mArrList.clear();
        if (charText.length() == 0) {
            mArrList.addAll(mArrListBackup);
        } else {
            for (HotelAroundObject wp : mArrListBackup) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getPhone().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getAddress().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mArrList.add(wp);
                }
            }
        }
        showMarker();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateLocationUI();
        getDeviceLocation();
        mClusterManager = new ClusterManager<MarkerItem>(mContext, mMap);
        mClusterManager.setRenderer(new ClusterRenderer(mContext, mMap, mClusterManager));
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

//        mClusterManager
//                .setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerItem>() {
//                    @Override
//                    public boolean onClusterItemClick(MarkerItem item) {
//                        mClickedClusterItem = item;
//                        return false;
//                    }
//                });

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View myContentView = getLayoutInflater().inflate(R.layout.view_marker_info, null);
                TextView tvTitle = myContentView.findViewById(R.id.tv_title);
                TextView tvPhone = myContentView.findViewById(R.id.tv_phone);
                TextView tvAddress = myContentView.findViewById(R.id.tv_address);
                TextView tvDetail = myContentView.findViewById(R.id.btn_view_detail);
                ImageView imgPrice = myContentView.findViewById(R.id.img_marker_money);
                ImageView imgPhone = myContentView.findViewById(R.id.img_marker_phone);
                ImageView imgAddress = myContentView.findViewById(R.id.img_marker_address);
                tvTitle.setText(marker.getTitle());
                TextView tvPrice = myContentView.findViewById(R.id.tv_price);
                int snippet = Integer.parseInt(marker.getSnippet());
                if(Integer.parseInt(mArrList.get(snippet).getBest()) == 1) {
                    tvDetail.setBackground(mContext.getResources().getDrawable(R.drawable.marker_button_background_yellow));
                    imgPrice.setBackground(mContext.getResources().getDrawable(R.drawable.icon_money_yellow));
                    imgPhone.setBackground(mContext.getResources().getDrawable(R.drawable.icon_phone_yellow));
                    imgAddress.setBackground(mContext.getResources().getDrawable(R.drawable.icon_address_yellow));
                } else {
                    if(Integer.parseInt(mArrList.get(snippet).getStatusRoom()) == 0) {
                        tvDetail.setBackground(mContext.getResources().getDrawable(R.drawable.marker_button_background_red));
                        imgPrice.setBackground(mContext.getResources().getDrawable(R.drawable.icon_money_red));
                        imgPhone.setBackground(mContext.getResources().getDrawable(R.drawable.icon_phone_red));
                        imgAddress.setBackground(mContext.getResources().getDrawable(R.drawable.icon_address_red));
                    }
                }
                DecimalFormat formatter = new DecimalFormat("#,###.00");
                Double pr = Double.parseDouble(mArrList.get(snippet).getPricehour());
                if(pr == 0) {
                    pr = Double.parseDouble(mArrList.get(snippet).getPrice());
                }
                String price = formatter.format(pr);
                tvPrice.setText("Giá: " + price + " VNĐ");
                tvPhone.setText(mArrList.get(snippet).getPhone());
                tvAddress.setText(mArrList.get(snippet).getAddress());
                return myContentView;
            }
        });
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                if(AppUtils.mLocationUpdated) {
                    readData(AppUtils.mLatitude, AppUtils.mLongitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(AppUtils.mLatitude, AppUtils.mLongitude), DEFAULT_ZOOM));
                } else {
                    Task locationResult = mFusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener((Activity) mContext, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLastKnownLocation = (Location) task.getResult();
                                mDefaultLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                readData(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s", task.getException());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });
                }
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private int progressToDistance(int progress) {
        if(progress <= 5) {
            return progress*2;
        }
        if(progress > 5 && progress <= 7) {
            return 5*(progress-5)+10;
        }
        if (progress == 8) {
            return 50;
        }
        if(progress == 9) {
            return 100;
        }
        return 100000;
    }

    private void setupFilter() {
        loadFurniture();
        mTvRadius.setText("Không giới hạn");//mTvRadius.setText(((int)(mDistance/1000))+" km");
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String price = formatter.format(mPrice);
        mTvPrice.setText("Không giới hạn");

//        mSeekBarRadius.setProgress((int)(mDistance/2000));//((int)((mRadius*100)/9));
//        mSeekBarPrice.setProgress(mPrice/10000);
        wrap.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                mImghandle.setImageDrawable(getResources().getDrawable(R.drawable.downb));
                mTvHandle.setText("Kéo xuống để đóng bộ lọc");
            }
        });
        wrap.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                mImghandle.setImageDrawable(getResources().getDrawable(R.drawable.upb));
                mTvHandle.setText("Kéo lên để mở bộ lọc");
            }
        });
        mSeekBarRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int textDistance = progressToDistance(seekBar.getProgress());
                if(textDistance>100) {
                    mTvRadius.setText("Không giới hạn");
                } else {
                    mTvRadius.setText(textDistance + " km");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int textDistance = progressToDistance(seekBar.getProgress());
                if(textDistance>100) {
                    mTvRadius.setText("Không giới hạn");
                } else {
                    mTvRadius.setText(textDistance + " km");
                }
                mDistance = textDistance*1000;
//                mRadius = (see_value*9)/100;
            }
        });
        mSeekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(seekBar.getProgress() == 51) {
                    mTvPrice.setText("Không giới hạn");
                } else {
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    String price = formatter.format(seekBar.getProgress() * 10000);
                    mTvPrice.setText(price + " VNĐ");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int see_value = seekBar.getProgress();
                if(see_value == 51) {
                    mTvPrice.setText("Không giới hạn");
                    mPrice = 1000000000;
                } else {
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    String price = formatter.format(see_value * 10000);
                    mTvPrice.setText(price + " VNĐ");
                    mPrice = see_value * 10000;
                }
            }
        });
        mBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wrap.close();
                String furniture = "";
                for(int i = 0; i<mFurniture.length; i++) {
                    if(mFurniture[i] == 1) {
                        if(!furniture.equals("")) furniture += ",";
                        furniture += i;
                    }
                }
                mArrFurniture = furniture.split(",");
                showMarker();
            }
        });
    }

    private void loadFurniture() {
        String link = getResources().getString(R.string.getFurnitureAPI);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppUtils.mJsonFurnitures = response;
                try {
                    JSONObject jo = new JSONObject(response);
                    mFurniture = new int[jo.length()];
                    for (int j = 1; j < jo.length(); j++) {
                        JSONObject ot = jo.getJSONObject(String.valueOf(j));
                        final String id = ot.getString("id");
                        String name = ot.getString("name");
                        mFurniture[Integer.parseInt(id)] = 0;
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        param.gravity = Gravity.CENTER;
                        param.rightMargin = 20;
                        param.topMargin = 20;
                        final TextView tag = new TextView(mContext);
                        tag.setText(name);
                        tag.setPadding(15, 15, 15, 15);
                        tag.setBackground(mContext.getResources().getDrawable(R.drawable.button_background));
                        tag.setLayoutParams(param);
                        tag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clickFurniture(tag, Integer.parseInt(id));
                            }
                        });
                        mFlexFurniture.addView(tag);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        GetFurnitureInfo gcr = new GetFurnitureInfo(link, response);
        RequestQueue qe = Volley.newRequestQueue(mContext);
        qe.add(gcr);
    }

    private void clickFurniture(TextView tv, int fid) {
        if(mFurniture[fid] == 0) {
            mFurniture[fid] = 1;
            tv.setBackground(mContext.getResources().getDrawable(R.drawable.booking_button_background));
        } else {
            mFurniture[fid] = 0;
            tv.setBackground(mContext.getResources().getDrawable(R.drawable.button_background));
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void readData(final double lat, final double lon) {
        mArrList.clear();
        String link = getContext().getResources().getString(R.string.getHotelAroundAPI);
        shoPr = DialogUtils.loadingDialog(getContext(),"Đang tải dữ liệu");
        shoPr.show();
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppUtils.mJsonHotelsAround = response;
                try {
                    shoPr.dismiss();
                    JSONObject jo = new JSONObject(response);

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
                                image = "";
                                JSONArray jsonArray = request.getJSONArray("image");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    if(!image.equals("")) image += ",";
                                    image += getResources().getString(R.string.home_site_link)+jsonArray.getString(j);
                                }
                                price = request.getString("price");
                                pricehour = request.getString("priceHour");
                                point = request.getString("point");
                                statusRoom = request.getString("statusRoom");
                                best = request.getString("best");
                                mArrList.add(new HotelAroundObject(name, address, phone, coordinates, id, price, pricehour, image, point, statusRoom, best));
                            }
                        }
                    }
                    if(mArrList != null) {
                        mArrListBackup.addAll(mArrList);
                    }
                    showMarker();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        getListLocalGps gcr = new getListLocalGps(lat + "", lon + "", mRadius + "", link, response);
        RequestQueue qe = Volley.newRequestQueue(getActivity());
        qe.add(gcr);
    }

    private void showMarker() {
        mMap.clear();
        mClusterManager.clearItems();
        Location crtLocation = new Location("");
        crtLocation.setLatitude(mDefaultLocation.latitude);
        crtLocation.setLongitude(mDefaultLocation.longitude);
        for (int i = 0; i < mArrList.size(); i++) {
            Location desLocation = new Location("");
            String[] separated = mArrList.get(i).getCoordinates().toString().split(",");
            if(separated.length < 2 || separated.length > 2) {
            } else {
                desLocation.setLatitude(Double.parseDouble(separated[0].toString()));
                desLocation.setLongitude(Double.parseDouble(separated[1].toString()));
                float distanceInMeters = crtLocation.distanceTo(desLocation);
                int pr = Integer.parseInt(mArrList.get(i).getPricehour());
                if(pr == 0) {
                    pr = Integer.parseInt(mArrList.get(i).getPrice());
                }
                if(pr <= mPrice && distanceInMeters <= mDistance) {
                    double lat = Double.parseDouble(separated[0].toString());
                    double lng = Double.parseDouble(separated[1].toString());
                    BitmapDescriptor iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_green);
                    if(Integer.parseInt(mArrList.get(i).getBest()) == 1) {
                        iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_yellow);
                    } else {
                        if(Integer.parseInt(mArrList.get(i).getStatusRoom()) == 0) {
                            iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_red);
                        }
                    }
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .title(mArrList.get(i).getName().toString())
                            .snippet(i+"")
                            .icon(iconMarker);
                    MarkerItem offsetItem = new MarkerItem(markerOptions);//lat, lng, mArrList.get(i).getName().toString(), i+"", iconMarker);
                    mClusterManager.addItem(offsetItem);
                }
            }
        }
        mClusterManager.cluster();
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<MarkerItem>() {
            @Override
            public void onClusterItemInfoWindowClick(MarkerItem marker) {
                String snippet = marker.getSnippet();
                Intent intent = new Intent(mContext, HotelDetailActivity.class);
                intent.putExtra("hotel_detail", mArrList.get(Integer.parseInt(snippet)));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged "+location.getLatitude()+"-"+location.getLongitude());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.i(TAG, "onStatusChanged ");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.i(TAG, "onProviderEnabled ");
    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

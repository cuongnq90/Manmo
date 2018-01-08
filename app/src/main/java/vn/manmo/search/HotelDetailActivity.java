package vn.manmo.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Arrays;

import vn.manmo.search.object.HotelAroundObject;
import vn.manmo.search.utils.getApi.GetDetailHotelRequest;
import vn.manmo.search.utils.getApi.GetFurnitureInfo;


public class HotelDetailActivity extends AppCompatActivity {

    private Context mContext;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TextView mHotelName, mPrice, mBtnBook, mAddress, mPhone, mEmail, mWebsite;
    private ImageView mImgAddress, mImgPhone, mImgEmail, mImgWebsite;
    private ImageView mImageHotel;
    private ProgressBar mLoading;
    private HotelAroundObject mHotelDetail;
    private FlexboxLayout mFurniture;
    private TextView mTvFurniture;
    private String mIdManager;
    private CarouselView mCarouselView;
    private String[] mImgArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        mContext = this;
        mHotelDetail = (HotelAroundObject) getIntent().getSerializableExtra("hotel_detail");

        setupView();
        readData();
    }

    private void setupView() {
        mImgArray = mHotelDetail.getImage().toString().split(",");
        mAppBarLayout = findViewById(R.id.app_bar_layout);
        mToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mHotelName = findViewById(R.id.tv_name);
        mPrice = findViewById(R.id.tv_price);
        mBtnBook = findViewById(R.id.btn_booking);
        mAddress = findViewById(R.id.tv_address);
        mPhone = findViewById(R.id.tv_phone);
        mEmail = findViewById(R.id.tv_email);
        mWebsite = findViewById(R.id.tv_website);
        mImageHotel = findViewById(R.id.img_hotel);
        mLoading = findViewById(R.id.img_loading);
        mFurniture = findViewById(R.id.flex_furniture);
        mTvFurniture = findViewById(R.id.tv_furniture);
        mHotelName.setText(mHotelDetail.getName());
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        Double pr = Double.parseDouble(mHotelDetail.getPricehour());
        if(pr == 0) {
            pr = Double.parseDouble(mHotelDetail.getPrice());
        }
        String price = formatter.format(pr);
        mPrice.setText(price + " VNĐ");
        mCarouselView = findViewById(R.id.carouselImage);
        mImgAddress = findViewById(R.id.ic_address);
        mImgPhone = findViewById(R.id.ic_phone);
        mImgEmail = findViewById(R.id.ic_email);
        mImgWebsite = findViewById(R.id.ic_website);
        if(Integer.parseInt(mHotelDetail.getBest()) == 1) {
            mImgAddress.setBackground(mContext.getResources().getDrawable(R.drawable.icon_address_yellow));
            mImgPhone.setBackground(mContext.getResources().getDrawable(R.drawable.icon_phone_yellow));
            mImgEmail.setBackground(mContext.getResources().getDrawable(R.drawable.icon_email_yellow));
            mImgWebsite.setBackground(mContext.getResources().getDrawable(R.drawable.icon_website_yellow));
        } else {
            if(Integer.parseInt(mHotelDetail.getStatusRoom()) == 0) {
                mImgAddress.setBackground(mContext.getResources().getDrawable(R.drawable.icon_address_red));
                mImgPhone.setBackground(mContext.getResources().getDrawable(R.drawable.icon_phone_red));
                mImgEmail.setBackground(mContext.getResources().getDrawable(R.drawable.icon_email_red));
                mImgWebsite.setBackground(mContext.getResources().getDrawable(R.drawable.icon_website_red));
            }
        }

        mCarouselView.setPageCount(mImgArray.length);
        mCarouselView.setImageListener(mImageListener);

        mBtnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BookingActivity.class);
                intent.putExtra("hotel_id", mHotelDetail.getId());
                intent.putExtra("hotel_name", mHotelDetail.getName());
                intent.putExtra("hotel_email", mEmail.getText());
                intent.putExtra("id_manager", mIdManager);
                startActivity(intent);
            }
        });
    }

    ImageListener mImageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(mContext)
                    .load(mImgArray[position].toString())
                    .into(imageView);
        }
    };

    private void readData() {
        String link = getResources().getString(R.string.getInfoHotelAPI);
        // doc du lieu ve
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("CUONG", response);
                    JSONObject jo = new JSONObject(response);
                    JSONObject a1 = jo.getJSONObject("hotel");
                    JSONObject hotel_detail = a1.getJSONObject("Hotel");

                    mHotelName.setText(hotel_detail.getString("name"));
//                    mPrice.setText(hotel_detail.getString("price")+" VNĐ");
                    mAddress.setText(hotel_detail.getString("address"));
                    mPhone.setText(hotel_detail.getString("phone"));
                    mEmail.setText(hotel_detail.getString("email"));
                    mWebsite.setText(hotel_detail.getString("website"));
                    mIdManager = hotel_detail.getString("idManager");
                    JSONArray furniture = hotel_detail.getJSONArray("furniture");
                    String[] arrFurniture = new String[furniture.length()];
                    for (int i = 0; i < furniture.length(); i++) {
                        arrFurniture[i] = furniture.getString(i);
                    }
                    getFurniture(arrFurniture);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        GetDetailHotelRequest gcr = new GetDetailHotelRequest(mHotelDetail.getId(), link, response);
        RequestQueue qe = Volley.newRequestQueue(this);
        qe.add(gcr);
    }

    private void getFurniture(final String[] arrFurniture) {
        String link = getResources().getString(R.string.getFurnitureAPI);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    for (int j = 1; j < jo.length(); j++) {
                        JSONObject ot = jo.getJSONObject(String.valueOf(j));
                        String id = ot.getString("id");
                        String name = ot.getString("name");
                        if(Arrays.asList(arrFurniture).contains(id)) {
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            param.gravity = Gravity.CENTER;
                            param.rightMargin = 20;
                            param.topMargin = 20;
                            TextView tag = new TextView(mContext);
                            tag.setText(name);
                            tag.setPadding(15, 15, 15, 15);
                            tag.setBackground(mContext.getResources().getDrawable(R.drawable.button_background));
                            tag.setLayoutParams(param);
                            mFurniture.addView(tag);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        GetFurnitureInfo gcr = new GetFurnitureInfo(link, response);
        RequestQueue qe = Volley.newRequestQueue(this);
        qe.add(gcr);
    }

}

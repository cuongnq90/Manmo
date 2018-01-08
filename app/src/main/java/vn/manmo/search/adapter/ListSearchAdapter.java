package vn.manmo.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.manmo.search.HotelDetailActivity;
import vn.manmo.search.R;
import vn.manmo.search.object.HotelAroundObject;

/**
 * Created by sev_user on 11/14/2017.
 */

public class ListSearchAdapter extends RecyclerView.Adapter<ListSearchAdapter.MyViewHolder> {

    private List<HotelAroundObject> mData;
    private List<HotelAroundObject> mDataCopy;
    private Context mContext;

    public ListSearchAdapter(Context context, List list) {
        this.mData = list;
        this.mContext = context;
        mDataCopy = new ArrayList<HotelAroundObject>();
        mDataCopy.addAll(mData);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String[] mImgArray = mData.get(position).getImage().toString().split(",");
        Glide.with(mContext)
                .load(mImgArray[0])
                .into(holder.mLogo);
        holder.mTitle.setText(mData.get(position).getName());
        holder.mTvPhone.setText(mData.get(position).getPhone());
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        Double pr = Double.parseDouble(mData.get(position).getPricehour());
        if(pr == 0) {
            pr = Double.parseDouble(mData.get(position).getPrice());
        }
        String price = formatter.format(pr);
        holder.mTvPrice.setText(price + " VNĐ");
        holder.mTvAddress.setText(mData.get(position).getAddress());
        holder.mRatingBar.setRating(Float.parseFloat(mData.get(position).getPoint()));
        if(Integer.parseInt(mData.get(position).getBest()) == 1) {
            holder.mImgPrice.setBackground(mContext.getResources().getDrawable(R.drawable.icon_money_yellow));
            holder.mImgPhone.setBackground(mContext.getResources().getDrawable(R.drawable.icon_phone_yellow));
            holder.mImgAddress.setBackground(mContext.getResources().getDrawable(R.drawable.icon_address_yellow));
        } else {
            if(Integer.parseInt(mData.get(position).getStatusRoom()) == 0) {
                holder.mImgPrice.setBackground(mContext.getResources().getDrawable(R.drawable.icon_money_red));
                holder.mImgPhone.setBackground(mContext.getResources().getDrawable(R.drawable.icon_phone_red));
                holder.mImgAddress.setBackground(mContext.getResources().getDrawable(R.drawable.icon_address_red));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LinearLayout mLayoutItem;
        private ImageView mLogo;
        private TextView mTitle;
        private TextView mTvPhone, mTvPrice, mTvAddress;
        private ImageView mImgPhone, mImgPrice, mImgAddress;
        private RatingBar mRatingBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLayoutItem = itemView.findViewById(R.id.layout_item);
            mLogo = itemView.findViewById(R.id.img_logo);
            mTitle = itemView.findViewById(R.id.tv_title);
            mTvPhone = itemView.findViewById(R.id.tv_phone);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mImgPhone = itemView.findViewById(R.id.img_item_phone);
            mImgPrice = itemView.findViewById(R.id.img_item_money);
            mImgAddress = itemView.findViewById(R.id.img_item_address);
            mRatingBar = itemView.findViewById(R.id.rate_view);
            mLayoutItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();
            Intent intent = new Intent(mContext, HotelDetailActivity.class);
            intent.putExtra("hotel_detail", mData.get(pos));
            mContext.startActivity(intent);
        }
    }

    public void filterList(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mData.clear();
        if (charText.length() == 0) {
            mData.addAll(mDataCopy);
        } else {
            for (HotelAroundObject wp : mDataCopy) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getPhone().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getAddress().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mData.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}

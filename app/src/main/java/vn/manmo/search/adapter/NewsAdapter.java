package vn.manmo.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import vn.manmo.search.HotelDetailActivity;
import vn.manmo.search.NewsDetailActivity;
import vn.manmo.search.R;
import vn.manmo.search.object.NewsObject;

/**
 * Created by sev_user on 11/14/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<NewsObject> mData;
    private Context mContext;

    public NewsAdapter(Context context, List list) {
        this.mData = list;
        this.mContext = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.news_item, parent, false);
        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String url = mContext.getResources().getString(R.string.home_site_link) +
                mData.get(position).getImage();
        Glide.with(mContext)
                .load(url)
                .into(holder.mLogo);
        holder.mTitle.setText(mData.get(position).getTitle());
        holder.mDescription.setText(mData.get(position).getIntroductory());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LinearLayout mLayoutItem;
        private ImageView mLogo;
        private TextView mTitle;
        private TextView mDescription;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLayoutItem = itemView.findViewById(R.id.layout_item);
            mLogo = itemView.findViewById(R.id.img_logo);
            mTitle = itemView.findViewById(R.id.tv_title);
            mDescription = itemView.findViewById(R.id.tv_description);
            mLayoutItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();
            Intent intent = new Intent(mContext, NewsDetailActivity.class);
            intent.putExtra("news_detail", mData.get(pos));
            mContext.startActivity(intent);
        }
    }
}

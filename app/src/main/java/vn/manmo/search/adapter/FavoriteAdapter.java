package vn.manmo.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.manmo.search.R;

/**
 * Created by sev_user on 11/14/2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    private List<String> mData;
    private Context mContext;

    public FavoriteAdapter(Context context, List list) {
        this.mData = list;
        this.mContext = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.favorite_item, parent, false);
        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTitle.setText("Favorite "+ position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LinearLayout mLayoutItem;
        private TextView mLogo;
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
            Toast.makeText(mContext, "Clicked in position "+pos, Toast.LENGTH_LONG).show();
        }
    }
}

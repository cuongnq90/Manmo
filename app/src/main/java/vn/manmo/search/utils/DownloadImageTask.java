package vn.manmo.search.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by NguyenQuocCuong on 11/15/2017.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView mImage;
    private Context mContext;

    public DownloadImageTask(ImageView mImage) {
        this.mImage = mImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        mImage.setImageBitmap(result);
    }
}

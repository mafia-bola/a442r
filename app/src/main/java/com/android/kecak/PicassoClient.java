package com.android.kecak;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoClient {

    public static void downloadImage(Context c, String urlAddress, ImageView imageList) {
        if (urlAddress != null && urlAddress.length() > 0) {
            Picasso
                    .with(c)
                    .load(urlAddress)
                    .placeholder(R.drawable.image_not_found)
                    .fit()
                    .into(imageList);
        } else {
            Picasso.with(c).load(R.drawable.image_not_found).into(imageList);
        }
    }
}

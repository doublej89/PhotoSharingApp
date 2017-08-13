package com.example.memyself.myphotosharingapp;

import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by MeMyself on 7/29/2017.
 */

public class ImageLoader {
    private RequestManager glideRequestManager;
    private RequestOptions options;

    public void setLoaderContext(Fragment fragment, RequestOptions options) {
        this.glideRequestManager = Glide.with(fragment);
        this.options = options;
    }

    public void load(ImageView imageView, String URL) {
        options.diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .override(800, 800);
        glideRequestManager.load(URL)
                .apply(options)
                .into(imageView);
    }
}

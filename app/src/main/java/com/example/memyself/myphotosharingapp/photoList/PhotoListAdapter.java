package com.example.memyself.myphotosharingapp.photoList;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.memyself.myphotosharingapp.ImageLoader;
import com.example.memyself.myphotosharingapp.R;
import com.example.memyself.myphotosharingapp.photo.Photo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by MeMyself on 7/31/2017.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    private List<Photo> photoList;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;
    private Geocoder geocoder;

    public PhotoListAdapter(List<Photo> photoList, ImageLoader imageLoader, OnItemClickListener onItemClickListener, Geocoder geocoder) {
        this.photoList = photoList;
        this.imageLoader = imageLoader;
        this.onItemClickListener = onItemClickListener;
        this.geocoder = geocoder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo currentPhoto = photoList.get(position);
        holder.setOnItemClickListener(currentPhoto, onItemClickListener);
        imageLoader.load(holder.imgAvatar, !TextUtils.isEmpty(currentPhoto.getEmail())? getAvatarUrl(currentPhoto.getEmail()) : "");
        imageLoader.load(holder.imgMain, currentPhoto.getUrl());
        holder.txtUser.setText(currentPhoto.getEmail());
        double lat = currentPhoto.getLatitutde();
        double lon = currentPhoto.getLongitude();
        if (lat != 0.0 && lon != 0.0) {
            holder.txtPlace.setText(getFromLocation(lat, lon));
            holder.txtPlace.setVisibility(View.VISIBLE);
        } else {
            holder.txtPlace.setVisibility(View.GONE);
        }
        if (currentPhoto.isPublishedByMe()){
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void addPhoto(Photo photo) {
        photoList.add(0, photo);
        notifyDataSetChanged();
    }

    public void removePhoto(Photo photo) {
        photoList.remove(photo);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgAvatar)
        CircleImageView imgAvatar;
        @BindView(R.id.txtUser)
        TextView txtUser;
        @BindView(R.id.imgMain)
        ImageView imgMain;
        @BindView(R.id.txtPlace)
        TextView txtPlace;
        @BindView(R.id.imgShare)
        ImageButton btnShare;
        @BindView(R.id.imgDelete)
        ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setOnItemClickListener(final Photo photo,
                                           final OnItemClickListener listener) {
            txtPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPlaceClick(photo);
                }
            });
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onShareClick(photo, imgMain);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDeleteClick(photo);
                }
            });
        }
    }

    public String getAvatarUrl(String username) {
        return "http://www.gravatar.com/avatar/" + md5(username) + "?s=64";
    }


    private String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getFromLocation(double lat, double lng) {
        String result = "";
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            Address address = addresses.get(0);

            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                result += address.getAddressLine(i) + ", ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

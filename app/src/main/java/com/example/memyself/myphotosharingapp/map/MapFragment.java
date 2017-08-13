package com.example.memyself.myphotosharingapp.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.memyself.myphotosharingapp.ImageLoader;
import com.example.memyself.myphotosharingapp.PhotoSharingApp;
import com.example.memyself.myphotosharingapp.R;
import com.example.memyself.myphotosharingapp.photo.Photo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements MapView, OnMapReadyCallback, GoogleMap.InfoWindowAdapter{
    private FrameLayout layout;
    @Inject
    ImageLoader loader;
    @Inject
    MapPresenter presenter;

    private GoogleMap map;
    private HashMap<Marker, Photo> markers;
    private List<Photo> photoList;
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoSharingApp app = (PhotoSharingApp) getActivity().getApplication();
        app.getMapComponent(this, this).inject(this);


        markers = new HashMap<>();
        photoList = new ArrayList<>();

        presenter.onCreate();
        presenter.subscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        layout = (FrameLayout) view.findViewById(R.id.container);
        return view;
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setInfoWindowAdapter(this);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        } else {
            map.setMyLocationEnabled(true);
            LatLng coords = null;
            for (Photo photo : photoList) {
                coords = new LatLng(photo.getLatitutde(), photo.getLongitude());
                Marker marker = map.addMarker(new MarkerOptions().position(coords));
                markers.put(marker, photo);
            }
            if (coords != null) map.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 6));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (map != null) {
                        map.setMyLocationEnabled(true);
                    }
                }
                return;
        }
    }

    @Override
    public void addPhoto(Photo photo) {
        photoList.add(photo);
    }

    @Override
    public void removePhoto(Photo photo) {
        for (Map.Entry<Marker, Photo> entry : markers.entrySet()) {
            Photo currentPhoto = entry.getValue();
            Marker currentMarker = entry.getKey();
            if (currentPhoto.getId().equals(photo.getId())) {
                currentMarker.remove();
                markers.remove(currentMarker);
                break;
            }
        }
    }

    @Override
    public void onPhotosError(String error) {
        Snackbar.make(layout, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View window = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.info_window, null);
        Photo photo = markers.get(marker);
        CircleImageView imgAvatar = (CircleImageView) window.findViewById(R.id.imgAvatar);
        TextView txtUser = (TextView) window.findViewById(R.id.txtUser);
        final ImageView imgMain = (ImageView) window.findViewById(R.id.imgMain);
        String userEmail = !TextUtils.isEmpty(photo.getEmail()) ?  photo.getEmail() : "";
        loader.load(imgAvatar, getAvatarUrl(userEmail));
        loader.load(imgMain, photo.getUrl());
        txtUser.setText(photo.getEmail());

        return window;
    }

    public String getAvatarUrl(String username) {
        return "http://www.gravatar.com/avatar/" + md5(username) + "?s=64";
    }


    //A hash function to hide the email address in the avatar url
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
}

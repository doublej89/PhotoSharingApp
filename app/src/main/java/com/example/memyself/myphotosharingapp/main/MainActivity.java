package com.example.memyself.myphotosharingapp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import java.text.SimpleDateFormat;
import android.location.Location;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.Manifest;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.memyself.myphotosharingapp.PhotoSharingApp;
import com.example.memyself.myphotosharingapp.R;
import com.example.memyself.myphotosharingapp.login.LoginActivity;
import com.example.memyself.myphotosharingapp.map.MapFragment;
import com.example.memyself.myphotosharingapp.photoList.PhotoListFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements MainView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Inject
    MainPresenter presenter;
    @Inject
    MainPagerAdapter pagerAdapter;

    @Inject
    SharedPreferences sharedPreferences;
    private String photoPath;
    private Location lastLocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_PICTURE = 0;
    private final static int PERMISSIONS_REQUEST_READ_MEDIA = 10;
    private final static int PERMISSIONS_REQUEST_LOCATION = 11;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PhotoSharingApp app = (PhotoSharingApp) getApplication();
        String[] titles = new String[]{getString(R.string.main_title_list), getString(R.string.main_title_map)};

        Fragment[] fragments = new Fragment[]{new PhotoListFragment(), new MapFragment()};
        app.getMainComponent(this, fragments, titles, getSupportFragmentManager()).inject(this);


        String email = sharedPreferences.getString(app.getEmailKey(), "");
        toolbar.setTitle(email);
        setSupportActionBar(toolbar);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setupGoogleAPIClient();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        presenter.onCreate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupGoogleAPIClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    private void logout() {
        presenter.logout();
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void takePicture() {
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_MEDIA);
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        Intent chooserIntent = null;
        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoIntent.putExtra("return-data", true);

        File photoFile = null;
        try {
            photoFile = createFile();
        } catch (IOException e) {
            Snackbar.make(viewPager, R.string.main_error_dispatch_camera, Snackbar.LENGTH_SHORT).show();
        }

        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
            photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            if (photoIntent.resolveActivity(getPackageManager()) != null) {
                intentList = addIntentsToList(intentList, photoIntent);
            }
        }

        if (pickIntent.resolveActivity(getPackageManager()) != null) {
            intentList = addIntentsToList(intentList, pickIntent);
        }

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size()-1), getString(R.string.main_message_picture_source));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        startActivityForResult(chooserIntent, REQUEST_PICTURE);
    }

    private List<Intent> addIntentsToList(List<Intent> intentList, Intent intent) {
        List<ResolveInfo> infoList = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resInfo : infoList) {
            Intent targetIntent = new Intent(intent);
            String packageName = resInfo.activityInfo.packageName;
            targetIntent.setPackage(packageName);
            intentList.add(targetIntent);
        }
        return intentList;
    }

    private File createFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_PICTURE) {
            boolean isCamera = (data == null || data.getData() == null);
            if (isCamera) {
                addPicToGallery();
            } else {
                photoPath = getRealPathFromUri(data.getData());
            }
            presenter.uploadPhoto(lastLocation, photoPath);
        }
    }

    private void addPicToGallery() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(photoPath);
        Uri contentUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", file);
        intent.setData(contentUri);
        this.sendBroadcast(intent);
    }

    private String getRealPathFromUri(Uri data) {
        String result = null;
        Cursor cursor = getContentResolver().query(data, null, null, null, null);

        if (cursor == null) {
            result = data.getPath();
        } else {
            if (data.toString().contains("mediaKey")) {
                cursor.close();

                try {
                    File file = File.createTempFile("tempImg", ".jpg", getCacheDir());
                    InputStream input = getContentResolver().openInputStream(data);
                    OutputStream output = new FileOutputStream(file);

                    try {
                        byte[] buffer = new byte[4*1024];
                        int read;
                        
                        while ((read = input.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        result = file.getAbsolutePath();
                        
                    } finally {
                        output.close();
                        input.close();
                    }
                } catch (Exception e) {
                    Log.e(MainActivity.class.getSimpleName(), "Error getting file path", e);
                }
            } else {
                cursor.moveToFirst();
                result = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                cursor.close();
            }
        }

        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    takePhoto();
                break;
            case PERMISSIONS_REQUEST_LOCATION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void onUploadInit() {
        Snackbar.make(viewPager, R.string.main_notice_upload_init, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadComplete() {
        Snackbar.make(viewPager, R.string.main_notice_upload_complete, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadError(String error) {
        Snackbar.make(viewPager, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkLocationEnabled();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
            }
            else {
                Log.d("MainActivity", lastLocation.toString());
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(viewPager, connectionResult.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
    }

    private void checkLocationEnabled() {
        // check if location is enabled
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            showSettingsAlert(this);
        }
    }


    public void showSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Location Settings");
        alertDialog.setMessage("Location is not enabled. Do you want to go to settings menu ?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}

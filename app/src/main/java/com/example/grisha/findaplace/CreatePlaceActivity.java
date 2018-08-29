package com.example.grisha.findaplace;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class CreatePlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = CreatePlaceActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private static final int PICK_IMAGE_ID = 666;

    private ImageView mPlaceImage;
    private View mMapContainer;
    private EditText mPlaceTitle;
    private EditText mPlaceDescription;
    private EditText mPlaceLocationEditText;
    private EditText mPlacePhone;
    private EditText mPlaceUrl;
    private Switch mCurrentLocationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_create_place);

        mPlaceImage = findViewById(R.id.PlaceImage);
        mPlaceTitle = findViewById(R.id.PlaceTitle);
        mPlaceDescription = findViewById(R.id.PlaceDescription);
        mPlaceLocationEditText = findViewById(R.id.PlaceAddress);
        mPlacePhone = findViewById(R.id.PlacePhone);
        mPlaceUrl = findViewById(R.id.PlaceWebsite);
        mCurrentLocationSwitch = findViewById(R.id.CurrentLocationSwitch);
        mMapContainer = findViewById(R.id.mapContainer);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        handleLocationSwitch();
    }

    // Create Place Methods
    public void uploadImage(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    public void SavePlaceClick(View view) {
        if(isValidInputs())
        {
            PlaceItem placeItem = new PlaceItem()
                    .setPhone(mPlacePhone.getText().toString())
                    .setUrl(mPlaceUrl.getText().toString())
                    .setTitle(mPlaceTitle.getText().toString())
                    .setDescription(mPlaceDescription.getText().toString());

            // Set Place Image
            if( mPlaceImage.getDrawable() != null)
            {
                placeItem.setImage(((BitmapDrawable)mPlaceImage.getDrawable()).getBitmap());
            }

            // Set Place Location
            if( mLocationPermissionGranted && mCurrentLocationSwitch.isChecked() )
            {
                placeItem.setLocation(mLastKnownLocation);
            }
            else if(mPlaceLocationEditText.getText().length() > 1)
            {
                placeItem.setLocation(mPlaceLocationEditText.getText().toString());
            }

            DataManager.SavePlace(this, placeItem);

            finish();
        }
    }

    private boolean isValidInputs() {
        boolean returnvalue = true;

        if(mPlaceTitle.getText().length() <= 1)
        {
            Toast.makeText(this, "Place Title is too short", Toast.LENGTH_LONG).show();
            returnvalue = false;
        }

        return returnvalue;
    }

    private void handleLocationSwitch() {

        mCurrentLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    InitLocationPermission();
                }
                else {
                    mMapContainer.setVisibility(View.GONE);
                    mPlaceLocationEditText.setEnabled(true);
                    mPlaceLocationEditText.setHint(R.string.place_address);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {

            switch (reqCode) {
                case PICK_IMAGE_ID:
                    Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);

                    View AddPhotoIcon = findViewById(R.id.AddPhotoIcon);
                    AddPhotoIcon.setVisibility(View.GONE);

                    ViewGroup.LayoutParams params = mPlaceImage.getLayoutParams();
                    params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    params.height = RelativeLayout.LayoutParams.MATCH_PARENT;

                    mPlaceImage.setLayoutParams(params);
                    mPlaceImage.setImageBitmap(bitmap);
                    mPlaceImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
            }
        }

    }
    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.getUiSettings().setAllGesturesEnabled(false);

        InitLocationPermission();
    }

    private void InitLocationPermission()
    {
        // Prompt the user for permission.
        getLocationPermission();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        // Update activity ui
        updateLocationUi();
    }

    /**
     * Update switch, editText and display map
     */
    private void updateLocationUi() {
        if (mLocationPermissionGranted) {
            mMapContainer.setVisibility(View.VISIBLE);
            mCurrentLocationSwitch.setChecked(true);
            mPlaceLocationEditText.setEnabled(false);
            mPlaceLocationEditText.setHint(R.string.location_edittext);
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            SetMarker(mLastKnownLocation, "My Location");

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    /**
     * Set Marker on the map
     */
    private void SetMarker(Location position, String title) {
        if (position != null) {
            LatLng pos = new LatLng(position.getLatitude(), position.getLongitude());

            mMap.addMarker(new MarkerOptions().position(pos).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        }
    }
}

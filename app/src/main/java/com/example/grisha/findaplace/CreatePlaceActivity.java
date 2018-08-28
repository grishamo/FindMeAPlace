package com.example.grisha.findaplace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

public class CreatePlaceActivity extends Activity implements LocationListener {

    private static final int PICK_IMAGE_ID = 666;
    private static final int ACCESS_PERMISSION_REQUEST = 777;
    private static final int PLACE_PICKER_REQUEST = 888;
    private static final String KEY_LOCATION = "location";

    private ImageView m_PlaceImage;
    private EditText m_PlaceTitle;
    private EditText m_PlaceDescription;
    private EditText m_PlaceLocation;
    private EditText m_PlacePhone;
    private EditText m_PlaceUrl;
    private Switch m_CurrentLocationSwitch;
    private Location m_LastKnownLocation;
    private LatLng m_CurrentLocation;
    private LocationManager m_LocationManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);

        m_PlaceImage = findViewById(R.id.PlaceImage);
        m_PlaceTitle = findViewById(R.id.PlaceTitle);
        m_PlaceDescription = findViewById(R.id.PlaceDescription);
        m_PlaceLocation = findViewById(R.id.PlaceAddress);
        m_PlacePhone = findViewById(R.id.PlacePhone);
        m_PlaceUrl = findViewById(R.id.PlaceWebsite);
        m_CurrentLocationSwitch = findViewById(R.id.CurrentLocationSwitch);

        m_LocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            m_LastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
           // mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        InitPlaceLocationInput();
    }

    private void InitPlaceLocationInput() {

        if (Build.VERSION.SDK_INT >= 23) {
            int hasPermission = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                displayCurrentLocation();
            } else {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_PERMISSION_REQUEST);
            }
        } else {
            m_CurrentLocationSwitch.setChecked(true);
        }

        m_CurrentLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    m_PlaceLocation.setEnabled(false);
                    displayCurrentLocation();
                }
                else {
                    m_PlaceLocation.setEnabled(true);
                }
            }
        });
    }

    private void displayCurrentLocation()
    {
//        m_LastKnownLocation = m_LocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        boolean gps_enabled = m_LocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean network_enabled = m_LocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(gps_enabled)
        {
            m_LastKnownLocation = m_LocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else if(network_enabled)
        {
            m_LastKnownLocation = m_LocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if(m_LastKnownLocation != null) {


            double currentLongitude = m_LastKnownLocation.getLongitude();
            double currentLatitude = m_LastKnownLocation.getLatitude();

            m_CurrentLocation = new LatLng(currentLatitude, currentLongitude);

            m_CurrentLocationSwitch.setChecked(true);
            m_PlaceLocation.setEnabled(false);
        }
       // mFusedLocationProviderClient.getLastLocation();
    }

    public void uploadImage(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
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

                    ViewGroup.LayoutParams params = m_PlaceImage.getLayoutParams();
                    params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    params.height = RelativeLayout.LayoutParams.MATCH_PARENT;

                    m_PlaceImage.setLayoutParams(params);
                    m_PlaceImage.setImageBitmap(bitmap);
                    m_PlaceImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;

                case PLACE_PICKER_REQUEST:
                    Place place = PlacePicker.getPlace(data, this);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    break;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                m_CurrentLocationSwitch.setChecked(true);
            } else {
                Toast.makeText(this, "Sorry, can't get nearby places without permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        m_PlaceLocation.setText("");
        //pb.setVisibility(View.INVISIBLE);
        Toast.makeText(
                getBaseContext(),
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v(TAG, longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v(TAG, latitude);

        /*------- To get city name from coordinates -------- */
        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                + cityName;
        m_PlaceLocation.setText(s);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    public void SavePlaceClick(View view) {
        if(isValidInputs())
        {
            PlaceItem placeItem = new PlaceItem()
                    .setPhone(m_PlacePhone.getText().toString())
                    .setUrl(m_PlaceUrl.getText().toString())
                    .setTitle(m_PlaceTitle.getText().toString())
                    .setDescription(m_PlaceDescription.getText().toString());

            if( m_PlaceImage.getDrawable() != null)
            {
                placeItem.setImage(((BitmapDrawable)m_PlaceImage.getDrawable()).getBitmap());
            }

            DataManager.SavePlace(this, placeItem);

            finish();
        }
    }

    private boolean isValidInputs() {
        boolean returnvalue = true;

        if(m_PlaceTitle.getText().length() <= 1)
        {
            Toast.makeText(this, "Place Title is too short", Toast.LENGTH_LONG).show();
            returnvalue = false;
        }

        return returnvalue;
    }
}

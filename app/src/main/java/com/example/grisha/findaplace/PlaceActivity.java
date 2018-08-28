package com.example.grisha.findaplace;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int CALL_PERMISSION_REQUEST = 1;
    private PlaceItem mPlaceItem;
    private TextView mPlaceTitleView;
    private TextView mPlaceDescriptionView;
    private ImageView mPlaceImage;
    private View mPhoneBtnView;
    private View mWebBtnView;
    private View mShareBtnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        mPlaceItem = (PlaceItem) intent.getSerializableExtra("placeItem");

        setContentView(R.layout.activity_place);

        mPlaceTitleView = findViewById(R.id.PlaceTitle);
        mPlaceDescriptionView = findViewById(R.id.PlaceDescription);;
        mPlaceImage = findViewById(R.id.PlaceImage);;
        mPhoneBtnView = findViewById(R.id.callButton);
        mWebBtnView = findViewById(R.id.weblinkButton);;
        mShareBtnView = findViewById(R.id.shareButton);;

        setItemData(mPlaceItem);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setItemData(PlaceItem placeItem) {
        mPlaceItem = placeItem;
        mPlaceTitleView.setText(placeItem.getTitle());
        mPlaceDescriptionView.setText(placeItem.getDescription());
        mPlaceImage.setImageBitmap(placeItem.getImage());

        if(placeItem.getPhone() == null || placeItem.getPhone().length() < 1)
        {
            mPhoneBtnView.setVisibility(View.GONE);
        }

        if(placeItem.getUrl() == null || placeItem.getUrl().length() < 1)
        {
            mWebBtnView.setVisibility(View.GONE);
        }
    }


    public void CallPlaceClick(View view) {

        if (Build.VERSION.SDK_INT >= 23) {
            int hasCallPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasCallPermission == PackageManager.PERMISSION_GRANTED) {
                call();
            } else {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
            }
        }
        else {
            call();
        }

    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mPlaceItem.getPhone()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CALL_PERMISSION_REQUEST)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
               call();
            }
            else
                {
                    Toast.makeText(this, "Sorry, can't call without permission", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void OpenUrlClick(View view) {
        if(mPlaceItem.getUrl() != null) {
            String url = mPlaceItem.getUrl();

            if (!url.startsWith("http://") && !url.startsWith("https://"))
            {
                url = "http://" + url;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

    public void ShareClick(View view) {

//        Uri imageUri = Uri.parse("android.resource://" + getPackageName()
//                + "/drawable/" + "ic_launcher");
        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mPlaceItem.getTitle());
        //shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        //shareIntent.setType("image/jpeg");
        //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }
}

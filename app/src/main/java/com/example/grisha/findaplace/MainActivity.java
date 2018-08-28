package com.example.grisha.findaplace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends AppCompatActivity {

    private static final int ACCESS_PERMISSION_REQUEST = 1;
    private PlaceCategoryView myPlacesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataManager.GetLocalData(this);
        setContentView(R.layout.activity_main);

        myPlacesView = findViewById(R.id.myPlaces);
        myPlacesView.setCount( DataManager.LocalDataSize() );

        getLocationPermission();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        myPlacesView.setCount( DataManager.LocalDataSize() );
    }


    private void getLocationPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            int hasPermission = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                updateNearByPlaces();
            } else {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_PERMISSION_REQUEST);
            }
        } else {
            updateNearByPlaces();
        }
    }

    private void updateNearByPlaces() {
        View nearbyPlacesContainer = findViewById(R.id.nearbyPlacesContainer);
        nearbyPlacesContainer.setVisibility(View.VISIBLE);
    }

    public void openPlace(View view) {
        Intent intent = new Intent(this, CategoryActivity.class);

        intent.putExtra("categoryName", ((PlaceCategoryView) view).getCategoryName());
        intent.putExtra("categoryId",  ((PlaceCategoryView) view).getCategoryId());
        intent.putExtra("categoryTitle", ((PlaceCategoryView) view).getCategoryTitle());

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateNearByPlaces();
            } else {
                Toast.makeText(this, "Sorry, can't get nearby places without permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}

package com.example.grisha.findaplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashMap;

public class CategoryActivity extends Activity {

    private PlacesManager mPlacesManager;
    private int mCategoryId;
    private String mCategoryName;
    private String mCategoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategoryId = getIntent().getIntExtra("categoryId", -1);
        mCategoryName = getIntent().getStringExtra("categoryName");
        mCategoryTitle = getIntent().getStringExtra("categoryTitle");

        mPlacesManager = DataManager.getCageoryPlacesByName(this, mCategoryName);

        setContentView(R.layout.activity_category);

        updateCategoryView();
    }

    @Override
    public void onRestart() {
        super.onRestart();

        mPlacesManager = DataManager.getCageoryPlacesByName(this, mCategoryName);
        updateCategoryView();
    }

    private void updateCategoryView() {
        TextView title = (TextView) findViewById(R.id.CategoryTitle);
        title.setText(mCategoryTitle);

        if (mPlacesManager != null && mPlacesManager.getPlaces().size() > 0) {
            LinearLayout placesContainer = findViewById(R.id.PlacesContainer);
            placesContainer.removeAllViews();

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.bottomMargin = 2;

            for (PlaceItem placeItem : mPlacesManager.getPlaces().values()) {
                PlaceItemView placeView = new PlaceItemView(this);
                placeView.setLayoutParams(layoutParams);
                placeView.setPlaceData(placeItem);

                placeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CategoryActivity.this, PlaceActivity.class);
                        PlaceItem placeItem = ((PlaceItemView) view).getPlaceItem().toSerializable();
                        intent.putExtra("placeItem", placeItem);

                        startActivity(intent);
                    }
                });

                placesContainer.addView(placeView);
            }
        }

        // Dhow Add More Button
        if (mCategoryName.equals("my_places")) {
            View addButton = findViewById(R.id.AddPlaceButton);
            addButton.setVisibility(View.VISIBLE);
        }

    }

    public void AddNewPlace(View view) {
        Intent intent = new Intent(CategoryActivity.this, CreatePlaceActivity.class);
        startActivity(intent);
    }


}

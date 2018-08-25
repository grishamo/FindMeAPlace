package com.example.grisha.findaplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

public class CategoryActivity extends Activity {

    private String mCategoryName;
    private String mCategoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryName = getIntent().getStringExtra("categoryName");
        mCategoryTitle = getIntent().getStringExtra("categoryTitle");

        setContentView(R.layout.activity_category);
        displayCategoryDataByName(mCategoryName);
    }

    private void displayCategoryDataByName(String iCategoryName) {
        TextView title = (TextView)findViewById(R.id.CategoryTitle);
        title.setText(mCategoryTitle);

        if(iCategoryName.equals("my_places"))
        {
            View addButton = findViewById(R.id.AddPlaceButton);
            addButton.setVisibility(View.VISIBLE);
        }

    }

    public void AddNewPlace(View view) {
        Intent intent = new Intent(CategoryActivity.this, CreatePlaceActivity.class);
        startActivity(intent);
    }

    public void openPlace(View view) {
        Intent intent = new Intent(CategoryActivity.this, PlaceActivity.class);
        intent.putExtra("id", ((PlaceItemView)view).getPlaceId());
        startActivity(intent);
    }

}

package com.example.grisha.findaplace;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlaceItemView extends LinearLayout {

    private PlaceItem mPlaceItem;
    private TextView placeTitleView;
    private TextView placeDescriptionView;
    private ImageView placeImageView;
    private Bitmap mImage;
    private String mTitle;
    private String mDescription;
    private int mPlaceId;

    public PlaceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Get View Attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlaceItemView, 0, 0);

        mTitle = a.getString(R.styleable.PlaceItemView_placeTitle);
        mDescription = a.getString(R.styleable.PlaceItemView_placeDescription);
        mPlaceId = a.getInteger(R.styleable.PlaceItemView_placeId, -1);
        //int valueCount = a.getInteger(R.styleable.PlaceCategoryView_count, 0);
        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View correntView = inflater.inflate(R.layout.place_item, this, true);

        placeTitleView = correntView.findViewById(R.id.PlaceTitle);
        placeDescriptionView = correntView.findViewById(R.id.PlaceDescription);
        placeImageView = correntView.findViewById(R.id.PlaceImage);

        placeTitleView.setText(mTitle);
        placeDescriptionView.setText(mDescription);

    }

    public PlaceItemView(Context context) {
        this(context, null);
    }

    public int getPlaceId() { return mPlaceId; }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap mImage) {
        this.mImage = mImage;
        placeImageView.setImageBitmap(mImage);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
        placeTitleView.setText(mTitle);
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
        placeDescriptionView.setText(mDescription);
    }

    public PlaceItem getPlaceItem() {
        return mPlaceItem;
    }

    public void setPlaceItem(PlaceItem mPlaceItem) {
        this.mPlaceItem = mPlaceItem;
    }

    public void setPlaceData(PlaceItem placeData) {
        setPlaceItem(placeData);
        setImage(placeData.getImage());
        setDescription(placeData.getDescription());
        setTitle(placeData.getTitle());

        if(placeData.getImage() != null)
        {
            setImage(placeData.getImage());
        }
    }

}
package com.example.grisha.findaplace;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlaceItemView extends LinearLayout {

    private View mValue;
    private ImageView mImage;

    public PlaceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Get View Attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlaceItemView, 0, 0);

        String titleText = a.getString(R.styleable.PlaceItemView_placeTitle);
        String descriptionText = a.getString(R.styleable.PlaceItemView_placeDescription);
        //int valueCount = a.getInteger(R.styleable.PlaceCategoryView_count, 0);
        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        //setBackground(getResources().getDrawable(R.drawable.rounded_corners));
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View correntView = inflater.inflate(R.layout.place_item, this, true);

        TextView placeTitle = correntView.findViewById(R.id.PlaceTitle);
        TextView placeDescription = correntView.findViewById(R.id.PlaceDescription);
        ImageView placeImage = correntView.findViewById(R.id.PlaceImage);

        placeTitle.setText(titleText);
        placeDescription.setText(descriptionText);

    }

    public PlaceItemView(Context context) {
        this(context, null);
    }


}
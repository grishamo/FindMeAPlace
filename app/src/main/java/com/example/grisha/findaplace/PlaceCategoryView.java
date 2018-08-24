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

public class PlaceCategoryView extends LinearLayout {

    private View mValue;
    private ImageView mImage;

    public PlaceCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Get View Attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlaceCategoryView, 0, 0);

        String titleText = a.getString(R.styleable.PlaceCategoryView_titleText);
        @SuppressWarnings("ResourceAsColor")
        int valueColor = a.getColor(R.styleable.PlaceCategoryView_imageBackgroundColor, android.R.color.holo_blue_light);
        int valueCount = a.getInteger(R.styleable.PlaceCategoryView_count, 0);
        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        //setBackground(getResources().getDrawable(R.drawable.rounded_corners));
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View correntView = inflater.inflate(R.layout.place_category, this, true);

        TextView categoryTitle = correntView.findViewById(R.id.CategoryTitle);
        TextView categoryCount = correntView.findViewById(R.id.CategoryCount);
        ImageView categoryImage = correntView.findViewById(R.id.CategoryImage);

        categoryCount.setText(Integer.toString(valueCount));
        categoryTitle.setText(titleText);
        categoryImage.setBackgroundColor(valueColor);

    }

    public PlaceCategoryView(Context context) {
        this(context, null);
    }

    public void setValueColor(int color) {
        mValue.setBackgroundColor(color);
    }

    public void setImageVisible(boolean visible) {
        mImage.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}
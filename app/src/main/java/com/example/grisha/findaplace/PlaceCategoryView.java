package com.example.grisha.findaplace;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlaceCategoryView extends LinearLayout {

    private View mValue;
    private ImageView mImage;
    private String mCategoryName;
    private String mCategoryTitle;
    private int mCategoryId;
    private int mCount;

    private TextView categoryTitleView;
    private TextView categoryCountView;
    private ImageView categoryImageView;
    private RelativeLayout categoryColorView;

    public PlaceCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Get View Attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlaceCategoryView, 0, 0);

        mCategoryTitle = a.getString(R.styleable.PlaceCategoryView_titleText);
        mCategoryName = a.getString(R.styleable.PlaceCategoryView_categoryName);
        mCategoryId = a.getInteger(R.styleable.PlaceCategoryView_categoryId, -1);
        int categoryIcon = a.getInteger(R.styleable.PlaceCategoryView_categoryIcon, -1);

        @SuppressWarnings("ResourceAsColor")
        int valueColor = a.getColor(R.styleable.PlaceCategoryView_imageBackgroundColor, android.R.color.holo_blue_light);
        mCount = a.getInteger(R.styleable.PlaceCategoryView_count, 0);
        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View correntView = inflater.inflate(R.layout.place_category, this, true);

        categoryTitleView = correntView.findViewById(R.id.CategoryTitle);
        categoryCountView  = correntView.findViewById(R.id.CategoryCount);
        categoryImageView  = correntView.findViewById(R.id.CategoryImage);
        categoryColorView  = correntView.findViewById(R.id.CategoryColor);

        categoryTitleView.setText(mCategoryTitle);
        categoryTitleView.setAllCaps(true);
        categoryCountView.setText(Integer.toString(mCount));
        //categoryImageView.setImageResource(categoryIcon);
        categoryColorView.setBackgroundColor(valueColor);

    }

    public PlaceCategoryView(Context context) {
        this(context, null);
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public String getCategoryTitle() {
        return mCategoryTitle;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCount(int i) {
        mCount = i;
        categoryCountView.setText(Integer.toString(mCount));
    }
}
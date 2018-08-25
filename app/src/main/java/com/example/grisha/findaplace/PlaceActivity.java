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
import android.view.View;
import android.widget.Toast;

public class PlaceActivity extends Activity {

    private static final int CALL_PERMISSION_REQUEST = 1;
    private int mPlaceId;
    private String mPlaceTitle;
    private String mPlaceDescription;
    private String mPlacePhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlaceId = getIntent().getIntExtra("id", -1);

        setContentView(R.layout.activity_place);
    }


    public void CallPlace(View view) {

        if (Build.VERSION.SDK_INT >= 23) {
            int hasCallPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasCallPermission == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
            }
        }
        else {
            callPhone();
        }

    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mPlacePhoneNumber));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CALL_PERMISSION_REQUEST)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
               callPhone();
            }
            else
                {
                    Toast.makeText(this, "Sorry, can't call without permission", Toast.LENGTH_LONG).show();
                }
        }
    }
}

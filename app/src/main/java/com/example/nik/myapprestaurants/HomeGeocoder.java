package com.example.nik.myapprestaurants;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.io.IOException;
import java.util.List;
import android.util.Log;
import android.widget.Toast;

public class HomeGeocoder extends AppCompatActivity {

    final String LOG = "LOG";
    String strLocation;
    double mLatitude;
    double mLongitude;
    Intent mIntent;
    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addresses = null;
        strLocation = getIntent().getStringExtra("mStrLocation");
        geocoder = new Geocoder(getBaseContext());

        try {
            addresses = geocoder.getFromLocationName(strLocation, 10);
            //for (int i = 0; i < addresses.size(); i++) {
            if (!addresses.isEmpty()) {
                Address loc = addresses.get(0);
                mLatitude = loc.getLatitude();
                mLongitude = loc.getLongitude();

                mIntent = new Intent();
                mIntent.putExtra("Lat", mLatitude);
                mIntent.putExtra("Lon", mLongitude);
                setResult(RESULT_OK, mIntent);
                finish();
            } else {
                Toast.makeText(this, "No location results", Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "GeoCoder error", Toast.LENGTH_SHORT).show();
            Log.d(LOG, "----------------------onCreate GeoCoder error");
            finish();
        }

    }
}

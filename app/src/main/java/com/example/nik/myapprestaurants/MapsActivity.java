package com.example.nik.myapprestaurants;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity {

    Geocoder geocoder;
    SupportMapFragment mapFragment;
    GoogleMap map;
    Marker marker;
    final String TAG = "myLogs";
    Address address;
    private GoogleMap mMap;
    int maxResult;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> locations = geocoder.getFromLocationName("Киев", 10);

            for (int i = 0; i < locations.size(); i++) {
                Address loc = locations.get(i);

                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        map = mapFragment.getMap();
        if (map == null) {
            finish();
            return;
        }
        init();
    }


    private void init() {
        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Ресторан")
                .snippet("Additional text"));
    }

    public void onClickTest(View view) {
        switch (view.getId()){
            case R.id.btnTest:
                marker.showInfoWindow();
                break;
            case R.id.btnTest2:
                Intent intent= new Intent(this,MainActivity.class);
                startActivity(intent);
                break;

            case R.id.btnTest3:
                Intent intent1= new Intent(this,FoursquareActivity.class);
                startActivity(intent1);
                break;
        }

    }


}

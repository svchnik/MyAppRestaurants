package com.example.nik.myapprestaurants;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity {

    public static final int REQUEST_CODE_LOCATION = 788;
    public static final int REQUEST_CODE_GEODECOD = 789;
    final String LOG = "LOG";
    String mEditText = "Dalvik, Iceland";
    double mlatitude;
    double mlongitude;
    Geocoder geocoder;
    SupportMapFragment mapFragment;
    GoogleMap map;
    Marker marker;
    Marker marker2;
    TextView textViewLatitude;
    TextView textViewLongitude;
    EditText editText;
    Button btnLocation;
    Button btnEditText;
    Intent intent;
    Intent intent2;
    Address address;
    List<Address> locations = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        textViewLatitude = (TextView) findViewById(R.id.textViewLatitude);
        textViewLongitude = (TextView) findViewById(R.id.textViewLongitude);
        editText = (EditText) findViewById(R.id.editText);
        btnLocation = (Button) findViewById(R.id.btnLocation);
        btnEditText = (Button) findViewById(R.id.btnEditText);

        geocoder = new Geocoder(this, Locale.getDefault());

        mLocationRefresh(mEditText);
    }


    private void init() {
        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(mlatitude, mlongitude))
                .title("Ресторан")
                .snippet("Additional text"));
        Log.d(LOG, "----------------------addMarker1");
    }


    private void init2() {
        marker2 = map.addMarker(new MarkerOptions()
                .position(new LatLng(mlatitude, mlongitude))
                .title("Ресторан2")
                .snippet("Additional text2")
                .icon(BitmapDescriptorFactory.defaultMarker())
        );
        Log.d(LOG, "----------------------addMarker2");
    }

    public void onClickTest(View view) {

        switch (view.getId()){

            case R.id.btnLocation:
                intent = new Intent(this, MyGoogleGPSCoordinate.class);
                startActivityForResult(intent,REQUEST_CODE_LOCATION);
                //intent= new Intent(this,MainService.class);
                //startService(intent);
                Log.d(LOG, "----------------------startActivityForResult");
                break;
            case R.id.btnEditText:
                mEditText = editText.getText().toString();
                if (!TextUtils.isEmpty(mEditText.trim())) {
                    intent2 = new Intent(this, HomeGeocoder.class);
                    intent2.putExtra("mStrLocation", mEditText);
                    startActivityForResult(intent2, REQUEST_CODE_GEODECOD);
                    Log.d(LOG, "----------------------startActivityForResult2");
                }else {
                    Toast.makeText(this, "No enter text sending", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.btnTest:
                //получаем результат и останавливаем сервис
                //stopService(intent);
                Log.d(LOG, "----------------------stopService");
                marker.showInfoWindow();
                break;

            case R.id.btnTest3:
                //Intent intent1= new Intent(this,FoursquareActivity.class);
                //startActivity(intent1);
                break;
        }

    }

    public void mLocationRefresh(String strLocation){

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null) {
            Toast.makeText(this, "No Maps", Toast.LENGTH_SHORT).show();
            finish();
            //return;
        }
        Toast.makeText(this, "есть - " + String.valueOf(mlatitude) + " *** " + String.valueOf(mlongitude) ,
                Toast.LENGTH_SHORT).show();
        init();
        mLatLongRefresh();
    }




    public void mLatLongRefresh(){
        textViewLatitude.setText(String.valueOf(mlatitude));
        textViewLongitude.setText(String.valueOf(mlongitude));
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_LOCATION){
            if(resultCode == RESULT_OK){
                Log.d(LOG, "----------------------REQUEST_CODE_LOCATION");
                mlatitude = data.getDoubleExtra("Lat", 0);
                mlongitude = data.getDoubleExtra("Lon", 0);
                mLatLongRefresh();
            }else{
                Toast.makeText(this, "NO responce1", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == REQUEST_CODE_GEODECOD){
            if(resultCode == RESULT_OK){
                Log.d(LOG, "----------------------REQUEST_CODE_GEODECOD");
                mlatitude = data.getDoubleExtra("Lat", 0);
                mlongitude = data.getDoubleExtra("Lon", 0);
                mLatLongRefresh();
                //init2();
            }else{
                Toast.makeText(this, "NO responce2", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
package com.example.nik.myapprestaurants;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.Locale;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MapsActivityFragment extends Fragment {

    public static final int REQUEST_CODE_LOCATION = 788;
    public static final int REQUEST_CODE_GEODECOD = 789;
    final String LOG = "LOG";
    String mEditText = "";
    double mLatitude, mLongitude;
    SupportMapFragment mapFragment;
    Geocoder geocoder;
    GoogleMap map;
    Marker marker;
    TextView textViewLatitude, textViewLongitude;
    EditText editText;
    Button btnLocation, btnEditText;
    Intent intent, intent2;
    LatLng latLng;
    View view;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        //setContentView(R.layout.activity_maps);

        view = inflater.inflate(R.layout.activity_maps, container, false);

        textViewLatitude = (TextView) view.findViewById(R.id.textViewLatitude);
        textViewLongitude = (TextView) view.findViewById(R.id.textViewLongitude);
        editText = (EditText) view.findViewById(R.id.editText);
        btnLocation = (Button) view.findViewById(R.id.btnLocation);
        btnEditText = (Button) view.findViewById(R.id.btnEditText);

        mLatitude = 0.0;
        mLongitude = 0.0;
        latLng = new LatLng(mLatitude, mLongitude);

        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        mLocationRefresh();

        //------------------------------
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG, "----------------------btnLocation");
                intent = new Intent(getActivity(), MyGoogleGPSCoordinate.class);
                startActivityForResult(intent,REQUEST_CODE_LOCATION);
            }
        });

        //----------------------------------
        btnEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG, "----------------------btnEditText");
                mEditText = editText.getText().toString();
                if (!TextUtils.isEmpty(mEditText.trim())) {
                    intent2 = new Intent(getActivity(), HomeGeocoder.class);
                    intent2.putExtra("mStrLocation", mEditText);
                    startActivityForResult(intent2, REQUEST_CODE_GEODECOD);
                    Log.d(LOG, "----------------------startActivityForResult2");
                }else {
                    Toast.makeText(getActivity(), "No enter text sending", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }



    public void onClickTest(View view) {
        switch (view.getId()){

            case R.id.btnLocation:
               /*
                intent = new Intent(getActivity(), MyGoogleGPSCoordinate.class);
                startActivityForResult(intent,REQUEST_CODE_LOCATION);
                */
                //intent= new Intent(this,MainService.class);
                //startService(intent);
                Log.d(LOG, "----------------------startActivityForResult");
                break;
            case R.id.btnEditText:
               /*
                mEditText = editText.getText().toString();
                if (!TextUtils.isEmpty(mEditText.trim())) {
                    intent2 = new Intent(getActivity(), HomeGeocoder.class);
                    intent2.putExtra("mStrLocation", mEditText);
                    startActivityForResult(intent2, REQUEST_CODE_GEODECOD);
                    Log.d(LOG, "----------------------startActivityForResult2");
                }else {
                    Toast.makeText(getActivity(), "No enter text sending", Toast.LENGTH_SHORT).show();
                }
                */
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


    public void mLocationRefresh(){
        //mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null) {
            Toast.makeText(getActivity(), "No Maps", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        Toast.makeText(getActivity(), "есть - " + String.valueOf(mLatitude) + " *** " + String.valueOf(mLongitude) ,
                Toast.LENGTH_SHORT).show();
        init();
        mLatLongRefresh();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_LOCATION){
            if(resultCode == Activity.RESULT_OK){
                mLatitude = data.getDoubleExtra("Lat", 0);
                mLongitude = data.getDoubleExtra("Lon", 0);
                mLatLongRefresh();

                latLng = new LatLng(mLatitude, mLongitude);
                marker.setPosition(latLng);
                myMoveCamera(latLng);
            }else{
                Toast.makeText(getActivity(), "No responce1", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == REQUEST_CODE_GEODECOD){
            Log.d(LOG, "----------------------REQUEST_CODE_GEODECOD");
            if(resultCode == Activity.RESULT_OK){
                mLatitude = data.getDoubleExtra("Lat", 0);
                mLongitude = data.getDoubleExtra("Lon", 0);
                mLatLongRefresh();

                latLng = new LatLng(mLatitude, mLongitude);

                if(marker == null){
                    init();
                }
                marker.setPosition(latLng);
                myMoveCamera(latLng);
            }else{
                Toast.makeText(getActivity(), "No responce2", Toast.LENGTH_SHORT).show();
            }
        }
    }


/*
    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK, null);
    }
*/


    private void init() {
        marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Ресторан")
                .snippet("Additional text"));
        Log.d(LOG, "----------------------addMarker1");
    }


    public void myMoveCamera(LatLng latLngN) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLngN)
                .zoom(12)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
    }

    public void mLatLongRefresh(){
        textViewLatitude.setText(String.valueOf(mLatitude));
        textViewLongitude.setText(String.valueOf(mLongitude));
    }


/*
    private void init2() {
        marker2 = map.addMarker(new MarkerOptions()
                //.position(new LatLng(mlatitude, mlongitude))
                .position(latLng2)
                .title("Ресторан2")
                .snippet("Additional text2")
                //.icon(BitmapDescriptorFactory.defaultMarker())
        );
        Log.d(LOG, "----------------------addMarker2");
    }
    */
}
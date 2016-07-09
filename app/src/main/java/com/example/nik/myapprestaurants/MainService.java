package com.example.nik.myapprestaurants;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainService extends Service {

    public final String LOG = "LOG";
    Context context;

    double mGetLatitudeGPS;
    double mGetLongitudeGPS;
    double mGetLatitudeNet;
    double mGetLongitudeNet;

    public LocationManager locationManager;



    @Override
    public void onCreate() {
        Log.d(LOG, "----------------------onCreate");
        context = getApplicationContext();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "----------------------onStartCommand");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Consider calling
            return 0;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);
        //checkEnabled();




        return super.onStartCommand(intent, flags, startId);
    }







    //******************************
    //слушатель
    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {   //вывод новых данных о местоположении
            //showLocation(location);
            mGetCoordinates(location);
        }

        @Override
        public void onProviderDisabled(String provider) {    //указанный провайдер был отключен юзером
            //checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {    //указанный провайдер был включен юзером
            //checkEnabled();
            if (ActivityCompat.checkSelfPermission(MainService.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Consider calling
                return;
            }


            //запрашиваем последнее доступное местоположение от включенного провайдера и отображаем
            //showLocation(locationManager.getLastKnownLocation(provider));
            mGetCoordinates(locationManager.getLastKnownLocation(provider));
        }

        @Override                                       //изменился статус  провайдера
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    };
//******************************





    //открыть установки андроид
    public void onClickLocationSettings(View view) {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void mGetCoordinates(Location location) {
        Log.d(LOG, "----------------------mGetCoordinates");
        if (location == null)
            return;

        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            mGetLatitudeGPS = location.getLatitude();
            mGetLongitudeGPS = location.getLongitude();
        } else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            mGetLatitudeNet = location.getLatitude();
            mGetLongitudeNet = location.getLongitude();
        }


    }

    @Override
    public void onDestroy() {
        Log.d(LOG, "----------------------onDestroy");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);          //отключаем слушателя
        super.onDestroy();

    }
}




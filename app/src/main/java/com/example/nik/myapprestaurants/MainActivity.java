package com.example.nik.myapprestaurants;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import java.util.Date;


public class MainActivity extends Activity {
    /*
    1. Пользователь должен иметь возможность ввести адресс или zip-код локации (12345-6789)
    2. Пользователь должен иметь возможность автоматически определить свое положение с помощью GPS
    или любым доступным способом и использовать его как базу для поиска.
    3. Приложение должно отображать рестораны вокруг заданой локации с помощью маркеров на карте и в режиме списка.
    4. В режиме списка должна отображаться следующая информация: название ресторана и растояние до указанного пользователем
 адресса или его местоположением. Так же, ретсораны должны быть отсортированны в списке по дистанции до выбраного
  пользователем адресса или локации.
    5.Как в режиме карты так и по нажатию на элесмент списка должен открываться экран (или показыватся диалог) в котором
 будет выведены имя ретсорана, контактный номыр телефона, адресс и ценовой рейтинг ($, $$  и т.п.)
    6. В открывшемся окне пользователь должен иметь возможность указать рейтинг ресторана и ввести свой никнейм, оценка
 каждого следующего пользователя перетирает оценку предыдущего.
    7.При скролинге списка зона поиска должна расширятся, так же пользователь должне иметь возможность редактировать
 зону поиска посредствам zoomIn/zoomOut в режиме карты.
    8. В режиме карты, ретсоран с самой высокой оценкой должен быть отмечен маркером другого цвета, если такиз ресторанов
 несколько - то они все должны иметь такой маркер.
Приложение должно поддерживать кеширование и работу в offline-режиме на основе сохраненных данных.
Приложение должно поддерживать поворот экрана.
    */

    //private MapView mapView;

    Context context;

    TextView tvEnabledGPS;
    TextView tvStatusGPS;
    TextView tvLocationGPS;
    TextView tvEnabledNet;
    TextView tvStatusNet;
    TextView tvLocationNet;

    public LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        context = getApplicationContext();
        tvEnabledGPS = (TextView) findViewById(R.id.tvEnabledGPS);
        tvStatusGPS = (TextView) findViewById(R.id.tvStatusGPS);
        tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
        tvEnabledNet = (TextView) findViewById(R.id.tvEnabledNet);
        tvStatusNet = (TextView) findViewById(R.id.tvStatusNet);
        tvLocationNet = (TextView) findViewById(R.id.tvLocationNet);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);
        checkEnabled();
    }


    //инфа о включенности провайдеров
    private void checkEnabled() {
        tvEnabledGPS.setText("Enabled: " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        tvEnabledNet.setText("Enabled: " + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }




    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.removeUpdates(locationListener);                      //отключаем слушателя
    }


    //слушатель
    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {   //вывод новых данных о местоположении
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {    //указанный провайдер был отключен юзером
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {    //указанный провайдер был включен юзером
            checkEnabled();
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            //запрашиваем последнее доступное местоположение от включенного провайдера и отображаем
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override                                       //изменился статус  провайдера
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                tvStatusGPS.setText("Status: " + String.valueOf(status));
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                tvStatusNet.setText("Status: " + String.valueOf(status));
            }
        }
    };



    //определяем соотв провайдера и отображаем координаты
    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            tvLocationGPS.setText(formatLocation(location));
        } else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            tvLocationNet.setText(formatLocation(location));
        }
    }



    // читаем location и форматируем вывод
    private String formatLocation(Location location) {
        if (location == null)
            return "";

        return String.format("Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(location.getTime()));
    }




    //открыть установки андроид
    public void onClickLocationSettings(View view) {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    };

}

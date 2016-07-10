package com.example.nik.myapprestaurants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MapsActivity extends SingleFragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_maps);
    }

    @Override
    protected Fragment createFragment() {
        return new MapsActivityFragment();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

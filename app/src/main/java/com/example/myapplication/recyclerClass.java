package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class recyclerClass extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mMapView;
    String address;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        TextView textTitle = findViewById(R.id.textView);
        TextView textAddress = findViewById(R.id.textView4);
        TextView textSport = findViewById(R.id.textView8);
        TextView textNumbers = findViewById(R.id.textView10);
        TextView textMoreInfo = findViewById(R.id.textView11);
        Bundle mapViewBundle = null;

        String title = "N/A";
        address = "N/A";
        String sport = "N/A";
        String numbers = "N/A";
        String moreInfo = "N/A";
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            title = extras.getString("title");
            address = extras.getString("address");
            sport = extras.getString("sport");
            numbers = extras.getString("numbers");
            moreInfo = extras.getString("moreInfo");
        }
        textTitle.setText(title);
        textAddress.setText(address);
        textSport.setText(sport);
        textNumbers.setText(numbers);
        textMoreInfo.setText(moreInfo);

        mMapView = (MapView) findViewById(R.id.mapView);
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("maps_api_key");
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);


/*        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(savedInstanceState.maps_api_key);
        }*/


    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle("maps_api_key");
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle("maps_api_key", mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng locati = getLocationFromAddress(address);
        map.addMarker(new MarkerOptions().position(locati));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(locati,12.0f));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    public LatLng getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {

            address = coder.getFromLocationName(strAddress,5);

            if (address==null) {

                return null;
            }
            Address location=address.get(0);


            //Log.e(TAG,"JUMALAUfTA :" + location.getLatitude() + " , "+ location.getLongitude());
            p1 = new LatLng((double) (location.getLatitude() ),
                    (double) (location.getLongitude()));

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }
}
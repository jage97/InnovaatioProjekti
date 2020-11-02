package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Workbook;
import jxl.Sheet;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import static android.content.ContentValues.TAG;

public class sportList extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    AsyncHttpClient client;
    Workbook workbook;
    List<String> titles, addresses, cities, sports;
    List<LatLng> coordinates;
    private AlertDialog.Builder dialogBuilderLogin, dialogBuilderInfo;
    private AlertDialog dialog, dialogInfo;
    private EditText user, password;
    private Button loginButton, exitButton, rekButton;


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };
    LocationManager locationManager;
    private int[] images = {R.drawable.tennis, R.drawable.football, R.drawable.hockey, R.drawable.boxing, R.drawable.athletics, R.drawable.wrestling};

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportlist);
        dialogBuilderInfo = new AlertDialog.Builder(this);
        String url = "https://github.com/jage97/InnovaatioProjekti/blob/new-master/sportPlaces.xls?raw=true";
        recyclerView = findViewById(R.id.listOfSport);
        titles = new ArrayList<>();
        addresses = new ArrayList<>();
        coordinates = new ArrayList<>();
        cities = new ArrayList<>();
        sports = new ArrayList<>();
        client = new AsyncHttpClient();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

        getLocation();
        client.get(url, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                WorkbookSettings ws = new WorkbookSettings();
                ws.setEncoding("windows-1252");
                ws.setGCDisabled(true);
                if(file != null){
                    try {
                        workbook = workbook.getWorkbook(file,ws);
                        Sheet sheet = workbook.getSheet(0);
                        for(int i = 1;i < sheet.getRows();i++){
                            Cell[] row = sheet.getRow(i);
                            titles.add(row[0].getContents());
                            addresses.add(row[1].getContents());
                            cities.add(row[2].getContents());
                            sports.add(row[3].getContents());
                        }
                        for(int i = 0;i <addresses.size();i++) {
                            coordinates.add(getLocationFromAddress("Matinraitti 5 Espoo finland"));
                        }
                        showData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void changeItem(int position) {
        String str1 = Integer.toString(position);
        Toast.makeText(this, str1, Toast.LENGTH_SHORT).show();


    }


    private void showData() {
        adapter = new Adapter(this, titles, addresses, cities, sports, images, getLocation(),coordinates);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position);
                Intent intent = new Intent(getApplicationContext(),recyclerClass.class);
                intent.putExtra("title", titles.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.user:
                Toast.makeText(this, "kayttaja painettu", Toast.LENGTH_SHORT).show();
                createNewContactDialogLogin();
                return true;
            case R.id.filter:
                Toast.makeText(this, "rajaa painettu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.map:
                Toast.makeText(this, "kartta painettu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.filter1:
                Toast.makeText(this, "rajaa 1 painettu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.filter2:
                Toast.makeText(this, "rajaa 2 painettu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.filter3:
                Toast.makeText(this, "rajaa 3 painettu", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.search:
                Toast.makeText(this, "Haku", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
  /*  public void createNewContactDialogInfo(int position){
        dialogBuilderInfo = new AlertDialog.Builder(this);
        View contactPopupViewInfo = getLayoutInflater().inflate(R.layout.popupinfo, null);
        dialogBuilderInfo.setView(contactPopupViewInfo);



        String temp = titles.get(position);
        final  TextView  textTitle = (TextView)findViewById(R.id.textTitle);
        //textTitle.setText("hrt");
        //textTitle.setText(temp);
        dialogInfo.setView(textTitle);
        dialogInfo = dialogBuilderInfo.create();
        dialogInfo.show();
    }*/

    public void createNewContactDialogLogin(){
        dialogBuilderLogin = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        user = (EditText) contactPopupView.findViewById(R.id.user);
        password = (EditText) contactPopupView.findViewById(R.id.password);
        loginButton = (Button) contactPopupView.findViewById(R.id.loginButton);
        exitButton = (Button) contactPopupView.findViewById(R.id.exitButton);
        rekButton = (Button) contactPopupView.findViewById(R.id.rekButton);

        dialogBuilderLogin.setView(contactPopupView);
        dialog = dialogBuilderLogin.create();
        dialog.show();

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sisaankirjautuminen tahan
            }
        });
        rekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rekisterointi tahan
            }
        });
    }
    String getLocation() {
        String sLocation = "";
        LatLng coordinates = null;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( (Activity) this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }else{
            Location location = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
            if(location != null){
                double lati = location.getLatitude();
                double longi = location.getLongitude();
                // Log.e(TAG,"Location :" +lati+ " ,"+longi );
                sLocation = lati+"";
                Log.e(TAG,"Location :" + sLocation );

            } else {
                boolean isNetworkAvailable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if(isNetworkAvailable) fetchCurrentLocation();
            }
        }
        Log.e(TAG,"Locatio2n :" + sLocation );
        return sLocation;
    }
    void fetchCurrentLocation() {
        try {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    if (location != null) {
                        locationManager.removeUpdates(this);
                        double lati = location.getLatitude();
                        double longi = location.getLongitude();

                        //Log.e(TAG,"Location :" +lati+ " ,"+longi );
                    }
                }

                @Override
                public void onProviderDisabled(String s) {
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }
            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500L,30f, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
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


            //Log.e(TAG,"JUMALAUTA :" + location.getLatitude() + " , "+ location.getLongitude());
            p1 = new LatLng((double) (location.getLatitude() ),
                    (double) (location.getLongitude()));

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }
}
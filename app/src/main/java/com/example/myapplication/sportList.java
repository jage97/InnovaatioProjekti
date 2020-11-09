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
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import android.widget.SearchView;
import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Workbook;
import jxl.Sheet;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.SearchManager;
import android.widget.SearchView.OnQueryTextListener;

import static android.content.ContentValues.TAG;

public class sportList extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    AsyncHttpClient client;
    Workbook workbook;
    //Excel
    List<String> titles, addresses, cities, sports, websites, regi, managers, supervisor, moreInfo;
    List<LatLng> coordinates;
    private AlertDialog.Builder dialogBuilderLogin, dialogBuilderInfo;
    private AlertDialog dialog, dialogInfo;
    private EditText user, password;
    private Button loginButton, exitButton, rekButton;
    private static Context context;

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
        Bundle extras = getIntent().getBundleExtra("bundl");

        sportList.context = getApplicationContext();
        dialogBuilderInfo = new AlertDialog.Builder(context);
        String url = "https://github.com/jage97/InnovaatioProjekti/blob/master/toimikko.xls?raw=true";

        //Excel fetch
        titles = new ArrayList<>();     //row 0
        addresses = new ArrayList<>();  //row 1
        cities = new ArrayList<>();     //row 2
        websites = new ArrayList<>();   //row 3
        sports = new ArrayList<>();     //row 4
        regi = new ArrayList<>();       //row 5
        managers = new ArrayList<>();   //row 6
        supervisor = new ArrayList<>(); //row 7 & 8
        moreInfo = new ArrayList<>();   //row 9


        coordinates = new ArrayList<>();
        // client = new AsyncHttpClient();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        titles = extras.getStringArrayList("titles");
        addresses = extras.getStringArrayList("addresses");
        cities = extras.getStringArrayList("cities");
        websites = extras.getStringArrayList("websites");
        sports = extras.getStringArrayList("sports");
        regi = extras.getStringArrayList("regi");
        managers = extras.getStringArrayList("managers");
        supervisor = extras.getStringArrayList("supervisor");
        moreInfo = extras.getStringArrayList("moreinfo");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        showData();
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

//            getLocation();
    }


    public void changeItem(int position) {
        String str1 = Integer.toString(position);
        Toast.makeText(this, str1, Toast.LENGTH_SHORT).show();


    }
    private void showData() {
        adapter = new Adapter(context, titles, addresses, cities, sports, images, "getLocation()");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView = (RecyclerView) findViewById(R.id.listOfSport);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position);
                Intent intent = new Intent(context,recyclerClass.class);
                intent.putExtra("title", titles.get(position));
                intent.putExtra("address", addresses.get(position) +" " +cities.get(position));
                intent.putExtra("sport", sports.get(position));
                intent.putExtra("numbers", regi.get(position) + managers.get(position) + supervisor.get(position));
                intent.putExtra("moreInfo", websites.get(position)+moreInfo.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_scrolling, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
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
                //LogIn
            }
        });
        rekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Register
                dialog.dismiss();
                Intent intentRegister = new Intent(sportList.this, RegisterActivity.class);
                startActivity(intentRegister);
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
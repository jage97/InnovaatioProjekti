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
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnQueryTextListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import static android.content.ContentValues.TAG;

import static android.content.ContentValues.TAG;

public class sportList extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    AsyncHttpClient client;
    Workbook workbook;
    //Excel
    List<String> titles, addresses, cities, sports, websites, regi, managers, supervisor, superhumans, contacts, trainers, appointments, beachstaff, specialRegi, moreInfo, rating;
    List<LatLng> coordinates;
    private AlertDialog.Builder dialogBuilderLogin, dialogBuilderInfo;
    private AlertDialog dialog, dialogInfo;
    private EditText user, passwordText;
    private Button loginButton, exitButton, rekButton;
    private static Context context;
    String temp, temp2 = "";
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

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
        mAuth = FirebaseAuth.getInstance();
        sportList.context = getApplicationContext();
        dialogBuilderInfo = new AlertDialog.Builder(context);

        //Excel fetch
        titles = new ArrayList<>();     //row 0
        addresses = new ArrayList<>();  //row 1
        cities = new ArrayList<>();     //row 2
        websites = new ArrayList<>();   //row 3
        sports = new ArrayList<>();     //row 4
        regi = new ArrayList<>();       //row 5
        managers = new ArrayList<>();   //row 6
        supervisor = new ArrayList<>(); //row 7 & 8
        superhumans = new ArrayList<>(); //row 9 & 10
        contacts = new ArrayList<>(); //row 11 & 12
        trainers = new ArrayList<>(); //row 13
        appointments = new ArrayList<>(); //row 14
        beachstaff = new ArrayList<>(); //row 15
        specialRegi = new ArrayList<>(); //row 16
        moreInfo = new ArrayList<>();   //row 17
        rating = new ArrayList<>();     //row 18

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
        superhumans = extras.getStringArrayList("superhumans");
        contacts = extras.getStringArrayList("contacts");
        trainers = extras.getStringArrayList("trainers");
        appointments = extras.getStringArrayList("appointments");
        beachstaff = extras.getStringArrayList("beachstaff");
        specialRegi = extras.getStringArrayList("specialRegi");
        moreInfo = extras.getStringArrayList("moreinfo");
        rating = extras.getStringArrayList("rating");
       // Log.e(TAG,titles.size()+" "+ addresses.size()+" "+websites.size()+" "+sports.size()+" "+regi.size()+" "+managers.size()+" "+supervisor.size()+" "+superhumans.size()+" "+contacts.size()+" "+trainers.size()+" "+appointments.size()+" "+beachstaff.size()+" "+specialRegi.size());

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        showData();
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

        //getLocation();
    }


    public void changeItem(int position) {
        String str1 = Integer.toString(position);
        Toast.makeText(this, str1, Toast.LENGTH_SHORT).show();


    }
    private void showData() {
        adapter = new Adapter(context, titles, addresses, cities, sports, images, rating, "getLocation()");
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
                temp = "";
                temp2 = "";
                if(regi.get(position).length() > 1){
                    temp = temp + regi.get(position) + "\n";
                }
                if(managers.get(position).length() > 1){
                    temp = temp + managers.get(position) + "\n";
                }
                if(supervisor.get(position).length() > 1){
                    temp = temp + supervisor.get(position) + "\n";
                }
                if(superhumans.get(position).length() > 1){
                    temp = temp + superhumans.get(position) + "\n";
                }
                if(contacts.get(position).length() > 1){
                    temp = temp + contacts.get(position) + "\n";
                }
                if(trainers.get(position).length() > 1){
                    temp = temp + trainers.get(position) + "\n";
                }
                if(appointments.get(position).length() > 1){
                    temp = temp + appointments.get(position) + "\n";
                }
                if(beachstaff.get(position).length() > 1){
                    temp = temp + beachstaff.get(position) + "\n";
                }
                if(specialRegi.get(position).length() > 1){
                    temp = temp + specialRegi.get(position) + "\n";
                }
                if(temp.length() > 1){
                    temp = temp.substring(0, temp.length() - 1);;
                }
                if(websites.get(position).length() > 1){
                    temp2 = websites.get(position) + "\n";
                }
                intent.putExtra("numbers", temp);
                intent.putExtra("moreInfo", temp2+moreInfo.get(position));
                intent.putExtra("rating",rating.get(position));
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
                return true;
            case R.id.logIn:
                createNewContactDialogLogin();
                return true;
            case R.id.map:
                Toast.makeText(this, "kartta painettu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.profile:
                Toast.makeText(this, mAuth.getUid(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logOut:
                logOut();
                return true;
            case R.id.search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createNewContactDialogLogin(){
        dialogBuilderLogin = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        user = (EditText) contactPopupView.findViewById(R.id.user);
        passwordText = (EditText) contactPopupView.findViewById(R.id.password);
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
                String email = user.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    user.setError("Anna sahkoposti");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    passwordText.setError("Anna salasana");
                    return;
                }
                if (mAuth.getCurrentUser() == null) {
                    logIn(email, password);
                }else{
                    Toast.makeText(sportList.this, "Already logged in", Toast.LENGTH_SHORT).show();
                }
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
    public void logOut(){
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            Toast.makeText(sportList.this, "Sign-out successful", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(sportList.this, "Not logged in", Toast.LENGTH_SHORT).show();
        }
    }
    public void logIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password) .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    dialog.dismiss();
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(sportList.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    // updateUI(null);
                }

                // ...
            }
        });
    }
    public void updateUI(FirebaseUser user){
        String s = user.getEmail()+" Logged in";
        Toast.makeText(sportList.this, s, Toast.LENGTH_SHORT).show();
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
package com.example.myapplication;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import static android.content.ContentValues.TAG;

public class Intro  extends AppCompatActivity {
    AsyncHttpClient client;
    Workbook workbook;
    private static Context context;
    ArrayList<String> titles, addresses, cities, sports, websites, regi, managers, supervisor,superhumans, contacts, trainers, appointments, beachstaff, specialRegi, moreInfo;
    List<LatLng> coordinates;
    LocationManager locationManager;
    protected void onCreate(android.os.Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( (Activity) this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            recreate();
        } else {
            super.onCreate(savedInstanceState);
            setTheme(R.style.AppTheme_NoActionBar);
            setContentView(R.layout.activity_intro);
            Intro.context = getApplicationContext();
            client = new AsyncHttpClient();
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
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("liikuntapaikat");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        titles.add(data.child("title").getValue().toString());
                        addresses.add(data.child("address").getValue().toString());
                        cities.add(data.child("city").getValue().toString());
                        websites.add(data.child("website").getValue().toString());
                        sports.add(data.child("sports").getValue().toString());
                        regi.add(data.child("regi").getValue().toString());
                        managers.add(data.child("manager").getValue().toString());
                        supervisor.add(data.child("supervisor").getValue().toString());
                        superhumans.add(data.child("superhuman").getValue().toString());
                        contacts.add(data.child("contact").getValue().toString());
                        trainers.add(data.child("trainer").getValue().toString());
                        appointments.add(data.child("appointment").getValue().toString());
                        beachstaff.add(data.child("beachstaff").getValue().toString());
                        specialRegi.add(data.child("specialregi").getValue().toString());
                        moreInfo.add(data.child("moreInfo").getValue().toString());
                    }
                    Bundle bun = new Bundle();
                    bun.putStringArrayList("titles", titles);
                    bun.putStringArrayList("addresses", addresses);
                    bun.putStringArrayList("cities", cities);
                    bun.putStringArrayList("websites", websites);
                    bun.putStringArrayList("sports", sports);
                    bun.putStringArrayList("regi", regi);
                    bun.putStringArrayList("managers", managers);
                    bun.putStringArrayList("supervisor", supervisor);
                    bun.putStringArrayList("superhumans", superhumans);
                    bun.putStringArrayList("contacts", contacts);
                    bun.putStringArrayList("trainers", trainers);
                    bun.putStringArrayList("appointments", appointments);
                    bun.putStringArrayList("beachstaff", beachstaff);
                    bun.putStringArrayList("specialRegi", specialRegi);
                    bun.putStringArrayList("moreinfo", moreInfo);
                    Intent intent = new Intent(context.getApplicationContext(), sportList.class);
                    intent.putExtra("bundl", bun);
                    startActivity(intent);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });

            /*String url = "https://github.com/jage97/InnovaatioProjekti/blob/master/toimikkojooko.xls?raw=true";
            client.get(url, new FileAsyncHttpResponseHandler(context) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File
                        file) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    WorkbookSettings ws = new WorkbookSettings();
                    ws.setEncoding("windows-1252");
                    ws.setGCDisabled(true);

                    Bundle bun = new Bundle();
                    if (file != null) {
                        try {
                            workbook = workbook.getWorkbook(file, ws);
                            //db.collection("sportList").document("sportList").set(workbook);
                            Sheet sheet = workbook.getSheet(0);

                            for (int i = 1; i < sheet.getRows(); i++) {
                                Cell[] row = sheet.getRow(i);
                                for (int s = 0; s < row.length; s++) {
                                    if (row[s].getType() == null) {
                                    }
                                }
                                titles.add(row[0].getContents());
                                addresses.add(row[1].getContents());
                                cities.add(row[2].getContents());
                                websites.add("Verkkosivut: " + row[3].getContents() + "\n");
                                sports.add(row[4].getContents());
                                regi.add("Kassa: " + row[5].getContents() + "\n");
                                managers.add("Liikuntapaikanhoitajat: " + row[6].getContents() + "\n");
                                supervisor.add("Tiimiesimies: " + row[7].getContents() + " " + row[8].getContents());
                                moreInfo.add(row[9].getContents());
                            }
                            bun.putStringArrayList("titles", titles);
                            bun.putStringArrayList("addresses", addresses);
                            bun.putStringArrayList("cities", cities);
                            bun.putStringArrayList("websites", websites);
                            bun.putStringArrayList("sports", sports);
                            bun.putStringArrayList("regi", regi);
                            bun.putStringArrayList("managers", managers);
                            bun.putStringArrayList("supervisor", supervisor);
                            bun.putStringArrayList("moreinfo", moreInfo);
                            Intent intent = new Intent(context.getApplicationContext(), sportList.class);
                            intent.putExtra("bundl", bun);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (BiffException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });*/
        }
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

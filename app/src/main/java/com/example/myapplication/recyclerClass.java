package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import static android.content.ContentValues.TAG;
import static java.lang.Double.parseDouble;

public class recyclerClass extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mMapView;
    String address;
    String idchild;
    Geocoder coder;
    Adapter adapter;
    private AlertDialog dialog, dialogInfo;
    private EditText user, passwordText, rate;
    private Button loginButton, exitButton, rekButton, rateButton;
    private AlertDialog.Builder dialogBuilderLogin, dialogBuilderInfo;
    FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        mAuth = FirebaseAuth.getInstance();

        TextView textTitle = findViewById(R.id.textView);
        TextView textAddress = findViewById(R.id.textView4);
        TextView textSport = findViewById(R.id.textView8);
        TextView textNumbers = findViewById(R.id.textView10);
        TextView textMoreInfo = findViewById(R.id.textView11);
        ImageView icon1 = findViewById(R.id.imageView1);
        ImageView icon2 = findViewById(R.id.imageView2);
        ImageView icon3 = findViewById(R.id.imageView3);
        ImageView icon4 = findViewById(R.id.imageView4);
        ImageView icon5 = findViewById(R.id.imageView5);
        Bundle mapViewBundle = null;
        coder = new Geocoder(this);
        String title = "N/A";
        address = "N/A";
        String sport = "N/A";
        String numbers = "N/A";
        String moreInfo = "N/A";
        idchild = "0";
        Double rating = 0.0;
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            title = extras.getString("title");
            address = extras.getString("address");
            sport = extras.getString("sport");
            numbers = extras.getString("numbers");
            moreInfo = extras.getString("moreInfo");
            idchild = extras.getString("idchild");
            try {
            rating = parseDouble(extras.getString("rating"));
            } catch (NumberFormatException e){

            };
        }
        double p = rating;
        ArrayList<ImageView> icons = new ArrayList<>();
        icons.add(icon1);
        icons.add(icon2);
        icons.add(icon3);
        icons.add(icon4);
        icons.add(icon5);
        for (ImageView icon : icons) {
            if (p > 1) {
                icon.setImageResource(R.drawable.full);
                p = p - 2;
            } else if (p == 1) {
                icon.setImageResource(R.drawable.half);
                p = p - 1;
            } else {
                icon.setImageResource(R.drawable.gray);
            }
        }
      //  };
        textTitle.setText(title);
        textAddress.setText(address);
        textSport.setText(sport);
        textNumbers.setText(numbers);
        textMoreInfo.setText(moreInfo);

        mMapView = (MapView) findViewById(R.id.mapView);
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MAPS_API_KEY");
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);


/*        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(savedInstanceState.maps_api_key);
        }*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rating, menu);
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
            case R.id.profile:
                Toast.makeText(this, mAuth.getUid(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logOut:
                logOut();
                return true;
            case R.id.rating:
                createNewContactDialogRate();
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
                    Toast.makeText(recyclerClass.this, "Already logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Register
                dialog.dismiss();
                Intent intentRegister = new Intent(recyclerClass.this, RegisterActivity.class);
                startActivity(intentRegister);
            }
        });
    }
    public void createNewContactDialogRate(){
        dialogBuilderLogin = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.activity_rate, null);
        rate = (EditText) contactPopupView.findViewById(R.id.ratetext);
        rateButton = (Button) contactPopupView.findViewById(R.id.rateButton);
        exitButton = (Button) contactPopupView.findViewById(R.id.backButton);

        dialogBuilderLogin.setView(contactPopupView);
        dialog = dialogBuilderLogin.create();
        dialog.show();

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LogIn
                String rateString = rate.getText().toString().trim();
                if (mAuth.getCurrentUser() == null) {
                    Toast.makeText(recyclerClass.this, "not logged in", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    try {
                        if (TextUtils.isEmpty(rateString) || parseDouble(rateString) > 10 || parseDouble(rateString) < 0) {
                            rate.setError("Anna luku 0-10");
                            return;
                        } else {
                            Toast.makeText(recyclerClass.this, "Updated database", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("liikuntapaikat");
                            myRef.child(idchild).child("rating").setValue(rateString);
                            dialog.dismiss();
                        }
                    } catch (NumberFormatException e) {
                        return;
                    }
                }
            }
        });
    }
    public void logOut(){
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            Toast.makeText(recyclerClass.this, "Sign-out successful", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(recyclerClass.this, "Not logged in", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(recyclerClass.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    // updateUI(null);
                }

                // ...
            }
        });
    }
    public void rate(String rateString){
        dialog.dismiss();
    }
    public void updateUI(FirebaseUser user){
        String s = user.getEmail()+" Logged in";
        Toast.makeText(recyclerClass.this, s, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle("MAPS_API_KEY");
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle("MAPS_API_KEY", mapViewBundle);
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
        LatLng locati = null;
        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MyGeocoder codery = new MyGeocoder();
        List<Address> loca = codery.getFromLocationName(address + " Finland", 2);
        if(loca.size() > 1) {
            locati = new LatLng((double) (loca.get(0).getLatitude()),
                    (double) (loca.get(0).getLongitude()));
            Log.e("TAG", loca + "thisIsShit");
        }
        if (locati != null) {
            map.addMarker(new MarkerOptions().position(locati));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(locati, 12.0f));
        }*/
        locati = new LatLng((double) (0),
                (double) (0));
        map.addMarker(new MarkerOptions().position(locati));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(locati, 12.0f));
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

        List<Address> address;
        LatLng p1 = null;
        Log.e("TAG",strAddress);

        try {

            address = coder.getFromLocationName(strAddress,1);

            if (address==null) {

                return null;
            }
            Address location=address.get(0);


            //Log.e("TAG","JUMALAUTA :" + location.getLatitude() + " , "+ location.getLongitude());
            p1 = new LatLng((double) (location.getLatitude() ),
                    (double) (location.getLongitude()));

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

}
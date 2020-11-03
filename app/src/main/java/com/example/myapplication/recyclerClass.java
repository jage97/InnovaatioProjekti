package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class recyclerClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        TextView textTitle = findViewById(R.id.textView);
        TextView textAddress = findViewById(R.id.textView4);
        TextView textSport = findViewById(R.id.textView8);
        TextView textNumbers = findViewById(R.id.textView10);
        TextView textMoreInfo = findViewById(R.id.textView11);

        String title = "N/A";
        String address = "N/A";
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

    }
}
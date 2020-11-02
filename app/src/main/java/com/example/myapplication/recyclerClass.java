package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class recyclerClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupinfo);
        TextView textTitle = findViewById(R.id.textTitle);

        String title = "N/A";

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            title = extras.getString("title");
        }

        textTitle.setText(title);
    }
}
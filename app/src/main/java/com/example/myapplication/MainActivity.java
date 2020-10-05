package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button buttonA2;
    private Button buttonPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonA2 = (Button) findViewById(R.id.buttonA2);
        buttonA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        buttonPic = (Button) findViewById(R.id.buttonPic);
        buttonPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityPic();
            }
        });

    }
    public void openActivity2(){
        Intent intent2 = new Intent(this, MainActivity2.class);
        startActivity(intent2);
    }
    public void openActivityPic(){
        Intent intent2 = new Intent(this, PicActivity.class);
        startActivity(intent2);
    }
}
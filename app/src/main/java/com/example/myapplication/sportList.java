package com.example.myapplication;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Workbook;
import jxl.Sheet;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class sportList extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    AsyncHttpClient client;
    Workbook workbook;
    List<String> titles, addresses, cities, sports;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText kayttaja, salasana;
    private Button kirjauduButton, exitButton, rekButton;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportlist);

        String url = "https://github.com/jage97/InnovaatioProjekti/blob/new-master/sportPlaces.xls?raw=true";

        recyclerView = findViewById(R.id.listOfSport);

        titles = new ArrayList<>();
        addresses = new ArrayList<>();
        cities = new ArrayList<>();
        sports = new ArrayList<>();

        client = new AsyncHttpClient();
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

                        };
                        showData();
                        Log.d("TAG","onSuccess: "+titles);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void showData() {
        adapter = new Adapter(this, titles, addresses, cities, sports);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
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
            case R.id.kayttaja:
                Toast.makeText(this, "kayttaja painettu", Toast.LENGTH_SHORT).show();
                createNewContactDialog();
                return true;
            case R.id.rajaus:
            Toast.makeText(this, "rajaa painettu", Toast.LENGTH_SHORT).show();
            return true;
            case R.id.kartta:
                Toast.makeText(this, "kartta painettu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.rajaus1:
                Toast.makeText(this, "rajaa 1 painettu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.rajaus2:
                Toast.makeText(this, "rajaa 2 painettu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.rajaus3:
                Toast.makeText(this, "rajaa 3 painettu", Toast.LENGTH_SHORT).show();
                return true;

                default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void createNewContactDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        kayttaja = (EditText) contactPopupView.findViewById(R.id.kayttaja);
        salasana = (EditText) contactPopupView.findViewById(R.id.salasana);
        kirjauduButton = (Button) contactPopupView.findViewById(R.id.kirjauduButton);
        exitButton = (Button) contactPopupView.findViewById(R.id.exitButton);
        rekButton = (Button) contactPopupView.findViewById(R.id.rekButton);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        kirjauduButton.setOnClickListener(new View.OnClickListener() {
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

}

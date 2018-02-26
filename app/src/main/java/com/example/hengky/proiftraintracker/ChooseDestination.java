package com.example.hengky.proiftraintracker;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class ChooseDestination extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 10;
    Button buttonMap, buttonGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_destination);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.argo_wilis);
        textView.setText(message);

        String[]list_stasiun = getResources().getStringArray(R.array.list_stasiun_argo_wilis);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner_start_wilis);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_stasiun);
        spinner1.setAdapter(adapter);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_end_wilis);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_stasiun);
        spinner2.setAdapter(adapter2);
        reqPermission();

        buttonMap = (Button) findViewById(R.id.btnOpenMap);
        buttonGo = (Button) findViewById(R.id.btn_go);

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap(view);

            }
        });
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToTripPage(view);
            }
        });
    }

    private void reqPermission(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            );
        }
        else{
            accessLocation();
        }
    }

    private void accessLocation() {
        Toast.makeText(this, "Allow Apps to Access Location", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    accessLocation();
                }
                else{
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)){
                        new AlertDialog.Builder(this).
                                setTitle("Access Location permission").
                                setMessage("You need to grant access location permission" +
                                        "to use this feature. Please retry and grant the permission.").show();
                    }
                    else {
                        new AlertDialog.Builder(this).
                                setTitle("Access Location permission dennied").setMessage("You dennied" +
                                "access location permission. So, the feature will be disabled." +
                                "To enable it, go on setting -> app -> Train Tracker and grant location service permission").show();
                    }
                }

                break;
        }
    }

    public void moveToTripPage(View view){
        Intent intent = new Intent(this, OnProgress.class);
        startActivity(intent);
    }

    public void openMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
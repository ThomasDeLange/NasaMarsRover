package com.example.thomas.nasamarsroverphotoapp.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.thomas.nasamarsroverphotoapp.R;
import com.example.thomas.nasamarsroverphotoapp.api.OnRoverPhotoAvailable;
import com.example.thomas.nasamarsroverphotoapp.api.RoverPhotoTask;
import com.example.thomas.nasamarsroverphotoapp.database.RoverPhotoDatabase;
import com.example.thomas.nasamarsroverphotoapp.domain.RoverPhoto;
import com.example.thomas.nasamarsroverphotoapp.util.RoverPhotoAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnRoverPhotoAvailable, AdapterView.OnItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static ArrayList<String> roverPhotoCameraNames = null;
    private ArrayList<RoverPhoto> roverPhotos;
    private ListView listView;
    public RoverPhotoAdapter roverPhotoAdapter;
    private RoverPhotoTask roverPhotoTask;
    private RoverPhotoDatabase roverPhotoDatabase;
    private Spinner spinner;
    public ArrayAdapter<String> spinnerAdapter;
    private Object lock = new Object();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: executed");

        //Connect met de activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Maak nieuwe database aan
        roverPhotoDatabase = new RoverPhotoDatabase(getApplicationContext());
        Log.d(TAG, roverPhotoDatabase.toString());


        /*
        Setup Database & ArrayList
         */

        //Maak een nieuwe ArrayList waar alle RoverPhotos in gezet worden
        //Via api key of via de database
        this.roverPhotos = new ArrayList<>();

        //Als er foto's in de database zijn zet die dan in de arraylist
        this.roverPhotos = roverPhotoDatabase.getAllRoverPhotos();
        Log.i(TAG, "onCreate: roverPhotos length from database: " + roverPhotos.size());

        //als de Arraylist leeg is is de database dus ook leeg vul de arraylist en database met de data uit de api
        if (roverPhotos.isEmpty()) {

            //Roep de RoverPhotoTask aan die de arraylist en de database vult met de informatie uit de api
            this.roverPhotoTask = new RoverPhotoTask(this);
            this.roverPhotoTask.execute("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=oj4xN5d5cHA5fbKZ3tIarICSbgGhZ7l7tjZOw6DJ");
            Log.i(TAG, "onCreate: roverPhotos length after filling from api : " + roverPhotos.size());

        }

        //Op dit moment is de Arraylist en de Database gevuld met data
        Log.d(TAG, "onCreate: roverPhotos length full: " + roverPhotos.size());
        Log.d(TAG, roverPhotos.toString());

        /*
        Setup de spinner
         */

        this.spinner = (Spinner) findViewById(R.id.roverPhotoCameraNamesSpinnerView);

        roverPhotoCameraNames = roverPhotoDatabase.getRoverPhotoCameraNames();
        roverPhotoCameraNames.add(0, "All cameras");
        spinnerAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, roverPhotoCameraNames);
        this.spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();

        this.spinner.setOnItemSelectedListener(this);

        /*
        Setup de listview
         */

        this.listView = (ListView) findViewById(R.id.roverPhotoListView);

        Log.d("listView", "onCreate: " + listView.toString());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("SelectedItem: ", i + "");

                RoverPhoto roverPhoto = (RoverPhoto) roverPhotos.get(i);

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("roverPhoto", roverPhoto);
                startActivity(intent);
            }
        });

        //Zet de data in de listview door via een adapter de data aan de listview te koppelen
        this.roverPhotoAdapter = new RoverPhotoAdapter(this, roverPhotos);
        this.listView.setAdapter(roverPhotoAdapter);

        //Notify de listview dat de data is veranderd
        //this.roverPhotoAdapter.notifyDataSetChanged();
        //this.spinnerAdapter.notifyDataSetChanged();

        Log.i(TAG, "onCreate: Database Rows = " + roverPhotoDatabase.countDatabaseRows());

    }
    @Override
    public void onRoverPhotoAvailable(RoverPhoto roverPhoto) {
        this.roverPhotos.add(roverPhoto);
        roverPhotoDatabase.addRoverPhoto(roverPhoto);
        this.roverPhotoAdapter.notifyDataSetChanged();

        roverPhotoCameraNames = roverPhotoDatabase.getRoverPhotoCameraNames();
        roverPhotoCameraNames.add(0, "All cameras");
        spinnerAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, roverPhotoCameraNames);
        this.spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();


    }

    public void updateData() {

        roverPhotos.clear();

        roverPhotos = roverPhotoDatabase.getAllRoverPhotos();

        this.roverPhotoAdapter = new RoverPhotoAdapter(this, roverPhotos);
        this.listView.setAdapter(roverPhotoAdapter);

        Log.d(TAG, "updateData: roverPhotos: " + roverPhotos.toString());
        Log.d(TAG, "onCreate: roverPhotos length full: " + roverPhotos.size());

    }

    public void updateDataWithCameraName(String cameraName) {
        roverPhotos.clear();

        roverPhotos = roverPhotoDatabase.getAllRoverPhotosWithCameraName(cameraName);

        this.roverPhotoAdapter = new RoverPhotoAdapter(this, roverPhotos);
        this.listView.setAdapter(roverPhotoAdapter);

        Log.d(TAG, "updateData: roverPhotos: " + roverPhotos.toString());
        Log.d(TAG, "onCreate: roverPhotos length full: " + roverPhotos.size() + " for camera name: " + cameraName);

        roverPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:
                updateData();
                break;
            case 1:
                updateDataWithCameraName((String) spinner.getSelectedItem());
                break;
            case 2:
                updateDataWithCameraName((String) spinner.getSelectedItem());
                break;
            case 3:
                updateDataWithCameraName((String) spinner.getSelectedItem());
                break;
            case 4:
                updateDataWithCameraName((String) spinner.getSelectedItem());
                break;
            case 5:
                updateDataWithCameraName((String) spinner.getSelectedItem());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

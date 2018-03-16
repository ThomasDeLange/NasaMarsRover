package com.example.thomas.nasamarsroverphotoapp.controllers;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.nasamarsroverphotoapp.R;
import com.example.thomas.nasamarsroverphotoapp.domain.RoverPhoto;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private RoverPhoto roverPhoto;
    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "onCreate: executed");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();

        roverPhoto = (RoverPhoto) extras.getSerializable("roverPhoto");

        ImageView roverPhotoView = (ImageView) findViewById(R.id.detailRoverPhotoView);
        TextView roverPhotoCameraFullNameView = (TextView) findViewById(R.id.fullCameraNameView);

        Log.i("RoverPhoto", "onCreate: " + roverPhoto.toString());

        Picasso.get().load(roverPhoto.getImgUrl()).into(roverPhotoView);
        roverPhotoCameraFullNameView.setText(roverPhoto.getFullName());

    }
}

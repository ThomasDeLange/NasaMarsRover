package com.example.thomas.nasamarsroverphotoapp.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.nasamarsroverphotoapp.R;
import com.example.thomas.nasamarsroverphotoapp.domain.RoverPhoto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by thomas on 13-03-18.
 */

public class RoverPhotoAdapter extends ArrayAdapter<RoverPhoto> {

    private static final String TAG = "RoverPhotoTask";

    public RoverPhotoAdapter(@NonNull Context context, ArrayList<RoverPhoto> roverPhotoArrayList) {
        super(context, 0, roverPhotoArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i(TAG, "getView: executed");

        // Photo ophalen
        RoverPhoto roverPhoto = getItem(position);

        // View aanmaken of herbruiken
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.rover_photo_listview_item,
                    parent,
                    false
            );
        }

        // Koppelen datasource aan UI
        ImageView roverPhotoView = (ImageView) convertView.findViewById(R.id.roverPhotoView);
        TextView roverPhotoImgIdView = (TextView) convertView.findViewById(R.id.roverPhotoImgIdView);

        Picasso.get().load(roverPhoto.getImgUrl()).into(roverPhotoView);

        roverPhotoImgIdView.setText("Image id: " + roverPhoto.getImgId());

        return convertView;

    }

}

package com.example.thomas.nasamarsroverphotoapp.api;

import android.widget.SpinnerAdapter;

import com.example.thomas.nasamarsroverphotoapp.domain.RoverPhoto;

import java.util.ArrayList;

/**
 * Created by thomas on 13-03-18.
 */

public interface OnRoverPhotoAvailable {
    void onRoverPhotoAvailable(RoverPhoto roverPhoto);
}

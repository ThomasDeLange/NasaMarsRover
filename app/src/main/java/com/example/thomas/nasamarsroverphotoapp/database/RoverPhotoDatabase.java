package com.example.thomas.nasamarsroverphotoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thomas.nasamarsroverphotoapp.domain.RoverPhoto;

import java.util.ArrayList;

/**
 * Created by thomas on 13-03-18.
 */

public class RoverPhotoDatabase extends SQLiteOpenHelper {

    private final static String TAG = "RoverPhotoDatabase";
    private final static String DB_NAME = "RoverPhoto.db";
    private final static int DB_VERSION = 33;
    private final static String TABLE_NAME = "RoverPhoto";

    private final static String COLUMN_PHOTO_URL = "PhotoUrl";
    private final static String COLUMN_CAMERA_FULL_NAME = "CameraFullName";
    private final static String COLUMN_PHOTO_ID_ = "PhotoId";

    public RoverPhotoDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE `" + TABLE_NAME + "` (" +
                        "`" + COLUMN_PHOTO_ID_ + "` TEXT NOT NULL," +
                        "`" + COLUMN_CAMERA_FULL_NAME + "` TEXT NOT NULL," +
                        "`" + COLUMN_PHOTO_URL + "` TEXT NOT NULL," +
                        "PRIMARY KEY(`" + COLUMN_PHOTO_ID_ + "`)" +
                        ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "onUpgrade: upgrading database.");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    public void addRoverPhoto(RoverPhoto roverPhoto) {
        Log.i(TAG, "addRoverPhoto aangeroepen.");

        ContentValues values = new ContentValues();
        values.put(COLUMN_PHOTO_URL, roverPhoto.getImgUrl());
        values.put(COLUMN_PHOTO_ID_, roverPhoto.getImgId());
        values.put(COLUMN_CAMERA_FULL_NAME, roverPhoto.getFullName());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public ArrayList<RoverPhoto> getAllRoverPhotos() {
        Log.i(TAG, "getAllPersons aangeroepen.");

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<RoverPhoto> roverPhotoArrayList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String photoUrl = cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_URL));
            String photoId = cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_ID_));
            String cameraFullName = cursor.getString(cursor.getColumnIndex(COLUMN_CAMERA_FULL_NAME));

            roverPhotoArrayList.add(new RoverPhoto(photoUrl, photoId, cameraFullName));
            cursor.moveToNext();
        }

        db.close();
        return roverPhotoArrayList;
    }

    public ArrayList<RoverPhoto> getAllRoverPhotosWithCameraName(String cameraName) {
        Log.i(TAG, "getAllPersons aangeroepen.");

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CAMERA_FULL_NAME + " = '" + cameraName + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<RoverPhoto> roverPhotoArrayList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String photoUrl = cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_URL));
            String photoId = cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_ID_));
            String cameraFullName = cursor.getString(cursor.getColumnIndex(COLUMN_CAMERA_FULL_NAME));

            roverPhotoArrayList.add(new RoverPhoto(photoUrl, photoId, cameraFullName));
            cursor.moveToNext();
        }

        db.close();
        return roverPhotoArrayList;
    }

    public void printRoverPhotos() {

        Log.i(TAG, "printRoverPhotos: " + getAllRoverPhotos().toString());
    }

    public ArrayList<String> getRoverPhotoCameraNames() {
        Log.i(TAG, "getAllRoverPhotoCameraNames aangeroepen.");

        String query = "SELECT DISTINCT " + COLUMN_CAMERA_FULL_NAME + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<String> roverPhotoCameraNames = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String cameraFullName = cursor.getString(cursor.getColumnIndex(COLUMN_CAMERA_FULL_NAME));

            roverPhotoCameraNames.add(cameraFullName);
            cursor.moveToNext();
        }
        return roverPhotoCameraNames;
    }

    public int countDatabaseRows(){
        Log.i(TAG, "getAllRoverPhotoCameraNames aangeroepen.");

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int count;
        count = cursor.getCount();
        return count;
    }


}


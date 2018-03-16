package com.example.thomas.nasamarsroverphotoapp.api;

import android.os.AsyncTask;
import android.util.Log;

import com.example.thomas.nasamarsroverphotoapp.domain.RoverPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by thomas on 13-03-18.
 */

public class RoverPhotoTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "RoverPhotoTask";
    private OnRoverPhotoAvailable listener = null;


    public RoverPhotoTask(OnRoverPhotoAvailable listener) {
        this.listener = listener;
    }

    
    //
    // convert InputStream to String
    //
    private static String getStringFromInputStream(InputStream is) {

        Log.i(TAG, "getStringFromInputStream : executed");

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    /**
     * doInBackground is de methode waarin de aanroep naar een service op
     * het Internet gedaan wordt.
     *
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {

        Log.i(TAG, "doInBackground : executed");


        InputStream inputStream = null;
        int responsCode = -1;
        // De URL die we via de .execute() meegeleverd krijgen
        String personUrl = params[0];
        // Het resultaat dat we gaan retourneren
        String response = "";

        try {
            // Maak een URL object
            URL url = new URL(personUrl);

            Log.i(TAG, "URL : " + url);

            // Open een connection op de URL
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            // Initialiseer een HTTP connectie
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");

            // Voer het request uit via de HTTP connectie op de URL
            httpConnection.connect();

            // Kijk of het gelukt is door de response code te checken
            responsCode = httpConnection.getResponseCode();
            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                // Log.i(TAG, "doInBackground response = " + response);
            } else {
                Log.e(TAG, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e(TAG, "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }

        // Hier eindigt deze methode.
        // Het resultaat gaat naar de onPostExecute methode.
        return response;
    }

    /**
     * onPostExecute verwerkt het resultaat uit de doInBackground methode.
     *
     * @param response
     */
    protected void onPostExecute(String response) {

        Log.i(TAG, "onPostExecute " + response);

        // Check of er een response is
        if (response == null || response == "") {
            Log.e(TAG, "onPostExecute kreeg een lege response!");
            return;
        }

        // Het resultaat is in JSON formaat.
        // Daar moeten we de info die we willen tonen uit filteren (parsen).
        // Dat kan met een JSONObject.
        JSONObject jsonObject;
        try {
            // Top level json object
            jsonObject = new JSONObject(response);

            // Get all roverPhotos and start looping

            //Parse door JSON
            JSONArray jsonRoverPhotos = jsonObject.getJSONArray("photos");
            for (int i = 0; i < jsonRoverPhotos.length(); i++) {

                JSONObject jsonRoverPhoto = jsonRoverPhotos.getJSONObject(i);

                String roverPhotoId = jsonRoverPhoto.getString("id");

                JSONObject roverPhotoCamera = jsonRoverPhoto.getJSONObject("camera");
                String roverPhotoCameraFullName = roverPhotoCamera.getString("full_name");

                String roverPhotoImgUrl = jsonRoverPhoto.getString("img_src");

                RoverPhoto roverPhoto = new RoverPhoto(roverPhotoImgUrl, roverPhotoId, roverPhotoCameraFullName);

                // call back with new roverPhoto data
                //
                listener.onRoverPhotoAvailable(roverPhoto);
                Log.i(TAG, "roverPhoto stored: " + roverPhoto.toString());

            }
        } catch (JSONException ex) {
            Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
    }
}




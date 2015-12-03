package edu.virginia.cs.cs4720.ispy;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.virginia.cs.cs4720.ispy.DBHelper;

/**
 * Created by john on 9/29/15.
 */
public class PictureList extends ListActivity implements AdapterView.OnItemSelectedListener {
    DBHelper myDb;
    SpyPictureAdapter myAdapter;
    ArrayList<SpyPicture> pictures;
    ArrayList<SpyPicture> filteredPictures;
    GPS gps;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        Log.d("pictureList", "test startup");
        final Context that = this;

        // set up spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // link up listener
        spinner.setOnItemSelectedListener(this);

        // populate list view
        myDb = new DBHelper(this);
        filteredPictures = new ArrayList<SpyPicture>();
        pictures = new ArrayList<SpyPicture>();

        //get pictures from DB
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Picture");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> pictureList, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + pictureList.size() + " scores");
                    for (int i = 0; i < pictureList.size(); i++) {
                        ParseObject f = pictureList.get(i);
                        ParseFile file = f.getParseFile("image");
                        try {
                            byte[] data = file.getData();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            SpyPicture pic = new SpyPicture(f.getObjectId(), f.getString("path"), f.getString("color"), f.getNumber("x").floatValue(), f.getNumber("y").floatValue(), f.getNumber("latitude").doubleValue(), f.getNumber("longitude").doubleValue(), bitmap);
                            pictures.add(pic);
                            Log.d("Picture", pictures.get(i).toString());
                        } catch (com.parse.ParseException error) {

                        }

                    }

                    for (SpyPicture picture : pictures) {
                        filteredPictures.add(picture);
                    }

                    myAdapter.notifyDataSetChanged();


                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        myAdapter = new SpyPictureAdapter(that, R.layout.picture_card, filteredPictures);
        setListAdapter(myAdapter);
        // attach adapter


        gps = new GPS(this);

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Toast.makeText(getApplicationContext(), "ID: " + id, Toast.LENGTH_LONG).show();
        Intent i = new Intent(PictureList.this, GuessActivity.class);
        String newId = pictures.get(position).getId();
        i.putExtra("id", newId);
        startActivity(i);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String option = (String) parent.getItemAtPosition(pos);
        filterPictures(option);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void filterPictures(String option) {

        Log.d("pictureList", "triggered filterPictures");
        // use all pictures
        if (option.equals("Any")) {
            Log.d("pictureList", "option any");
            filteredPictures.clear();
            for (SpyPicture picture : pictures) {
                filteredPictures.add(picture);
            }
            myAdapter.notifyDataSetChanged();
        } else {
            Log.d("pictureList", "option: " + option);
            double miles = Double.parseDouble(option);
            double latitude;
            double longitude;

            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                // testing locations
//                latitude = 38.0384711;
//                longitude = -78.4896637;

            } else {
                gps.showSettingsAlert();
                return;
            }
            filteredPictures.clear();
            for (SpyPicture picture : pictures) {
                double distance = distance(latitude, longitude, picture.getLatitude(), picture.getLongitude());
                if (distance <= miles) {
                    Log.d("pictureList", "distance checks out: " + distance);
                    filteredPictures.add(picture);
                }
            }
            myAdapter.notifyDataSetChanged();
        }
    }

    // gets the distance between two gps coordinates in miles
    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}


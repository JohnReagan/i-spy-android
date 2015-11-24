package edu.virginia.cs.cs4720.ispy;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;


/**
 * Created by john on 10/11/15.
 */
public class GuessActivity extends Activity {
    DBHelper myDb;
    SpyPicture picture;
    int guesses;

    public void update(int x) {
        String str = "Total guesses: "+x;
        TextView guessText = (TextView)findViewById(R.id.guessText);
        guessText.setText(str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_guess);
        myDb = new DBHelper(this);
        String id = intent.getStringExtra("id");
        final TextView textView = (TextView) findViewById(R.id.textView);

        if (id == "") {
            textView.setText("Could not get id from intent.");
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Picture");
            //Cursor cursor = myDb.getPicture(id);
            query.getInBackground(id, new GetCallback<ParseObject>() {
                public void done(ParseObject object, com.parse.ParseException e) {
                    if (e == null) {
                        //success
                        picture = new SpyPicture(object.getObjectId(),
                                object.getString("path"),
                                object.getString("color"),
                                object.getNumber("x").floatValue(),
                                object.getNumber("y").floatValue(),
                                object.getNumber("latitude").doubleValue(),
                                object.getNumber("longitude").doubleValue());
                        String path = picture.getPath();
                        Bitmap bitmap = null;
                        //Bitmap thumbnail = null;
                        ImageView imageView = (ImageView) findViewById(R.id.showImage);

                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

                        bitmap = BitmapFactory.decodeFile(path, bmOptions);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
                        imageView.setImageBitmap(bitmap);

                        textView.setText("I spy something.... " + picture.getColor());
                        guesses = 0;
                        update(guesses);
                        imageView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                float x = event.getX();
                                float y = event.getY();
                                //finger placed on screen
                                Handler handle = new Handler();
                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                };
                                if (Math.abs(x - picture.getX()) < 20.00 && Math.abs(y - picture.getY()) < 20.00) {
                                    if (guesses == 0) {
                                        Toast.makeText(getApplicationContext(), "Congratulations, you got it on the first try!", Toast.LENGTH_LONG).show();
                                        handle.postDelayed(r, 5000);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Nice, you got it!", Toast.LENGTH_LONG).show();
                                        handle.postDelayed(r, 5000);
                                    }
                                } else {
                                    guesses++;
                                    update(guesses);
                                }
                                return false;
                            }
                        });
                    } else {
                        Log.d("Picture", "Error: " + e.getMessage());
                        textView.setText("picture could not be found.");
                    }
                }
            });
//            if (cursor !=  null) {
//                cursor.moveToFirst();
//                picture = new SpyPicture(cursor);
//                String path = picture.getPath();
//                Bitmap bitmap = null;
//                //Bitmap thumbnail = null;
//                ImageView imageView = (ImageView) findViewById(R.id.showImage);
//
//                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//
//                bitmap = BitmapFactory.decodeFile(path, bmOptions);
//                bitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
//                imageView.setImageBitmap(bitmap);
//
//                textView.setText("I spy something.... " + picture.getColor());
//                guesses = 0;
//                update(guesses);
//                imageView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        float x = event.getX();
//                        float y = event.getY();
//                        //finger placed on screen
//                        Handler handle = new Handler();
//                        Runnable r = new Runnable() {
//                            @Override
//                            public void run() {
//                                finish();
//                            }
//                        };
//                        if (Math.abs(x - picture.getX()) < 20.00 && Math.abs(y - picture.getY()) < 20.00) {
//                            if (guesses == 0) {
//                                Toast.makeText(getApplicationContext(), "Congratulations, you got it on the first try!", Toast.LENGTH_LONG).show();
//                                handle.postDelayed(r, 5000);
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Nice, you got it!", Toast.LENGTH_LONG).show();
//                                handle.postDelayed(r, 5000);
//                            }
//                        } else {
//                            guesses++;
//                            update(guesses);
//                        }
//                        return false;
//                    }
//                });
//            } else {
//                textView.setText("picture could not be found.");
//            }

        }


    }
}

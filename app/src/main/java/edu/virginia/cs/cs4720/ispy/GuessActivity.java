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
        long id = intent.getLongExtra("id", -1L);
        final TextView textView = (TextView) findViewById(R.id.textView);
//        textView.setText("ID: " + id);
        if (id == -1L) {
            textView.setText("Could not get id from intent.");
        } else {
            Cursor cursor = myDb.getPicture(id);
            if (cursor !=  null) {
                cursor.moveToFirst();
                picture = new SpyPicture(cursor);
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
                            Toast.makeText(getApplicationContext(), "Guess again: X = " + x + "Y= " + y, Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
            } else {
                textView.setText("picture could not be found.");
            }

        }


    }
}

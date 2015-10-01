package edu.virginia.cs.cs4720.ispy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by cole2 on 9/29/15.
 */
public class PhotoView extends Activity {
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_photo_view);
        if(intent.getStringExtra("img") != null) {
            String filename = intent.getStringExtra("img");
            ImageView myImage = (ImageView) findViewById(R.id.photoView);
            Bitmap bmp = BitmapFactory.decodeFile(filename);
            myImage.setImageBitmap(bmp);
            Log.d("Filename", filename);


            myImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //finger placed on screen
                            Toast.makeText(getApplicationContext(), "Finger placed at " + x + ", " + y, Toast.LENGTH_SHORT).show();
                            Log.d("Density", "" + getResources().getDisplayMetrics().density);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            //finger drag
                            Toast.makeText(getApplicationContext(), "Finger dragged", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return false;
                }
            });
        } else if (intent.getStringExtra("imgB") != null) {
            String filename = intent.getStringExtra("imgB");
            ImageView myImage = (ImageView) findViewById(R.id.photoView);
            Bitmap bmp = BitmapFactory.decodeFile(filename);
            myImage.setImageBitmap(bmp);
            Log.d("Filename", filename);
            myImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //finger placed on screen
                            Toast.makeText(getApplicationContext(), "Finger placed at " + x + ", " + y, Toast.LENGTH_SHORT).show();
                            Log.d("Density", "" + getResources().getDisplayMetrics().density);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            //finger drag
                            Toast.makeText(getApplicationContext(), "Finger dragged", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return false;
                }
            });
        }
        b = (Button)findViewById(R.id.backButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}

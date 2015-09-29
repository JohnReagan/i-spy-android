package edu.virginia.cs.cs4720.ispy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by cole2 on 9/29/15.
 */
public class PhotoView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String filename = intent.getStringExtra("img");
        setContentView(R.layout.activity_photo_view);
        ImageView myImage = (ImageView) findViewById(R.id.photoView);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(filename), 1200, 1700, true);
        myImage.setImageBitmap(bmp);
        Log.d("Filename", filename);


        myImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //finger placed on screen
                        Toast.makeText(getApplicationContext(), "Finger placed", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}

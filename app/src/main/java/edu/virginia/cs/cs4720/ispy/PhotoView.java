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

import java.io.File;

/**
 * Created by cole2 on 9/29/15.
 */
public class PhotoView extends Activity {

    protected void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ImageView myImage = (ImageView) findViewById(R.id.photoView);
        Intent intent = getIntent();
        String newStr = intent.getStringExtra("file");
        Log.d("StringTest", newStr);
        Toast.makeText(getApplicationContext(), newStr, Toast.LENGTH_SHORT).show();
        File imgFile = new File(newStr);
        Bitmap mb = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        myImage.setImageBitmap(mb);
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


}

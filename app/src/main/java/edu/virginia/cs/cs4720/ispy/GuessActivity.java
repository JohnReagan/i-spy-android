package edu.virginia.cs.cs4720.ispy;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by john on 10/11/15.
 */
public class GuessActivity extends Activity {
    DBHelper myDb;
    SpyPicture picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_guess);
        myDb = new DBHelper(this);

        long id = intent.getLongExtra("id", -1L);
        TextView textView = (TextView) findViewById(R.id.textView);
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
                bitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
                imageView.setImageBitmap(bitmap);
            } else {
                textView.setText("picture could not be found.");
            }

        }


    }
}

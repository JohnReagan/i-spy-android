package edu.virginia.cs.cs4720.ispy;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.File;

import edu.virginia.cs.cs4720.ispy.DBHelper;

/**
 * Created by john on 9/29/15.
 */
public class PictureList extends ListActivity{
    DBHelper myDb;
    SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // We'll define a custom screen layout here (the one shown above), but
        // typically, you could just use the standard ListActivity layout.
        setContentView(R.layout.activity_list);

        // Query for all people contacts using the Contacts.People convenience class.
        // Put a managed wrapper around the retrieved cursor so we don't have to worry about
        // requerying or closing it as the activity changes state.
        myDb = new DBHelper(this);


        displayList();
    }

    private void displayList () {
        Cursor cursor = myDb.getPictures();

        //desired columns to be bound
        String[] columns = new String[] {
            DBHelper.PICTURES_COLUMN_ID,
            DBHelper.PICTURES_COLUMN_PATH,
            DBHelper.PICTURES_COLUMN_PATH,
            DBHelper.PICTURES_COLUMN_COLOR,
            DBHelper.PICTURES_COLUMN_LATITUDE,
            DBHelper.PICTURES_COLUMN_LONGITUDE
        };

        //xml views data will bind to
        int[] to = new int[] {
                R.id.id,
                R.id.image,
                R.id.path,
                R.id.color,
                R.id.latitude,
                R.id.longitude
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter (
                this, R.layout.picture_card,
                cursor,
                columns,
                to,
                0);

        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue (View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.image) {
                    String path = cursor.getString(cursor.getColumnIndex(DBHelper.PICTURES_COLUMN_PATH));
                    Bitmap bitmap = null;
                    //Bitmap thumbnail = null;
                    ImageView imageView = (ImageView) view;

                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(path, bmOptions);
                    bitmap = Bitmap.createScaledBitmap(bitmap,100,100,true);
                    imageView.setImageBitmap(bitmap);

                    return true;
                }

                return false;
            }
        });

        setListAdapter(dataAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Toast.makeText(getApplicationContext(), "ID: " + id, Toast.LENGTH_LONG).show();
        Intent i = new Intent(PictureList.this, GuessActivity.class);
        i.putExtra("id", id);
        startActivity(i);
    }
}


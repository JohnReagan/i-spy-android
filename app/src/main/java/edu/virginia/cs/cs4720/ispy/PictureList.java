package edu.virginia.cs.cs4720.ispy;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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

        // Now create a new list adapter bound to the cursor.
        // SimpleListAdapter is designed for binding to a Cursor.
//        ListAdapter adapter = new ArrayAdapter<SpyPicture>(
//                this, // Context.
//                android.R.layout.activity_list_item,  // Specify the row template to use (here, two columns bound to the two retrieved cursor
//                myDb.getPictures());


        // Bind to our new adapter.
        //setListAdapter(adapter);

        displayList();
    }

    private void displayList () {
        Cursor cursor = myDb.getPictures();

        //desired columns to be bound
        String[] columns = new String[] {
            DBHelper.PICTURES_COLUMN_PATH,
            DBHelper.PICTURES_COLUMN_PATH,
            DBHelper.PICTURES_COLUMN_COLOR,
            DBHelper.PICTURES_COLUMN_LATITUDE,
            DBHelper.PICTURES_COLUMN_LONGITUDE
        };

        //xml views data will bind to
        int[] to = new int[] {
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

        setListAdapter(dataAdapter);
    }
}


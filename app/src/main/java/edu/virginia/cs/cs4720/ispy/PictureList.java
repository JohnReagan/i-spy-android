package edu.virginia.cs.cs4720.ispy;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import edu.virginia.cs.cs4720.ispy.DBHelper;

/**
 * Created by john on 9/29/15.
 */
public class PictureList extends ListActivity{
    DBHelper myDb;

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
        ListAdapter adapter = new ArrayAdapter<SpyPicture>(
                this, // Context.
                android.R.layout.activity_list_item,  // Specify the row template to use (here, two columns bound to the two retrieved cursor
                myDb.getPictures());


        // Bind to our new adapter.
        setListAdapter(adapter);
    }
}


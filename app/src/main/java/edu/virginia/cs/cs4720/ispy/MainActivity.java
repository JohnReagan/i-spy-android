package edu.virginia.cs.cs4720.ispy;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {
    Button b1, b2;
    Button camButt;
    Button galButt;
    TextView tv;
    EditText et;
    int TAKE_PIC = 1;
    Uri imageUri;
    ImageView ivImage;

    List<String> myList;
    String mCurrentPhotoPath;


    GPS gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivImage = (ImageView)findViewById(R.id.ivImage);
        b1 = (Button) findViewById(R.id.button1);


        b1.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                et = (EditText)findViewById(R.id.edit);
                tv = (TextView)findViewById(R.id.textView2);
                tv.setText("Welcome "+et.getText().toString()+"!");
            }
        });

        b2 = (Button) findViewById(R.id.button);



        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                gps = new GPS(MainActivity.this);

                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(), "your location is: \nLat" + latitude + "\nLon" + longitude, Toast.LENGTH_LONG).show();
                } else {
                    gps.showSettingsAlert();
                }
            }
        });

        camButt = (Button)findViewById(R.id.myButt);

        camButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(MainActivity.this, CameraPhotoCapture.class);
                startActivity(i);
            }
        });

        galButt = (Button)findViewById(R.id.galButt);
        galButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(MainActivity.this, AndroidCustomGallery.class);
                startActivity(i);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
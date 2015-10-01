package edu.virginia.cs.cs4720.ispy;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.virginia.cs.cs4720.ispy.DBHelper;

import java.io.IOException;

/**
 * Created by cole2 on 9/15/15.
 */
public class CameraPhotoCapture extends Activity {
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    static String[] filePaths = null;

    Uri imageUri                      = null;
    static TextView imageDetails      = null;
    public  static ImageView showImg  = null;
    CameraPhotoCapture CameraActivity = null;
    double latitude;
    double longitude;
    float x = -1;
    float y = -1;
    String path = "";
    GPS gps;
    DBHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_photo_capture);
        CameraActivity = this;
        myDb = new DBHelper(this);

        imageDetails = (TextView) findViewById(R.id.imageDetails);

        showImg = (ImageView) findViewById(R.id.showImg);

        final Button photo = (Button) findViewById(R.id.photo);

        showImg.setScaleType(ImageView.ScaleType.FIT_XY);
        showImg.setAdjustViewBounds(true);

        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        final Button home = (Button)findViewById(R.id.home);



        photo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*************************** Camera Intent Start ************************/

                // Define the file-name to save photo taken by Camera activity

                String fileName = "Camera_Example.jpg";

                // Create parameters for Intent with filename

                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.TITLE, fileName);

                values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");



                // imageUri is the current activity attribute, define and save it for later usage

                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                /**** EXTERNAL_CONTENT_URI : style URI for the "primary" external storage volume. ****/


                // Standard Intent action that can be sent to have the camera
                // application capture an image and return it.

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                /*************************** Camera Intent End ************************/


            }

        });

        showImg.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                //get location of click relative to original image dimensions

                // calculate inverse matrix
                Matrix inverse = new Matrix();
                showImg.getImageMatrix().invert(inverse);

                // map touch point from ImageView to image
                float[] touchPoint = new float[] {event.getX(), event.getY()};
                inverse.mapPoints(touchPoint);

                x = touchPoint[0];
                y = touchPoint[1];

                Toast.makeText(getApplicationContext(), "X: " + x + "\nY: " + x, Toast.LENGTH_LONG).show();

                updateCoordsText(touchPoint[0], touchPoint[1]);

                return false;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                savePicToDatabase();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraPhotoCapture.this, MainActivity.class);
                finish();
                startActivity(intent);

            }
        });
    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if ( resultCode == RESULT_OK) {

                /*********** Load Captured Image And Data Start ****************/

                String imageId = convertImageUriToFile( imageUri,CameraActivity);

                //Intent newIntent = new Intent(CameraPhotoCapture.this, AndroidCustomGallery.class);
                //startActi
                //  Create and excecute AsyncTask to load capture image
//                String[] stores = {MediaStore.Images.Media.DATA};
//                Cursor c = CameraActivity.getContentResolver().query(imageUri, stores, null, null, null);
//                int fileindex = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                c.moveToFirst();
//                String path = c.getString(fileindex);
//                Intent newIntent = new Intent(CameraPhotoCapture.this, PhotoView.class);
//                newIntent.putExtra("imgB", path);
//                newIntent.setAction(Intent.ACTION_SEND);
//                startActivity(newIntent);
                new LoadImagesFromSDCard().execute(""+imageId);

                /*********** Load Captured Image And Data End ****************/

                // Get coordinates
                gps = new GPS(CameraPhotoCapture.this);

                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    String details = imageDetails.getText() + " Latitude: " + latitude + "\n Longitude: " + longitude + "\n\n";

                    imageDetails.setText(details);

                } else {
                    gps.showSettingsAlert();
                }


            } else if ( resultCode == RESULT_CANCELED) {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateCoordsText(float x, float y) {
        String str = "X: " + x + ", Y: " + y;
        TextView textView = (TextView) findViewById(R.id.coordsText);
        textView.setText(str);
    }

    public boolean savePicToDatabase() {
        TextView colorView = (TextView) findViewById(R.id.color);
        String color = colorView.getText() + "";
        if (path.length() > 0) {
            if (color.length() != 0) {
                if (x >= 0 && y >= 0) {
                    myDb.insertPicture(path, x, y, latitude, longitude, color);
                    //Toast.makeText(getApplicationContext(), "Picture saved!", Toast.LENGTH_LONG).show();
                    Cursor rs = myDb.getPictureByPath(path);
                    rs.moveToFirst();
                    String info = "id: " + rs.getInt(rs.getColumnIndex(DBHelper.PICTURES_COLUMN_ID)) +
                            "\npath: " + rs.getString(rs.getColumnIndex(DBHelper.PICTURES_COLUMN_PATH)) +
                            "\ncolor: " + rs.getString(rs.getColumnIndex(DBHelper.PICTURES_COLUMN_COLOR));
                    Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "Specify coordinates", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Specify a color", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Take a picture first!", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    /************ Convert Image Uri path to physical path **************/

    public String convertImageUriToFile ( Uri imageUri, Activity activity )  {

        Cursor cursor = null;
        int imageID = 0;

        try {

            /*********** Which columns values want to get *******/
            String [] proj={
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };

            cursor = activity.getContentResolver().query(

                    imageUri,         //  Get data for specific image URI
                    proj,             //  Which columns to return
                    null,             //  WHERE clause; which rows to return (all rows)
                    null,             //  WHERE clause selection arguments (none)
                    null              //  Order-by clause (ascending by name)

            );

            //  Get Query Data

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            //int orientation_ColumnIndex = cursor.
            //    getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

            int size = cursor.getCount();

            /*******  If size is 0, there are no images on the SD Card. *****/

            if (size == 0) {


                imageDetails.setText("No Image");
            }
            else
            {

                int thumbID = 0;
                if (cursor.moveToFirst()) {

                    /**************** Captured image details ************/

                    /*****  Used to show image on view in LoadImagesFromSDCard class ******/
                    imageID     = cursor.getInt(columnIndex);

                    thumbID     = cursor.getInt(columnIndexThumb);

                    path = cursor.getString(file_ColumnIndex);

                    //String orientation =  cursor.getString(orientation_ColumnIndex);

                    String CapturedImageDetails = "Image Details: \n";

                    // Show Captured Image detail on activity
                    imageDetails.setText( CapturedImageDetails );

                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )

        return ""+imageID;
    }


    /**
     * Async task for loading the images from the SD card.
     *
     * @author Android Example
     *
     */

    // Class with extends AsyncTask class

    public class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(CameraPhotoCapture.this);

        Bitmap mBitmap;

        protected void onPreExecute() {
            /****** NOTE: You can call UI Element here. *****/

            // Progress Dialog
            Dialog.setMessage(" Loading image from Sdcard..");
            Dialog.show();
        }


        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            Uri uri = null;


            try {

                /**  Uri.withAppendedPath Method Description
                 * Parameters
                 *    baseUri  Uri to append path segment to
                 *    pathSegment  encoded path segment to append
                 * Returns
                 *    a new Uri based on baseUri with the given segment appended to the path
                 */

                uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + urls[0]);

                /**************  Decode an input stream into a bitmap. *********/
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                if (bitmap != null) {

                    /********* Creates a new bitmap, scaled from an existing bitmap. ***********/

//                    int orginalWidth = bitmap.getWidth();
//                    int originalHeight = bitmap.getHeight();

                    double factor = (double) bitmap.getWidth() / (double) bitmap.getHeight();

                    //newBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500 * (int) factor, true);
                    newBitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);

                    bitmap.recycle();

                    if (newBitmap != null) {

                        mBitmap = newBitmap;
                    }
                }
            } catch (IOException e) {
                // Error fetching image, try to recover

                /********* Cancel execution of this task. **********/
                cancel(true);
            }

            return null;
        }


        protected void onPostExecute(Void unused) {

            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if(mBitmap != null)
            {
                // Set Image to ImageView

                showImg.setImageBitmap(mBitmap);
            }

        }

    }


}
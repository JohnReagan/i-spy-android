package edu.virginia.cs.cs4720.ispy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by john on 10/12/15.
 */
public class SpyPictureAdapter extends ArrayAdapter<SpyPicture> {

    //array
    private ArrayList<SpyPicture> pictures;

    /* here we must override the constructor for ArrayAdapter
     * the only variable we care about now is ArrayList<Item> objects,
     * because it is the list of objects we want to display.
     */
    public SpyPictureAdapter(Context context, int textViewResourceId, ArrayList<SpyPicture> pictures) {
        super(context, textViewResourceId, pictures);
        this.pictures = pictures;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.picture_card, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        SpyPicture picture = pictures.get(position);

        if (picture != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView path = (TextView) v.findViewById(R.id.path);
            TextView id = (TextView) v.findViewById(R.id.id);
            TextView color = (TextView) v.findViewById(R.id.color);
            TextView latitude = (TextView) v.findViewById(R.id.latitude);
            TextView longitude = (TextView) v.findViewById(R.id.longitude);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (path != null) {
                path.setText(picture.getPath());
            }
            if (id != null) {
                id.setText(picture.getId() + "");
            }
            if (color != null) {
                color.setText(picture.getColor());
            }
            if (latitude != null) {
                latitude.setText(picture.getLatitude() + "");
            }
            if (longitude != null) {
                longitude.setText(picture.getLongitude() + "");
            }

            ImageView imageView = (ImageView) v.findViewById(R.id.image);

            // move to separate thread
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();

            Bitmap bitmap = picture.getImage();
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            imageView.setImageBitmap(bitmap);
        }

        // the view must be returned to our activity
        return v;
    }
}

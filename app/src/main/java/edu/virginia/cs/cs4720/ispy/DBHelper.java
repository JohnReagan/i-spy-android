package edu.virginia.cs.cs4720.ispy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import edu.virginia.cs.cs4720.ispy.SpyPicture;

/**
 * Created by john on 9/29/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ISpy.db";
    public static final String PICTURES_TABLE_NAME = "pictures";
    public static final String PICTURES_COLUMN_ID = "_id";
    public static final String PICTURES_COLUMN_PATH = "path";
    public static final String PICTURES_COLUMN_X = "x";
    public static final String PICTURES_COLUMN_Y = "y";
    public static final String PICTURES_COLUMN_LATITUDE = "latitude";
    public static final String PICTURES_COLUMN_LONGITUDE = "longitude";
    public static final String PICTURES_COLUMN_COLOR = "color";

    private static final String DATABASE_CREATE =
        "create table if not exists " + PICTURES_TABLE_NAME + " (" +
        PICTURES_COLUMN_ID + " integer primary key autoincrement," +
        PICTURES_COLUMN_PATH + " text," +
        PICTURES_COLUMN_X + " float," +
        PICTURES_COLUMN_Y + " float," +
        PICTURES_COLUMN_LATITUDE + " double," +
        PICTURES_COLUMN_LONGITUDE + " double," +
        PICTURES_COLUMN_COLOR + " text)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS pictures");
        onCreate(db);
    }

    public boolean insertPicture (final String path, final float x, final float y, final double lat, final double lng, final String color) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        final ParseFile file = new ParseFile("spypicture.png", byteArray);
        file.saveInBackground(new SaveCallback() {
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    ParseObject contentValues = new ParseObject("Picture");
                    contentValues.put("user", ParseUser.getCurrentUser());
                    contentValues.put("path", path);
                    contentValues.put("x", x);
                    contentValues.put("y", y);
                    contentValues.put("latitude", lat);
                    contentValues.put("longitude", lng);
                    contentValues.put("color", color);
                    contentValues.put("image", file);
                    contentValues.saveInBackground();
                } else {
                    Log.d("save", "file did not save");
                }
            }
        });

        //db.insert("pictures", null, contentValues);
        return true;
    }
    public Cursor getPicture(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from pictures where " + DBHelper.PICTURES_COLUMN_ID + "=" + (int) id + "", null);

        return res;
    }

    public Cursor getPictureByPath(String path) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from pictures where path=\'" + path + "\'", null);
        return res;
    }


    public ArrayList<SpyPicture> getPictures () {
        final ArrayList<SpyPicture> pictures = new ArrayList<SpyPicture>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Picture");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                    for(int i = 0; i < scoreList.size(); i++) {
                        ParseObject f = scoreList.get(i);
                        ParseFile imageFile = f.getParseFile("picture");
                        try {
                            byte[] data = imageFile.getData();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            SpyPicture pic = new SpyPicture(f.getString("objectID"), f.getString("path"), f.getString("color"), f.getNumber("x").floatValue(), f.getNumber("y").floatValue(), f.getNumber("latitude").doubleValue(), f.getNumber("longitude").doubleValue(), bitmap);
                            pictures.add(pic);
                        } catch (com.parse.ParseException error) {
                            Log.d("load", e.getMessage());
                        }
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


        return pictures;
    }
}

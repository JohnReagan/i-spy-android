package edu.virginia.cs.cs4720.ispy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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

    public boolean insertPicture (String path, float x, float y, double lat, double lng, String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("path", path);
        contentValues.put("x", x);
        contentValues.put("y", y);
        contentValues.put("latitude", lat);
        contentValues.put("longitude", lng);
        contentValues.put("color", color);
        db.insert("pictures", null, contentValues);
        return true;
    }

    public Cursor getPicture(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from pictures where id="+id+"", null);
        return res;
    }

    public Cursor getPictureByPath(String path) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from pictures where path=\'" + path + "\'", null);
        return res;
    }

    public Cursor getPictures () {
//    public ArrayList<SpyPicture> getPictures () {
        //ArrayList<SpyPicture> pictureList = new ArrayList<SpyPicture>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from pictures", null);

        if (res != null) {
            res.moveToFirst();
        }
//        while (!res.isAfterLast()) {
//            SpyPicture pic = new SpyPicture(res.getInt(res.getColumnIndex(DBHelper.PICTURES_COLUMN_ID)),
//                    res.getString(res.getColumnIndex(DBHelper.PICTURES_COLUMN_PATH)),
//                    res.getString(res.getColumnIndex(DBHelper.PICTURES_COLUMN_COLOR)),
//                    res.getFloat(res.getColumnIndex(DBHelper.PICTURES_COLUMN_X)),
//                    res.getFloat(res.getColumnIndex(DBHelper.PICTURES_COLUMN_Y)),
//                    res.getDouble(res.getColumnIndex(DBHelper.PICTURES_COLUMN_LATITUDE)),
//                    res.getDouble(res.getColumnIndex(DBHelper.PICTURES_COLUMN_LONGITUDE)));
//
//            pictureList.add(pic);
//        }

        return res;
    }
}

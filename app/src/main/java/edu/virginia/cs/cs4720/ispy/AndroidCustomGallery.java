package edu.virginia.cs.cs4720.ispy;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
public class AndroidCustomGallery extends Activity {
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private ImageAdapter imageAdapter;
    static String[] files;

    Button selectBtn;
    public static Bitmap[] removeElement(Bitmap[] original, int element) {
        Bitmap[] n = new Bitmap[original.length - 1];
        System.arraycopy(original, 0, n, 0, element);
        System.arraycopy(original, element+1, n, element, original.length-element-1);
        return n;
    }
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_custom_gallery);

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = imagecursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        this.count = imagecursor.getCount();
        this.thumbnails = new Bitmap[this.count];
        files = new String[this.count];
        this.arrPath = new String[this.count];
        this.thumbnailsselection = new boolean[this.count];
        for (int i = 0; i < this.count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MICRO_KIND, null);

            arrPath[i] = imagecursor.getString(dataColumnIndex);
        }
        final GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);

        selectBtn = (Button) findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                final int len = thumbnailsselection.length;
                int cnt = 0;
                String selectImages = "";
                for (int i = 0; i < len; i++) {
                    if (thumbnailsselection[i]) {
                        cnt++;
                        selectImages = selectImages + arrPath[i] + "|";
                    }
                }
                if (cnt == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Please select at least one image",
                            Toast.LENGTH_LONG).show();
                } else {
                    for(int i = 0; i < files.length; i++) {
                        if (files[i] != null) {
                            File img = new File(files[i]);
                            if (img.exists()) {
                                Log.d("ImageDeleted", files[i]);
                                img.delete();
                                thumbnails[i] = null;
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

                            } else {
                                Log.d("Error", files[i]);
                            }
                        }

                    }
                    //recreate();
                    //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                    imageAdapter.notifyDataSetChanged();
                    imagegrid.setAdapter(imageAdapter);
                    finish();
                    startActivity(getIntent());

                    //Log.d("arrPath", arrPath[0]);
                    //Log.d("SelectedImages", selectImages);


                }

            }
        });
    }




    public class ImageAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.galcustom, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
                if (thumbnails[position] != null) {
                    holder.imageview.setImageBitmap(thumbnails[position]);
                    holder.checkbox.setChecked(thumbnailsselection[position]);
                }
            }
            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.checkbox.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]){
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsselection[id] = true;
                        //Log.d("ID", "" + id);
                        //Log.d("ArrPath", "" + arrPath[id]);
                        files[id] = arrPath[id];
                        //Log.d("Files", "" + files[id]);
                    }
                }
            });
            holder.imageview.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();
                    /*Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
                    startActivity(intent);*/
                    Intent newIntent = new Intent(AndroidCustomGallery.this,
                            PhotoView.class);
                    newIntent.setAction(Intent.ACTION_SEND);
                    newIntent.putExtra("img", arrPath[id]);
                    startActivity(newIntent);

                }
            });
            holder.imageview.setImageBitmap(thumbnails[position]);
            holder.checkbox.setChecked(thumbnailsselection[position]);
            holder.id = position;
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        int id;
    }
}
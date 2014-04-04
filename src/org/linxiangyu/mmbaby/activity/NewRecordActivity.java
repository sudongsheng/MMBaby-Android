package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.linxiangyu.mmbaby.R;
import org.linxiangyu.mmbaby.database.DatabaseHelper;
import org.linxiangyu.mmbaby.database.Record;
import org.linxiangyu.mmbaby.utils.AppConstant;
import org.linxiangyu.mmbaby.utils.FontManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sudongsheng on 14-3-18.
 */
public class NewRecordActivity extends Activity {

    private TextView date;
    private EditText title;
    private EditText content;
    private ImageView mBaby;
    private Button submit;
    private RatingBar rating;
    private ImageView back;
    private GridView photos;

    private Record record;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private int position;

    private SharedPreferences preferences;
    private int money;
    private int integral;
    private int grade;
    private int money_old;
    private int integral_old;
    private int grade_old;

    private final int REQUEST_CODE_CAPTURE_CAMERA = 1;
    private final int REQUEST_CODE_PICK_IMAGE = 2;
    private final int REQUEST_CODE_CUT_IMAGE = 3;

    private MyAdapter myAdapter;
    private int position_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        init();
        myAdapter = new MyAdapter(this);
        photos.setAdapter(myAdapter);
    }

    private void init() {

        findViewByIds();

        record = new Record();
        record.field = getIntent().getStringExtra("Field");
        record.rating = (int) rating.getRating();

        setListeners();

        preferences = PreferenceManager.getDefaultSharedPreferences(NewRecordActivity.this);
        money = preferences.getInt("money", 0);
        integral = preferences.getInt(record.field + "_integral", 0);
        grade = preferences.getInt(record.field + "_grade", 0);

        //判断新建记录或者读取记录并把读取的数据显示出来
        position = getIntent().getIntExtra("position", -1);
        if (position != -1) {
            dbHelper = new DatabaseHelper(NewRecordActivity.this, "MMBaby");
            sqLiteDatabase = dbHelper.getReadableDatabase();
            cursor = sqLiteDatabase.query("record", null, "field" + "=?", new String[]{getIntent().getStringExtra("Field")}, null, null, "time");
            cursor.moveToPosition(position);
            record.time = cursor.getString(cursor.getColumnIndex("time"));
            record.title = cursor.getString(cursor.getColumnIndex("title"));
            record.content = cursor.getString(cursor.getColumnIndex("content"));
            record.rating = cursor.getInt(cursor.getColumnIndex("rating"));
            money_old = record.rating * 100;
            integral_old = record.rating * 100;
            grade_old = record.rating;
            record.primary_key = cursor.getInt(cursor.getColumnIndex("primary_key"));
            date.setText(record.time);
            title.setText(record.title);
            content.setText(record.content);
            rating.setRating(record.rating);
            try {
                for (int i = 0; i < 6; i++) {
                    record.photo[i] = cursor.getBlob(cursor.getColumnIndex("photo" + i));
                }
            } catch (Exception e) {
                Log.i("TAG","exception:"+e.toString());
            }
        }

        ViewGroup v = (ViewGroup) findViewById(R.id.new_record_activity);
        FontManager.changeFonts(v, NewRecordActivity.this, AppConstant.Mama);
    }

    private void findViewByIds() {
        date = (TextView) findViewById(R.id.time);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        submit = (Button) findViewById(R.id.submit);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        back = (ImageView) findViewById(R.id.back);
        photos = (GridView) findViewById(R.id.photoGridView);
    }

    private void setListeners() {
        //日期选择
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int monthOfYear = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        record.time = year + "." + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + "." + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        date.setText(record.time);
        final DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int y, int m, int d) {
                record.time = y + "." + (m + 1) + "." + d;
                date.setText(record.time);
            }
        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NewRecordActivity.this, dateSet, year, monthOfYear, dayOfMonth).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                NewRecordActivity.this.finish();
            }
        });
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                record.rating = (int) v;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().equals("")) {
                    Toast.makeText(NewRecordActivity.this, "标题不能为空", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("time", record.time);
                    cv.put("field", record.field);
                    cv.put("title", title.getText().toString());
                    cv.put("content", content.getText().toString());
                    cv.put("rating", record.rating);
                    for (int i = 0; i < 6; i++) {
                        if (record.photo[i] != null) {
                            cv.put("photo" + i, record.photo[i]);
                        }
                    }
                    DatabaseHelper dbHelper = new DatabaseHelper(NewRecordActivity.this, "MMBaby");
                    SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

                    SharedPreferences.Editor editor = preferences.edit();
                    if (position == -1) {
                        sqLiteDatabase.insert("record", null, cv);
                        Toast.makeText(NewRecordActivity.this, "新建记录成功", Toast.LENGTH_LONG).show();
                        money = money + record.rating * 100;
                        integral = integral + record.rating * 100;
                        grade = grade + record.rating;
                        editor.putInt("money", money);
                        editor.putInt(record.field + "_integral", integral);
                        editor.putInt(record.field + "_grade", grade);
                    } else {
                        sqLiteDatabase.update("record", cv, "primary_key=?", new String[]{String.valueOf(record.primary_key)});
                        money = money + record.rating * 100 - money_old;
                        integral = integral + record.rating * 100 - integral_old;
                        grade = grade + record.rating - grade_old;
                        editor.putInt("money", money);
                        editor.putInt(record.field + "_integral", integral);
                        editor.putInt(record.field + "_grade", grade);
                    }
                    editor.commit();

                    Intent intent = new Intent(NewRecordActivity.this, MamaActivity.class);
                    startActivity(intent);
                    NewRecordActivity.this.finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        int position = data.getIntExtra("position", -1);
        Log.i("TAG", "position:" + position_photo);
        if (requestCode == REQUEST_CODE_CAPTURE_CAMERA && resultCode == RESULT_OK && null != data) {
            Bundle bundle = data.getExtras();
            Bitmap photo = bundle.getParcelable("data");
            mBaby.setImageBitmap(photo);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, os);
            record.photo[position_photo] = os.toByteArray();
        } else if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            startPhotoZoom(data.getData());
        } else if (requestCode == REQUEST_CODE_CUT_IMAGE && null != data) {
            Uri imgUri = data.getData();
            Bitmap bitmap = null;
            if (imgUri != null) {
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Bundle bundle = data.getExtras();
                if (bundle != null)
                    bitmap = bundle.getParcelable("data");
            }
            mBaby.setImageBitmap(bitmap);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            record.photo[position_photo] = os.toByteArray();
        }
        int count = 0;
        for (int i = 0; i < 6; i++) {
            if (record.photo[i] != null)
                count++;
        }
        myAdapter.count = count;
        myAdapter.notifyDataSetChanged();
    }

    //    private void iterator(SQLiteDatabase sqLiteDatabase){
//        String s = new String();
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM record", null);
//        while (cursor.moveToNext()) {
//            for (int i = 0; i < cursor.getCount(); i++)
//                s = s + cursor.getString(i) + "   ";
//            s=s+"\n";
//        }
//        Log.i("task", s);
//    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 3);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_CODE_CUT_IMAGE);
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int count = 6;

        public MyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            for (int i = 5; i >= 0; i--) {
                if (record.photo[i] == null)
                    count--;
            }
        }

        @Override
        public int getCount() {
            return count + 1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.view_photos_item, null);
            mBaby = (ImageView) view.findViewById(R.id.mBaby);
            if (i == count && i < 6) {
                mBaby.setImageResource(R.drawable.add);
                mBaby.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(NewRecordActivity.this, R.style.pickDialog);
                        dialog.setContentView(R.layout.view_pick_dialog);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        TextView fromCamera = (TextView) dialog.findViewById(R.id.fromCamera);
                        TextView fromAlbum = (TextView) dialog.findViewById(R.id.fromAlbum);
                        fromCamera.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String state = Environment.getExternalStorageState();
                                if (state.equals(Environment.MEDIA_MOUNTED)) {
                                    Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                                    //                           getImageByCamera.putExtra("position", i);
                                    position_photo = i;
                                    startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMERA);
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(NewRecordActivity.this, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        fromAlbum.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent getImageByAlbum = new Intent(Intent.ACTION_PICK);
                                getImageByAlbum.setType("image/*");//相片类型
                                //                        getImageByAlbum.putExtra("position", i);
                                position_photo = i;
                                startActivityForResult(getImageByAlbum, REQUEST_CODE_PICK_IMAGE);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(record.photo[i], 0, record.photo[i].length);
                mBaby.setImageBitmap(bitmap);
            }
            return view;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        cursor.close();
        dbHelper.close();
        sqLiteDatabase.close();
    }
}

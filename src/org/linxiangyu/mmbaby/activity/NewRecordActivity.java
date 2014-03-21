package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.linxiangyu.mmbaby.R;
import org.linxiangyu.mmbaby.database.DatabaseHelper;
import org.linxiangyu.mmbaby.database.Record;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by sudongsheng on 14-3-18.
 */
public class NewRecordActivity extends Activity {

    private TextView date;
    private EditText title;
    private EditText content;
    private Button photo;
    private ImageView mBaby;
    private Button submit;
    private RatingBar rating;

    private Record record;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private int position;

    private final int REQUEST_CODE_CAPTURE_CAMERA = 1;
    private final int REQUEST_CODE_PICK_IMAGE = 2;
    private final int REQUEST_CODE_CUT_IMAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        init();
    }

    private void init() {

        findViewByIds();

        record = new Record();
        record.field = getIntent().getStringExtra("Field");
        record.rating = (int) rating.getRating();

        setListeners();

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
            record.primary_key = cursor.getInt(cursor.getColumnIndex("primary_key"));
            date.setText(record.time);
            title.setText(record.title);
            content.setText(record.content);
            rating.setRating(record.rating);
            try {
                record.photo = cursor.getBlob(cursor.getColumnIndex("photo"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(record.photo, 0, record.photo.length);
                mBaby.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setListeners() {
        //日期选择
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int monthOfYear = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        record.time = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
        date.setText(record.time);
        final DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int y, int m, int d) {
                record.time = y + "年" + (m + 1) + "月" + d + "日";
                date.setText(record.time);
            }
        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NewRecordActivity.this, dateSet, year, monthOfYear, dayOfMonth).show();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
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
                        startActivityForResult(getImageByAlbum, REQUEST_CODE_PICK_IMAGE);
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
                if (title.getText().equals("")) {
                    Toast.makeText(NewRecordActivity.this, "标题不能为空", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("time", record.time);
                    cv.put("field", record.field);
                    cv.put("title", title.getText().toString());
                    cv.put("content", content.getText().toString());
                    cv.put("rating", record.rating);
                    if (record.photo != null) {
                        cv.put("photo", record.photo);
                    }
                    DatabaseHelper dbHelper = new DatabaseHelper(NewRecordActivity.this, "MMBaby");
                    SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
                    if (position == -1) {
                        sqLiteDatabase.insert("record", null, cv);
                        Toast.makeText(NewRecordActivity.this, "新建记录成功", Toast.LENGTH_LONG).show();
                    } else {
                        sqLiteDatabase.update("record", cv, "primary_key=?", new String[]{String.valueOf(record.primary_key)});
                        Toast.makeText(NewRecordActivity.this, "修改记录成功", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(NewRecordActivity.this, MamaActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_CAPTURE_CAMERA && resultCode == RESULT_OK && null != data) {
            Bundle bundle = data.getExtras();
            Bitmap photo = bundle.getParcelable("data");
            mBaby.setImageBitmap(photo);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, os);
            record.photo = os.toByteArray();
        } else if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            startPhotoZoom(data.getData());
        } else if (requestCode == REQUEST_CODE_CUT_IMAGE && resultCode == RESULT_OK && null != data) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap photo = bundle.getParcelable("data");
                mBaby.setImageBitmap(photo);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, os);
                record.photo = os.toByteArray();
            }
        }
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
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CODE_CUT_IMAGE);
    }

    private void findViewByIds() {
        date = (TextView) findViewById(R.id.time);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        photo = (Button) findViewById(R.id.photo);
        mBaby = (ImageView) findViewById(R.id.mBaby);
        submit = (Button) findViewById(R.id.submit);
        rating = (RatingBar) findViewById(R.id.ratingBar);
    }
}

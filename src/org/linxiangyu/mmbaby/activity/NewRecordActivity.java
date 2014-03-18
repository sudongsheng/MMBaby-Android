package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.linxiangyu.mmbaby.R;
import org.linxiangyu.mmbaby.database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by helloworld on 14-3-18.
 */
public class NewRecordActivity extends Activity {

    private DatePicker datePicker;
    private RadioGroup field;
    private RadioButton morality;
    private RadioButton intelligence;
    private RadioButton physical;
    private EditText title;
    private EditText content;
    private Button photo;
    private ImageView mBaby;
    private Button submit;

    private String time;
    private String mField;
    private byte[] baby;

    private final int REQUEST_CODE_CAPTURE_CAMERA = 1;
    private final int REQUEST_CODE_PICK_IMAGE = 2;
    private final int REQUEST_CODE_CUT_IMAGE=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        findViewByIds();
        setListeners();
    }

    private void setListeners() {
        //日期选择
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        time = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                time = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
            }
        });
        field.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.morality) {
                    mField = "morality";
                } else if (i == R.id.intelligence) {
                    mField = "intelligence";
                } else if (i == R.id.physical) {
                    mField = "physical";
                }
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(NewRecordActivity.this, R.style.pickDialog);
                dialog.setContentView(R.layout.view_pick_dialog);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                Button fromCamera = (Button) dialog.findViewById(R.id.fromCamera);
                Button fromAlbum = (Button) dialog.findViewById(R.id.fromAlbum);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_CAPTURE_CAMERA&& resultCode == RESULT_OK && null != data) {
            Bundle bundle = data.getExtras();
            Bitmap photo = bundle.getParcelable("data");
            mBaby.setImageBitmap(photo);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, os);
            baby=os.toByteArray();
        } else if (requestCode == REQUEST_CODE_PICK_IMAGE&& resultCode == RESULT_OK && null != data) {
            startPhotoZoom(data.getData());
        } else if (requestCode == REQUEST_CODE_CUT_IMAGE&& resultCode == RESULT_OK && null != data) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap photo = bundle.getParcelable("data");
                mBaby.setImageBitmap(photo);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, os);
                baby=os.toByteArray();
            }
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().equals("")) {
                    Toast.makeText(NewRecordActivity.this, "标题不能为空", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("time", time);
                    cv.put("field", mField);
                    cv.put("title", title.getText().toString());
                    cv.put("content", content.getText().toString());
                    cv.put("photo",baby);
                    DatabaseHelper dbHelper = new DatabaseHelper(NewRecordActivity.this, "MMBaby");
                    SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
                    sqLiteDatabase.insert("record", null, cv);
                    Toast.makeText(NewRecordActivity.this,"新建记录成功",Toast.LENGTH_LONG).show();
                    iterator(sqLiteDatabase);
                    Intent intent=new Intent(NewRecordActivity.this,MamaActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    private void iterator(SQLiteDatabase sqLiteDatabase){
        String s = new String();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM record", null);
        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getCount(); i++)
                s = s + cursor.getString(i) + "   ";
            s=s+"\n";
        }
        Log.i("task", s);
    }
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
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        field = (RadioGroup) findViewById(R.id.field);
        morality = (RadioButton) findViewById(R.id.morality);
        intelligence = (RadioButton) findViewById(R.id.intelligence);
        physical = (RadioButton) findViewById(R.id.physical);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        photo = (Button) findViewById(R.id.photo);
        mBaby = (ImageView) findViewById(R.id.mBaby);
        submit = (Button) findViewById(R.id.submit);
    }
}

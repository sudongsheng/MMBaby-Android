package me.ahfun.mmbaby.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import me.ahfun.mmbaby.R;
import me.ahfun.mmbaby.database.DatabaseHelper;
import me.ahfun.mmbaby.database.Record;
import me.ahfun.mmbaby.utils.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private LinearLayout linearLayout;

    private Record record;
    private String time;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private int position;

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

    private int screenWidth;

    private int rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        init();
        myAdapter = new MyAdapter(this);
        photos.setAdapter(myAdapter);
    }

    private void init() {
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();

        findViewByIds();

        record = new Record();
        record.field = getIntent().getStringExtra("Field");
        record.rating = (int) rating.getRating();
        record.photoNum = 0;
        record.isCheck = AppConstant.FALSE;
        record.primary_key=1;

        setListeners();

        money = (Integer) SharedPreferencesUtils.getParam(NewRecordActivity.this, "money", 0);
        integral = (Integer) SharedPreferencesUtils.getParam(NewRecordActivity.this, record.field + "_integral", 0);
        grade = (Integer) SharedPreferencesUtils.getParam(NewRecordActivity.this, record.field + "_grade", 0);
        rate = (Integer) SharedPreferencesUtils.getParam(NewRecordActivity.this, record.field + "_rate", 100);

        //判断新建记录或者读取记录并把读取的数据显示出来
        position = getIntent().getIntExtra("position", -1);
        dbHelper = new DatabaseHelper(NewRecordActivity.this, "MMBaby");
        sqLiteDatabase = dbHelper.getReadableDatabase();

        if (position != -1) {
            cursor = sqLiteDatabase.query("record", null, "field" + "=?", new String[]{getIntent().getStringExtra("Field")}, null, null, "time");
            cursor.moveToPosition(position);
            time=cursor.getString(cursor.getColumnIndex("time"));
            record.time = time;
            record.title = cursor.getString(cursor.getColumnIndex("title"));
            record.content = cursor.getString(cursor.getColumnIndex("content"));
            record.rating = cursor.getInt(cursor.getColumnIndex("rating"));
            record.photoNum = cursor.getInt(cursor.getColumnIndex("photoNum"));
            money_old = record.rating * rate;
            integral_old = record.rating * rate;
            grade_old = record.rating;
            record.primary_key = cursor.getInt(cursor.getColumnIndex("primary_key"));
            date.setText(record.time);
            title.setText(record.title);
            content.setText(record.content);
            rating.setRating(record.rating);
            linearLayout.setFocusable(true);
            linearLayout.setFocusableInTouchMode(true);
        } else {
            cursor = sqLiteDatabase.query("record", null,null,null,null,null,null);
            if(cursor.getCount()!=0) {
                cursor.moveToLast();
                Log.i("TAG", cursor.getInt(cursor.getColumnIndex("primary_key")) + "");
                record.primary_key = cursor.getInt(cursor.getColumnIndex("primary_key"))+1;
            }
        }

//        ViewGroup v = (ViewGroup) findViewById(R.id.new_record_activity);
//        FontManager.changeFonts(v, NewRecordActivity.this, AppConstant.Mama);
    }

    private void findViewByIds() {
        date = (TextView) findViewById(R.id.time);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        submit = (Button) findViewById(R.id.submit);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        back = (ImageView) findViewById(R.id.back);
        photos = (GridView) findViewById(R.id.photoGridView);
        linearLayout = (LinearLayout) findViewById(R.id.edit_linear);
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
                record.time = y + "." + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "." + (d < 10 ? "0" + d : d);
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
                    cv.put("photoNum", record.photoNum);
                    cv.put("isCheck", record.isCheck);
                    DatabaseHelper dbHelper = new DatabaseHelper(NewRecordActivity.this, "MMBaby");
                    SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

                    if (position == -1) {
                        File file= new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/亲子习惯养成/"+record.field+"/temp");
                        file.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/亲子习惯养成/"+record.field+"/"+record.time+"-"+record.primary_key));
                        sqLiteDatabase.insert("record", null, cv);
                        Toast.makeText(NewRecordActivity.this, "新建记录成功", Toast.LENGTH_LONG).show();
                        money = money + record.rating * rate;
                        integral = integral + record.rating * rate;
                        grade = grade + record.rating;
                        SharedPreferencesUtils.setParam(NewRecordActivity.this, "money", money);
                        SharedPreferencesUtils.setParam(NewRecordActivity.this, record.field + "_integral", integral);
                        SharedPreferencesUtils.setParam(NewRecordActivity.this, record.field + "_grade", grade);
                    } else {
                        if(!time.equals(record.time)){
                            File file= new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/亲子习惯养成/"+record.field+"/"+time+"-"+record.primary_key);
                            file.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/亲子习惯养成/"+record.field+"/"+record.time+"-"+record.primary_key));
                        }
                        sqLiteDatabase.update("record", cv, "primary_key=?", new String[]{String.valueOf(record.primary_key)});
                        money = money + record.rating * rate - money_old;
                        integral = integral + record.rating * rate - integral_old;
                        grade = grade + record.rating - grade_old;
                        SharedPreferencesUtils.setParam(NewRecordActivity.this, "money", money);
                        SharedPreferencesUtils.setParam(NewRecordActivity.this, record.field + "_integral", integral);
                        SharedPreferencesUtils.setParam(NewRecordActivity.this, record.field + "_grade", grade);
                    }
                    CustomDialog customDialog = new CustomDialog(NewRecordActivity.this, R.layout.view_dialog_new_record, R.style.settingDialog);
                    customDialog.show();
                    customDialog.setCanceledOnTouchOutside(false);
                    Button yes = (Button) customDialog.findViewById(R.id.dialog_new_record_yes);
                    Button not = (Button) customDialog.findViewById(R.id.dialog_new_record_not);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(NewRecordActivity.this, ChooseShareActivity.class);
                            intent.putExtra("title", title.getText().toString());
                            startActivity(intent);
                            NewRecordActivity.this.finish();
                        }
                    });
                    not.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(NewRecordActivity.this, MamaActivity.class);
                            startActivity(intent);
                            NewRecordActivity.this.finish();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("TAG", "position_photo:" + position_photo);
        if (requestCode == REQUEST_CODE_CAPTURE_CAMERA && resultCode == RESULT_OK) {
            record.photoNum++;
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
            record.photoNum++;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            if (position == -1)
                new FileUtils().write2SDFromBitmap("亲子习惯养成/" + record.field + "/temp/", position_photo + ".jpg", bitmap);
            else
                new FileUtils().write2SDFromBitmap("亲子习惯养成/" + record.field + "/" + record.time + "-" + record.primary_key, position_photo + ".jpg", bitmap);


        }
        myAdapter.count = record.photoNum;
        myAdapter.notifyDataSetChanged();
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 3);
        intent.putExtra("aspectY", 4);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 450);
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_CODE_CUT_IMAGE);
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int count;

        public MyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            count = record.photoNum;
            //          Log.i("TAG", "count:" + count);
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
                                    FileUtils fileUtils = new FileUtils();
                                    File file = null;
                                    try {
                                        if (position == -1) {
                                            fileUtils.createSDDir("亲子习惯养成/" + record.field + "/temp");
                                            file = fileUtils.createFileInSDCard("亲子习惯养成/" + record.field + "/temp/", i + ".jpg");
                                        } else {
                                            file = fileUtils.createFileInSDCard("亲子习惯养成/" + record.field + "/" + record.time + "-" + record.primary_key + "/", i + ".jpg");
                                        }
                                    } catch (Exception e) {
                                    }
                                    Uri imageUri = Uri.fromFile(file);
                                    getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                    getImageByCamera.putExtra("return-data", true);
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
                                position_photo = i;
                                startActivityForResult(getImageByAlbum, REQUEST_CODE_PICK_IMAGE);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
            } else if (i < 6) {
                ArrayList<String> arrayList_temp = null;
                if (position == -1)
                    arrayList_temp = new FileUtils().readFileName("亲子习惯养成/" + record.field + "/temp/");
                else
                    arrayList_temp = new FileUtils().readFileName("亲子习惯养成/" + record.field + "/" + record.time + "-" + record.primary_key);
                final ArrayList<String> arrayList = arrayList_temp;
                //解决用户手动删除内存卡的图片出现的异常情况
                if (arrayList.size() < record.photoNum) {
                    record.photoNum = arrayList.size();
                    myAdapter.count = record.photoNum;
                    myAdapter.notifyDataSetChanged();
                    for (int j = 0; j < arrayList.size(); j++) {
                        File file1 = new File(arrayList.get(j));
                        if (position == -1)
                            file1.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/亲子习惯养成/" + record.field + "/temp/" + j + ".jpg"));
                        else
                            file1.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/亲子习惯养成/" + record.field + "/" + record.time + "-" + record.primary_key + "/" + j + ".jpg"));


                    }
                }
                Bitmap bitmap = BitmapFactory.decodeFile(arrayList.get(i));
                final int h = bitmap.getHeight();
                final int w = bitmap.getWidth();
                // Log.i("TAG", "w:" + w + "h:" + h);
                Matrix matrix = new Matrix();
                if (h > w) {
                    matrix.postScale(240f / w, 320f / h);
                } else {
                    matrix.postScale(240f / h, 320f / w);
                    matrix.postRotate(90);
                }
                Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
                bitmap.recycle();
                mBaby.setImageBitmap(resizeBmp);
                mBaby.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap bitmap = BitmapFactory.decodeFile(arrayList.get(i));
                        ImageScan img = new ImageScan(NewRecordActivity.this, (int) (screenWidth / 7f * 6f), (int) (h > w ? screenWidth / 7f / w * 6f * h : screenWidth / 7f / h * 6f * w));
                        Matrix matrix = new Matrix();
                        if (h > w) {
                            matrix.postScale(screenWidth / 7f / w * 6f, screenWidth / 7f / w * 6f);
                        } else {
                            matrix.postScale(screenWidth / 7f / h * 6f, screenWidth / 7f / h * 6f);
                            matrix.postRotate(90);
                        }
                        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
                        bitmap.recycle();
                        Toast.makeText(NewRecordActivity.this, "双指拖动可缩放图片~", Toast.LENGTH_SHORT).show();
                        img.setImageBitmap(resizeBmp);
                        new AlertDialog.Builder(NewRecordActivity.this).setView(img).create().show();
                    }
                });
                mBaby.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        new AlertDialog.Builder(NewRecordActivity.this).setTitle("是否删除图片？").setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        File file = new File(arrayList.get(i));
                                        file.delete();
                                        record.photoNum--;
                                        for (int j = i + 1; j < arrayList.size(); j++) {
                                            File file1 = new File(arrayList.get(j));
                                            if (position == -1)
                                                file1.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/亲子习惯养成/" + record.field + "/temp/" + (j - 1) + ".jpg"));
                                            else
                                                file1.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/亲子习惯养成/" + record.field + "/" + record.time + "-" + record.primary_key + "/" + (j - 1) + ".jpg"));
                                        }
                                        myAdapter.count--;
                                        myAdapter.notifyDataSetChanged();
                                    }
                                }
                        ).setNegativeButton("取消", null).show();
                        return false;
                    }
                });
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

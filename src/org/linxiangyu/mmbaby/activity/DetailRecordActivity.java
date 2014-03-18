package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.linxiangyu.mmbaby.R;
import org.linxiangyu.mmbaby.database.DatabaseHelper;

/**
 * Created by helloworld on 14-3-18.
 */
public class DetailRecordActivity extends Activity {

    private TextView date;
    private RadioGroup field;
    private RadioButton morality;
    private RadioButton intelligence;
    private RadioButton physical;
    private EditText title;
    private EditText content;
    private ImageView mBaby;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_record);
        DatabaseHelper dbHelper = new DatabaseHelper(DetailRecordActivity.this, "MMBaby");
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM record", null);
        cursor.moveToPosition(getIntent().getIntExtra("position", 0));

        findViewByIds();

        date.setText(cursor.getString(cursor.getColumnIndex("time")));
        if(cursor.getString(cursor.getColumnIndex("field"))=="morality")
            field.check(R.id.detail_morality);
        else if(cursor.getString(cursor.getColumnIndex("field"))=="intelligence")
            field.check(R.id.detail_intelligence);
        if(cursor.getString(cursor.getColumnIndex("field"))=="physical")
            field.check(R.id.detail_physical);
        title.setText(cursor.getString(cursor.getColumnIndex("title")));
        content.setText(cursor.getString(cursor.getColumnIndex("content")));
        byte[] in=cursor.getBlob(cursor.getColumnIndex("photo"));
        Bitmap bitmap= BitmapFactory.decodeByteArray(in, 0, in.length);
        mBaby.setImageBitmap(bitmap);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(DetailRecordActivity.this,MamaActivity.class);
                startActivity(intent);
            }
        });
    }
    private void findViewByIds() {
        date = (TextView) findViewById(R.id.detail_time);
        field = (RadioGroup) findViewById(R.id.detail_field);
        title = (EditText) findViewById(R.id.detail_title);
        content = (EditText) findViewById(R.id.detail_content);
        mBaby = (ImageView) findViewById(R.id.detail_mBaby);
        submit = (Button) findViewById(R.id.detail_back);
    }
}

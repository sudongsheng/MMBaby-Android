package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.linxiangyu.mmbaby.R;
import org.linxiangyu.mmbaby.database.DatabaseHelper;

/**
 * Created by helloworld on 14-3-17.
 */
public class MamaActivity extends Activity {

    private EditText title;
    private EditText content;
    private Button submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mama);

        DatabaseHelper dbHelper = new DatabaseHelper(MamaActivity.this, "MMBaby");
        final SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        findViewByIds();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().equals("")) {
                    Toast.makeText(MamaActivity.this, "标题不能为空", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("title", title.getText().toString());
                    cv.put("content", content.getText().toString());
                    sqLiteDatabase.insert("task", null, cv);
                    iterator(sqLiteDatabase);
                }
            }
        });


    }

    private void findViewByIds() {
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        submit = (Button) findViewById(R.id.submit);
    }
    private void iterator(SQLiteDatabase sqLiteDatabase){
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM task", null);
        String s = new String();
        while (c.moveToNext()) {
            for (int i = 0; i < c.getCount(); i++)
                s = s + c.getString(i) + "   ";
            s=s+"\n";
        }
        Log.i("task", s);
    }
}
package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.linxiangyu.mmbaby.R;
import org.linxiangyu.mmbaby.database.DatabaseHelper;

/**
 * Created by sudongsheng on 14-3-17.
 */
public class MamaActivity extends Activity {

    private Button newTask;
    private ListView historyItem;

    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mama);

        DatabaseHelper dbHelper = new DatabaseHelper(MamaActivity.this, "MMBaby");
        sqLiteDatabase = dbHelper.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM record", null);

        findViewByIds();
        historyItem.setAdapter(new MyAdapter(this, cursor));
        historyItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MamaActivity.this,DetailRecordActivity.class);
                intent.putExtra("position",i);
                startActivity(intent);
            }
        });
        newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MamaActivity.this,NewRecordActivity.class);
                startActivity(intent);
            }
        });


    }

    private void findViewByIds() {
        newTask = (Button) findViewById(R.id.new_task);
        historyItem=(ListView)findViewById(R.id.history_item);
    }

    private class MyAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private Cursor c;

        public MyAdapter(Context context,Cursor c) {
            this.inflater = LayoutInflater.from(context);
            this.c=c;
        }

        @Override
        public int getCount() {
            return c.getCount();
        }
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view=inflater.inflate(R.layout.view_history_item,null);
            TextView title=(TextView)view.findViewById(R.id.history_item_title);
            TextView time=(TextView)view.findViewById(R.id.history_item_time);
            c.moveToPosition(i);
            time.setText(c.getString(c.getColumnIndex("time")));
            title.setText(c.getString(c.getColumnIndex("title")));
            return view;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }
}
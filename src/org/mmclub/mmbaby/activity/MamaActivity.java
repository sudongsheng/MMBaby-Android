package org.mmclub.mmbaby.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.mmclub.mmbaby.R;
import org.mmclub.mmbaby.database.DatabaseHelper;
import org.mmclub.mmbaby.utils.AppConstant;
import org.mmclub.mmbaby.utils.FileUtils;
import org.mmclub.mmbaby.utils.FontManager;
import org.mmclub.mmbaby.utils.MemoryTimeTest;

import java.util.ArrayList;

/**
 * Created by sudongsheng on 14-3-17.
 */
public class MamaActivity extends Activity implements View.OnTouchListener{

    private Button newTask;
    private ListView historyItem;
    private RadioGroup field;
    private ImageView divide;
    private RadioButton morality;
    private RadioButton intelligence;
    private RadioButton physical;
    private TextView status;
    private TextView grade_num1;
    private TextView grade_num2;
    private TextView grade_num3;
    private TextView grade_num4;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;

    private String mField = "morality";

    private MyAdapter adapter;

    private SharedPreferences preferences;

    private int screenWidth;
    private float start = 0.0f;
    private float end = 0.0f;
    private float offset = 0.0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mama);
        init();
    }

    private void init() {
        dbHelper = new DatabaseHelper(MamaActivity.this, "MMBaby");
        sqLiteDatabase = dbHelper.getReadableDatabase();
        cursor = sqLiteDatabase.query("record", null, "field" + "=?", new String[]{mField}, null, null, "time");
//        iterator(sqLiteDatabase);
        findViewByIds();
        setListeners();
        adapter = new MyAdapter(this, cursor);
        historyItem.setAdapter(adapter);

//        MemoryTimeTest.start();
//        ViewGroup v = (ViewGroup) findViewById(R.id.mama_activity);
//        FontManager.changeFonts(v, MamaActivity.this, AppConstant.Mama);
//        MemoryTimeTest.end();
        preferences = PreferenceManager.getDefaultSharedPreferences(MamaActivity.this);
        setGradeText(preferences.getInt("morality_grade", 0));

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        divide.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 3, 5));
    }

    private void setListeners() {
        historyItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MamaActivity.this, NewRecordActivity.class);
                intent.putExtra("Field", mField);
                intent.putExtra("position", adapter.c.getCount()-i-1);
                startActivity(intent);
            }
        });
        historyItem.setOnTouchListener(this);
        newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MamaActivity.this, NewRecordActivity.class);
                intent.putExtra("Field", mField);
                startActivity(intent);
            }
        });
        field.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) divide.getLayoutParams();
                if (i == R.id.morality) {
                    status.setText("品德现状:");
                    mField = "morality";
                    setGradeText(preferences.getInt("morality_grade", 0));
                    adapter.c = sqLiteDatabase.query("record", null, "field" + "=?", new String[]{mField}, null, null, "time");
                    adapter.notifyDataSetChanged();
                    params.setMargins(0, 0, 0, 0);
                } else if (i == R.id.intelligence) {
                    status.setText("智力现状:");
                    mField = "intelligence";
                    setGradeText(preferences.getInt("intelligence_grade", 0));
                    adapter.c = sqLiteDatabase.query("record", null, "field" + "=?", new String[]{mField}, null, null, "time");
                    adapter.notifyDataSetChanged();
                    params.setMargins(screenWidth / 3, 0, 0, 0);
                } else if (i == R.id.physical) {
                    status.setText("体育现状:");
                    mField = "physical";
                    setGradeText(preferences.getInt("physical_grade", 0));
                    adapter.c = sqLiteDatabase.query("record", null, "field" + "=?", new String[]{mField}, null, null, "time");
                    adapter.notifyDataSetChanged();
                    params.setMargins(screenWidth / 3 * 2, 0, 0, 0);
                }
                divide.setLayoutParams(params);
            }
        });
        LinearLayout layout = (LinearLayout) findViewById(R.id.mama_activity);
        layout.setOnTouchListener(this);
    }

    private void setGradeText(int grade) {
//        Log.i("TAG","grade:"+grade);
        grade_num1.setText(grade / 1000 + "");
        grade_num2.setText(grade / 100 % 10 + "");
        grade_num3.setText(grade % 100 / 10 + "");
        grade_num4.setText(grade % 10 + "");
    }

    private void findViewByIds() {
        newTask = (Button) findViewById(R.id.new_task);
        historyItem = (ListView) findViewById(R.id.history_item);
        field = (RadioGroup) findViewById(R.id.field);
        morality = (RadioButton) findViewById(R.id.morality);
        intelligence = (RadioButton) findViewById(R.id.intelligence);
        physical = (RadioButton) findViewById(R.id.physical);
        divide = (ImageView) findViewById(R.id.divide);
        status = (TextView) findViewById(R.id.state_tex);
        grade_num1 = (TextView) findViewById(R.id.grade_num1);
        grade_num2 = (TextView) findViewById(R.id.grade_num2);
        grade_num3 = (TextView) findViewById(R.id.grade_num3);
        grade_num4 = (TextView) findViewById(R.id.grade_num4);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) divide.getLayoutParams();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            start = (int) event.getRawX();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            offset =  start-event.getRawX();
            switch (field.getCheckedRadioButtonId()) {
                case R.id.morality:
                    params.setMargins(offset > 0 ? (int) offset / 3 : 0, 0, 0, 0);
                    break;
                case R.id.intelligence:
                    params.setMargins((int) (screenWidth / 3 + offset / 3), 0, 0, 0);
                    break;
                case R.id.physical:
                    params.setMargins(offset < 0 ? (int) (screenWidth / 3 * 2 + offset / 3) : screenWidth / 3 * 2, 0, 0, 0);
                    break;
            }
            divide.setLayoutParams(params);
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            end = (int) event.getRawX();
            offset = start-end;
            switch (field.getCheckedRadioButtonId()) {
                case R.id.morality: {
                    if (offset > screenWidth / 4) {
                        field.check(R.id.intelligence);
                        params.setMargins(screenWidth / 3, 0, 0, 0);
                    } else
                        params.setMargins(0, 0, 0, 0);
                    break;
                }
                case R.id.intelligence: {
                    if (offset < 0 && offset < (screenWidth / 4 * -1)) {
                        field.check(R.id.morality);
                        params.setMargins(0, 0, 0, 0);
                    } else if (offset > screenWidth / 4) {
                        field.check(R.id.physical);
                        params.setMargins(screenWidth / 3 * 2, 0, 0, 0);
                    } else
                        params.setMargins(screenWidth / 3, 0, 0, 0);
                    break;
                }
                case R.id.physical: {
                    if (offset < (screenWidth / 4 * -1)) {
                        field.check(R.id.intelligence);
                        params.setMargins(screenWidth / 3, 0, 0, 0);
                    } else
                        params.setMargins(screenWidth / 3 * 2, 0, 0, 0);
                    break;
                }
            }
            divide.setLayoutParams(params);
        }
        return false;
    }

    private void iterator(SQLiteDatabase sqLiteDatabase) {
        String s = new String();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM record", null);
        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getCount(); i++)
                s = s + cursor.getString(i) + "   ";
            s = s + "\n";
        }
        Log.i("record", s);
    }

    class ViewHolder{
        TextView title;
        TextView time;
        ImageView[] photo=new ImageView[6];
        LinearLayout photo_linear;
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private Cursor c;

        public MyAdapter(Context context, Cursor c) {
            this.inflater = LayoutInflater.from(context);
            this.c = c;
        }

        @Override
        public int getCount() {
            return c.getCount();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
//            if(view==null) {
                holder=new ViewHolder();
                view = inflater.inflate(R.layout.view_history_item, null);
                holder.title = (TextView) view.findViewById(R.id.history_item_title);
                holder.time = (TextView) view.findViewById(R.id.history_item_time);
                holder.photo[0]=(ImageView)view.findViewById(R.id.photo0);
                holder.photo[1]=(ImageView)view.findViewById(R.id.photo1);
                holder.photo[2]=(ImageView)view.findViewById(R.id.photo2);
                holder.photo[3]=(ImageView)view.findViewById(R.id.photo3);
                holder.photo[4]=(ImageView)view.findViewById(R.id.photo4);
                holder.photo[5]=(ImageView)view.findViewById(R.id.photo5);
                holder.photo_linear=(LinearLayout)view.findViewById(R.id.photo_linear);
//                view.setTag(holder);
//            }else {
//                holder=(ViewHolder)view.getTag();
//            }
            c.moveToPosition(c.getCount()-i-1);
            holder.time.setText(c.getString(c.getColumnIndex("time")).substring(5));
            holder.title.setText(c.getString(c.getColumnIndex("title")));
            Typeface tf = Typeface.createFromAsset(MamaActivity.this.getAssets(), "fonts/mama.ttf");
            holder.time.setTypeface(tf);
            holder.title.setTypeface(tf);
            Log.i("TAG","亲子习惯养成/" + mField + (c.getCount()-i-1));
            ArrayList<String> arrayList = new FileUtils().readFileName("亲子习惯养成/" + mField + (c.getCount()-i-1));
            for(int j=0;j<c.getInt(c.getColumnIndex("photoNum"));j++){
       //         Log.i("TAG", arrayList.get(j));
                Bitmap bitmap = BitmapFactory.decodeFile(arrayList.get(j));
                holder.photo[j].setImageBitmap(bitmap);
            }
            if(c.getInt(c.getColumnIndex("photoNum"))==0){
                holder.photo_linear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0));
                holder.photo_linear.setVisibility(View.INVISIBLE);
            }
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MamaActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void finalize() throws Throwable {
        cursor.close();
        sqLiteDatabase.close();
    }

}
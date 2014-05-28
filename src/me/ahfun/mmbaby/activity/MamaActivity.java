package me.ahfun.mmbaby.activity;

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
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import me.ahfun.mmbaby.R;
import me.ahfun.mmbaby.database.DatabaseHelper;
import me.ahfun.mmbaby.utils.*;
import me.ahfun.mmbaby.widget.IndexableListView;

import java.util.ArrayList;
import java.util.logging.LogRecord;

/**
 * Created by sudongsheng on 14-3-17.
 */
public class MamaActivity extends Activity implements View.OnTouchListener {

    private LinearLayout newTask;
    private IndexableListView historyItem;
    private RadioGroup field;
    private ImageView divide;
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

    private int screenWidth;
    private float start = 0.0f;
    private float end = 0.0f;
    private float offset = 0.0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mama);
        init();
        new Update().start();
    }

    class Update extends Thread {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            historyItem = (IndexableListView) findViewById(R.id.history_item);
            dbHelper = new DatabaseHelper(MamaActivity.this, "MMBaby");
            sqLiteDatabase = dbHelper.getReadableDatabase();
            cursor = sqLiteDatabase.query("record", null, "field" + "=?", new String[]{mField}, null, null, "time");
            adapter = new MyAdapter(MamaActivity.this, cursor);
            historyItem.setFastScrollEnabled(true);
            historyItem.setAdapter(adapter);
            historyItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MamaActivity.this, NewRecordActivity.class);
                    intent.putExtra("Field", mField);
                    intent.putExtra("position", adapter.c.getCount() - i - 1);
                    startActivity(intent);
                }
            });
            historyItem.setOnTouchListener(MamaActivity.this);
        }
    };

    private void init() {
//        dbHelper = new DatabaseHelper(MamaActivity.this, "MMBaby");
//        sqLiteDatabase = dbHelper.getReadableDatabase();
//        cursor = sqLiteDatabase.query("record", null, "field" + "=?", new String[]{mField}, null, null, "time");
        findViewByIds();
        setListeners();
//        adapter = new MyAdapter(this, cursor);
//        historyItem.setAdapter(adapter);

//        MemoryTimeTest.start();
//        ViewGroup v = (ViewGroup) findViewById(R.id.mama_activity);
//        FontManager.changeFonts(v, MamaActivity.this, AppConstant.Mama);
//        MemoryTimeTest.end();
        setGradeText((Integer) SharedPreferencesUtils.getParam(MamaActivity.this, "morality_grade", 0));

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        divide.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 3, 5));
    }

    private void setListeners() {
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
                    setGradeText((Integer) SharedPreferencesUtils.getParam(MamaActivity.this, "morality_grade", 0));
                    adapter.c = sqLiteDatabase.query("record", null, "field" + "=?", new String[]{mField}, null, null, "time");

                    params.setMargins(0, 0, 0, 0);
                } else if (i == R.id.intelligence) {
                    status.setText("智力现状:");
                    mField = "intelligence";
                    setGradeText((Integer) SharedPreferencesUtils.getParam(MamaActivity.this, "intelligence_grade", 0));
                    adapter.c = sqLiteDatabase.query("record", null, "field" + "=?", new String[]{mField}, null, null, "time");
                    params.setMargins(screenWidth / 3, 0, 0, 0);
                } else if (i == R.id.physical) {
                    status.setText("体育现状:");
                    mField = "physical";
                    setGradeText((Integer) SharedPreferencesUtils.getParam(MamaActivity.this, "physical_grade", 0));
                    adapter.c = sqLiteDatabase.query("record", null, "field" + "=?", new String[]{mField}, null, null, "time");
                    params.setMargins(screenWidth / 3 * 2, 0, 0, 0);
                }
                adapter.notifyDataSetChanged();
                historyItem.fresh();
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
        newTask = (LinearLayout) findViewById(R.id.new_task);
//        historyItem = (ListView) findViewById(R.id.history_item);
        field = (RadioGroup) findViewById(R.id.field);
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
            offset = start - event.getRawX();
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
            offset = start - end;
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

    class ViewHolder {
        TextView title;
        TextView time;
        ImageView[] photo = new ImageView[6];
        LinearLayout photo_linear;
    }

    private class MyAdapter extends BaseAdapter implements SectionIndexer {
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
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.view_history_item, null);
            holder.title = (TextView) view.findViewById(R.id.history_item_title);
            holder.time = (TextView) view.findViewById(R.id.history_item_time);
            holder.photo[0] = (ImageView) view.findViewById(R.id.photo0);
            holder.photo[1] = (ImageView) view.findViewById(R.id.photo1);
            holder.photo[2] = (ImageView) view.findViewById(R.id.photo2);
            holder.photo[3] = (ImageView) view.findViewById(R.id.photo3);
            holder.photo[4] = (ImageView) view.findViewById(R.id.photo4);
            holder.photo[5] = (ImageView) view.findViewById(R.id.photo5);
            holder.photo_linear = (LinearLayout) view.findViewById(R.id.photo_linear);
//                view.setTag(holder);
//            }else {
//                holder=(ViewHolder)view.getTag();
//            }
            c.moveToPosition(c.getCount() - i - 1);
            holder.time.setText(c.getString(c.getColumnIndex("time")).substring(5));
            holder.title.setText(c.getString(c.getColumnIndex("title")));
            Typeface tf = Typeface.createFromAsset(MamaActivity.this.getAssets(), "fonts/mama.ttf");
            holder.time.setTypeface(tf);
            holder.title.setTypeface(tf);
            Log.i("TAG", "亲子习惯养成/" + mField + "/" + c.getString(c.getColumnIndex("time")) + "-" + c.getInt(c.getColumnIndex("primary_key")));
            ArrayList<String> arrayList = new FileUtils().readFileName("亲子习惯养成/" + mField + "/" + c.getString(c.getColumnIndex("time")) + "-" + c.getInt(c.getColumnIndex("primary_key")));
            for (int j = 0; j < c.getInt(c.getColumnIndex("photoNum")); j++) {
                Log.i("TAG", arrayList.get(j));
                Bitmap bitmap = BitmapFactory.decodeFile(arrayList.get(j));
                final int h = bitmap.getHeight();
                final int w = bitmap.getWidth();
                Matrix matrix = new Matrix();
                if (w > h) {
                    matrix.postRotate(90);
                    Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
                    bitmap.recycle();
                    holder.photo[j].setImageBitmap(resizeBmp);
                } else {
                    holder.photo[j].setImageBitmap(bitmap);
                }

            }
            if (c.getInt(c.getColumnIndex("photoNum")) == 0) {
                holder.photo_linear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
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

        @Override
        public int getSectionForPosition(int i) {
            return 0;
        }

        @Override
        public int getPositionForSection(int i) {
            String temp = "";
            if (c.getCount() > 0) {

                Log.i("TAG", getSections()[i].toString());
                String[] mSections = (String[]) getSections();
                if (c.moveToLast()) {
                    do {
                        if (c.getString(c.getColumnIndex("time")).substring(0, 7).equals(mSections[i])) {
                            Log.i("TAG", "position:"+(c.getCount()-1-c.getPosition()));
                            return c.getCount()-1-c.getPosition();
                        }
                    } while (c.moveToPrevious());
                }

            }
            return c.getCount() - 1;
        }

        @Override
        public Object[] getSections() {
            ArrayList<String> sections = new ArrayList<String>();
            String temp = "";
            if (c.getCount() > 0) {
                if(c.moveToFirst()) {
                    do {
                        if (!c.getString(c.getColumnIndex("time")).substring(0, 7).equals(temp)) {
                            sections.add(c.getString(c.getColumnIndex("time")).substring(0, 7));
                        }
                        temp = c.getString(c.getColumnIndex("time")).substring(0, 7);
                    } while (c.moveToNext());
                }
                String s[] = new String[sections.size()];
                for (int i = 0; i < sections.size(); i++) {
                    s[i] = sections.get(sections.size() - 1 - i);
                }
                return s;
            }
            return null;
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
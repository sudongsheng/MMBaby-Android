package me.ahfun.mmbaby.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import me.ahfun.mmbaby.R;
import me.ahfun.mmbaby.database.DatabaseHelper;
import me.ahfun.mmbaby.database.Record;
import me.ahfun.mmbaby.utils.AppConstant;
import me.ahfun.mmbaby.utils.CustomDialog;
import me.ahfun.mmbaby.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by helloworld on 14-3-17.
 */
public class BabyActivity extends Activity {

    private Button petDog;
    private Button petChick;
    private Button petSunFlower;
    private Button babyBack;
    private TextView babyBg;
    SoundPool soundPool;
    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    ArrayList<ArrayList<String>> arrayLists = new ArrayList<ArrayList<String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby);

        dbHelper = new DatabaseHelper(BabyActivity.this, "MMBaby");
        sqLiteDatabase = dbHelper.getReadableDatabase();
        cursor = sqLiteDatabase.query("record", null, null, null, null, null, "time");
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex("isCheck")) == AppConstant.FALSE) {
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(cursor.getString(cursor.getColumnIndex("title")));
                arrayList.add(cursor.getString(cursor.getColumnIndex("field")));
                arrayList.add(cursor.getInt(cursor.getColumnIndex("rating")) + "");
                arrayList.add(cursor.getString(cursor.getColumnIndex("time")));
                arrayList.add(cursor.getInt(cursor.getColumnIndex("primary_key")) + "");
                arrayLists.add(arrayList);
            }
        }
        if (arrayLists.size() != 0) {
            final CustomDialog customDialog = new CustomDialog(BabyActivity.this, R.layout.view_account, R.style.settingDialog);
            customDialog.show();

            for (int i = 0; i < arrayLists.size(); i++) {
                ContentValues cv = new ContentValues();
                cv.put("isCheck", AppConstant.TRUE);
                sqLiteDatabase.update("record", cv, "primary_key=?", new String[]{String.valueOf(arrayLists.get(i).get(4))});
            }
            customDialog.setCancelable(true);
            customDialog.setCanceledOnTouchOutside(true);
            ListView listView = (ListView) customDialog.findViewById(R.id.account_list);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    customDialog.dismiss();
                }
            });
            listView.setAdapter(new ListAdapter(BabyActivity.this));
        }

        petDog = (Button) findViewById(R.id.petDog);
        petChick = (Button) findViewById(R.id.petChick);
        petSunFlower = (Button) findViewById(R.id.petSunFlower);
        babyBack = (Button) findViewById(R.id.babyBack);
        babyBg = (TextView) findViewById(R.id.babyBg);
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

        soundMap.put(AppConstant.PET_PLANT, soundPool.load(BabyActivity.this, R.raw.sunlight, 1));
        soundMap.put(AppConstant.PET_CHICK, soundPool.load(BabyActivity.this, R.raw.crow, 1));
        soundMap.put(AppConstant.PET_DOG, soundPool.load(BabyActivity.this, R.raw.bark, 1));
        soundMap.put(4, soundPool.load(this, R.raw.button_clicked, 1));

        petDog.setOnClickListener(new PetClickListener("旺财", AppConstant.PET_DOG, "intelligence"));
        petChick.setOnClickListener(new PetClickListener("鸡仔", AppConstant.PET_CHICK, "physical"));
        petSunFlower.setOnClickListener(new PetClickListener("小葵", AppConstant.PET_PLANT, "morality"));

        babyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(soundMap.get(4), 1, 1, 0, 0, 1);
                onBackPressed();
                BabyActivity.this.finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        petDog = (Button) findViewById(R.id.petDog);
        petChick = (Button) findViewById(R.id.petChick);
        petSunFlower = (Button) findViewById(R.id.petSunFlower);
        babyBack = (Button) findViewById(R.id.babyBack);
        babyBg = (TextView) findViewById(R.id.babyBg);

    }

    class ListAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private int rate;

        public ListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayLists.size();
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.view_account_item, null);
            TextView title = (TextView) view.findViewById(R.id.account_title);
            TextView field_money = (TextView) view.findViewById(R.id.account_field_money);
            TextView field_integral = (TextView) view.findViewById(R.id.account_field_integral);
            TextView rate_money = (TextView) view.findViewById(R.id.account_rating_money);
            TextView rate_integral = (TextView) view.findViewById(R.id.account_rating_integral);
            TextView time = (TextView) view.findViewById(R.id.account_time);
            ImageView img1 = (ImageView) view.findViewById(R.id.account_img1);
            ImageView img2 = (ImageView) view.findViewById(R.id.account_img2);
            Animation alpha = AnimationUtils.loadAnimation(BabyActivity.this, R.anim.account_add);
            img1.startAnimation(alpha);
            img2.startAnimation(alpha);
            title.setText(arrayLists.get(i).get(0));
            if (arrayLists.get(i).get(1).equals("morality")) {
                field_money.setText("葵葵的金钱增加");
                field_integral.setText("葵葵的经验增加");
                rate = (Integer) SharedPreferencesUtils.getParam(BabyActivity.this, "morality_rate", 100);
            } else if (arrayLists.get(i).get(1).equals("intelligence")) {
                field_money.setText("狗狗的金钱增加");
                field_integral.setText("狗狗的经验增加");
                rate = (Integer) SharedPreferencesUtils.getParam(BabyActivity.this, "intelligence_rate", 100);
            } else if (arrayLists.get(i).get(1).equals("physical")) {
                field_money.setText("小鸡仔的金钱增加");
                field_integral.setText("小鸡仔的经验增加");
                rate = (Integer) SharedPreferencesUtils.getParam(BabyActivity.this, "physical_rate", 100);
            }
            rate_money.setText(Integer.parseInt(arrayLists.get(i).get(2)) * rate + "");
            rate_integral.setText(Integer.parseInt(arrayLists.get(i).get(2)) * rate + "");
            time.setText(arrayLists.get(i).get(3));
            return view;
        }
    }
//    @Override
//    public void onStop(){
//        super.onStop();
//        recycleBitmap(petDog);
//        recycleBitmap(petChick);
//        recycleBitmap(petSunFlower);
//        recycleBitmap(babyBack);
//        recycleBitmap(babyBg);
//        Log.i("TAG","onStop");
//
//    }
//    private void recycleBitmap(View v){
//        if(v != null)
//        {
//           v.setBackground(null);
//
//        }
//    }

    public class PetClickListener implements View.OnClickListener {
        private int petId;
        private Intent intent;

        public PetClickListener(String petsName, int petId, String integral_type) {
            this.petId = petId;
            intent = new Intent(BabyActivity.this, PetsActivity.class);
            intent.putExtra("petId", petId);
            intent.putExtra("petsName", petsName);
            intent.putExtra("integral_type", integral_type);

        }

        @Override
        public void onClick(View view) {
            soundPool.play(soundMap.get(petId), 1, 1, 0, 0, 1);
            startActivity(intent);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        cursor.close();
        dbHelper.close();
        sqLiteDatabase.close();
    }
}

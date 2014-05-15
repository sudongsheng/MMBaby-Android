package me.ahfun.mmbaby.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import me.ahfun.mmbaby.R;
import me.ahfun.mmbaby.database.BabyData;
import me.ahfun.mmbaby.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Inner on 14-3-18.
 */
public class PetsActivity extends Activity {

    private LinearLayout linearLayout;
    private TextView petLeveltv;
    private TextView petNametv;
    private TextView petTypetv;
    private Button marketButton;
    private Button petBackButton;
    private ImageView petImageiv;

    private String petName;
    private ProgressBar progressBar;
    private SharedPreferences preferences;
    private GridView gridView;
    private List<Map<String, Integer>> petsData;
    public int[] ownedNumber = new int[]{0, 0, 0, 0, 0};
    private Pets pets;
    private Handler handler;
    private int integral;
    private int petId;
    private String integral_type;
    private Typeface tf;

    SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MemoryTimeTest.start();
        setContentView(R.layout.activity_pets);
        MemoryTimeTest.end();
        petName = getIntent().getStringExtra("petsName");
        petId = getIntent().getIntExtra("petId", 0);
        integral_type = getIntent().getStringExtra("integral_type");
        tf = Typeface.createFromAsset(PetsActivity.this.getAssets(), "fonts/baby.ttf");
        soundMap.put(1, soundPool.load(this, R.raw.dialog_show, 1));
        soundMap.put(2, soundPool.load(this, R.raw.button_clicked, 1));


        initView();
        //获取对应积分
        preferences = PreferenceManager.getDefaultSharedPreferences(PetsActivity.this);
        //实例化pet类的对象
        //获取已经存好的拥有的道具数量
        for (int i = 0; i < 4; i++) {
            ownedNumber[i] = preferences.getInt(petName + "ownedNumber" + i, 0);
        }


        //给两个button设置监听，执行相应操作
        marketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);
                Intent intent = new Intent(PetsActivity.this, MarketActivity.class);
                intent.putExtra("petId", petId);
                intent.putExtra("petsName", petName);
                startActivity(intent);
            }
        });
        petBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);
                onBackPressed();
                PetsActivity.this.finish();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        //preferences= PreferenceManager.getDefaultSharedPreferences(PetsActivity.this);
        for (int i = 0; i < 4; i++) {
            ownedNumber[i] = preferences.getInt(petName + "ownedNumber" + i, 0);
        }
        pets = new Pets();
        integral = preferences.getInt(integral_type + "_integral", 0);
        pets.levelUp(integral);
        switch (petId) {
            case AppConstant.PET_PLANT:
                initPets(R.drawable.morality_bg, petName, pets.getPetsLevel(), BabyData.sunflower_image[pets.getPetsLevel()],
                        "品德", pets.getNeedIntegral(), pets.getCurrentIntegral());
                petsData = getData(BabyData.morality_buttonImage, ownedNumber);
                break;
            case AppConstant.PET_CHICK:
                initPets(R.drawable.physical_bg, petName, pets.getPetsLevel(), BabyData.chick_image[pets.getPetsLevel()],
                        "体育", pets.getNeedIntegral(), pets.getCurrentIntegral());
                petsData = getData(BabyData.physical_buttonImage, ownedNumber);
                break;
            case AppConstant.PET_DOG:
                initPets(R.drawable.intelligence_bg, petName, pets.getPetsLevel(), BabyData.dog_image[pets.getPetsLevel()],
                        "智力", pets.getNeedIntegral(), pets.getCurrentIntegral());
                petsData = getData(BabyData.intelligence_buttonImage, ownedNumber);
                break;
        }

        MyAdapter adapter = new MyAdapter(PetsActivity.this,ownedNumber);
        gridView.setAdapter(adapter);

    }

    //获得view控件显示
    private void initView() {
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        petLeveltv = (TextView) findViewById(R.id.petLevel);
        petNametv = (TextView) findViewById(R.id.petName);
        petTypetv = (TextView) findViewById(R.id.petType);
        marketButton = (Button) findViewById(R.id.marketButton);
        petBackButton = (Button) findViewById(R.id.petBack);
        petImageiv = (ImageView) findViewById(R.id.petImage);
        progressBar = (ProgressBar) findViewById(R.id.levelProgressBar);
        gridView = (GridView) findViewById(R.id.gridView);
    }

    //初始化view
    private void initPets(int petBackground, String petName, int petLevel, int petImageId,
                          String petType, int needIntegral, int currentIntegral) {
        linearLayout.setBackgroundResource(petBackground);
        petLeveltv.setText("LV" + petLevel);
        petLeveltv.setTypeface(tf);
        petNametv.setText(petName);
        petNametv.setTypeface(tf);
        petTypetv.setText(petType);
        petTypetv.setTypeface(tf);
        petImageiv.setBackgroundResource(petImageId);
        progressBar.setMax(needIntegral);
        progressBar.setProgress(currentIntegral);
    }

    //生成所需的数据
    private List<Map<String, Integer>> getData(int[] buttonImage, int[] ownedNumber) {
        List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
        for (int i = 0; i < 4; i++) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            //map.put("goodsName", goodsName[i]);
            //map.put("ownedNumber", ownedNumber[i]);
            map.put("ownedNumber", ownedNumber[i]);
            map.put("buttonImage", buttonImage[i]);
            list.add(map);
        }
        return list;
    }

    //自定义adapter获取数据
    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private int[] own;

        public MyAdapter(Context context,int[] own) {
            this.mInflater = LayoutInflater.from(context);
            this.own=own;
        }

        @Override
        public int getCount() {
            return petsData.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }
        public void fresh(int[] own){
            this.own=own;
            this.notifyDataSetChanged();
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.pets_list, null);
            Button useButton = (Button) convertView.findViewById(R.id.useButton);
            TextView hadNumber = (TextView) convertView.findViewById(R.id.hadNumber);

            hadNumber.setText("" + own[position]);
            hadNumber.setTypeface(tf);
            useButton.setBackgroundResource(petsData.get(position).get("buttonImage"));
            useButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (own[position] <= 0) {
                        soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
                        final CustomDialog dialog = new CustomDialog(PetsActivity.this, R.layout.view_dialog, R.style.settingDialog);
                        dialog.show();
                        TextView dialogText = (TextView) dialog.findViewById(R.id.dialogText);
                        dialogText.setText("已经没有啦，去商店买一些吧！");
                        dialogText.setTypeface(tf);
                        Button knowButton = (Button) dialog.findViewById(R.id.dialogButton);
                        knowButton.setTypeface(tf);
                        knowButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PetsActivity.this, MarketActivity.class);
                                intent.putExtra("petId", petId);
                                intent.putExtra("petsName", petName);
                                startActivity(intent);
                                soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);
                                dialog.dismiss();
                            }
                        });
                        Button cancel = (Button) dialog.findViewById(R.id.dialogButton_cancel);
                        cancel.setTypeface(tf);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        own[position]--;
                        switch (petId) {
                            case AppConstant.PET_PLANT:
                                integral = integral + BabyData.morality_up[position];
                                initPets(R.drawable.morality_bg, petName, pets.getPetsLevel(), BabyData.sunflower_image[pets.getPetsLevel()],
                                        "品德", pets.getNeedIntegral(), pets.getCurrentIntegral());
                                AnimationDrawable animationDrawable;
                                //测试动画效果
                                petImageiv.setBackgroundResource(BabyData.morality_animation[pets.getPetsLevel()][position]);
                                animationDrawable = (AnimationDrawable) petImageiv.getBackground();
                                animationDrawable.start();
                                break;
                            case AppConstant.PET_CHICK:
                                initPets(R.drawable.physical_bg, petName, pets.getPetsLevel(), BabyData.chick_image[pets.getPetsLevel()],
                                        "体育", pets.getNeedIntegral(), pets.getCurrentIntegral());
                                //测试动画效果
                                petImageiv.setBackgroundResource(BabyData.physical_animation0[pets.getPetsLevel()][position]);
                                animationDrawable = (AnimationDrawable) petImageiv.getBackground();
                                animationDrawable.start();
                                integral = integral + BabyData.physical_up[position];
                                break;
                            case AppConstant.PET_DOG:
                                integral = integral + BabyData.intelligence_up[position];
                                initPets(R.drawable.intelligence_bg, petName, pets.getPetsLevel(), BabyData.dog_image[pets.getPetsLevel()],
                                        "智力", pets.getNeedIntegral(), pets.getCurrentIntegral());
                                petImageiv.setBackgroundResource(BabyData.intelligence_animation[pets.getPetsLevel()][position]);
                                animationDrawable = (AnimationDrawable) petImageiv.getBackground();
                                animationDrawable.start();
                                break;
                        }
                        SharedPreferencesUtils.setParam(PetsActivity.this,integral_type + "_integral", integral);
                        SharedPreferencesUtils.setParam(PetsActivity.this,getIntent().getStringExtra("petsName") + "ownedNumber" + position, own[position]);
                        integral = preferences.getInt(integral_type + "_integral", 0);
                        pets = new Pets();
                        pets.levelUp(integral);
                        fresh(own);
                    }
                }
            });
            return convertView;
        }

    }

}

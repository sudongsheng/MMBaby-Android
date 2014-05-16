package me.ahfun.mmbaby.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import me.ahfun.mmbaby.R;
import me.ahfun.mmbaby.database.BabyData;
import me.ahfun.mmbaby.utils.AppConstant;
import me.ahfun.mmbaby.utils.CustomDialog;
import me.ahfun.mmbaby.utils.FontManager;
import me.ahfun.mmbaby.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Inner on 14-3-18.
 */
public class MarketActivity extends Activity {

    protected static final int CHANGE_UI = 1;
    protected static final int TOAST = 2;
    private List<Map<String, Object>> marketData;
    public int[] ownedNumber = new int[]{0, 0, 0, 0};
    private int[] goodListBg = new int[]{R.drawable.goods_list_bg0,R.drawable.goods_list_bg1,
            R.drawable.goods_list_bg2,R.drawable.goods_list_bg3};
    private GridView goodsGridView;

    private TextView moneyTextview;
    private TextView money_name;
    private SharedPreferences preferences;
    private int money;
    private int petId;
    private String petName;
    private Typeface tf;
    private Button marketBack;

    SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
    HashMap<Integer,Integer> soundMap = new HashMap<Integer, Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        tf = Typeface.createFromAsset(MarketActivity.this.getAssets(), "fonts/baby.ttf");

        marketBack = (Button)findViewById(R.id.marketBack);
        moneyTextview = (TextView) findViewById(R.id.moneyTextView);
        money_name = (TextView) findViewById(R.id.money_name);
        goodsGridView = (GridView) findViewById(R.id.goodsGridView);
        preferences = PreferenceManager.getDefaultSharedPreferences(MarketActivity.this);
        money = preferences.getInt("money", 0);
        moneyTextview.setText(": "+money);
        money_name.setTypeface(tf);
        moneyTextview.setTypeface(tf);
        petId = getIntent().getIntExtra("petId", 0);
        petName = getIntent().getStringExtra("petsName");

        soundMap.put(1,soundPool.load(this,R.raw.dialog_show,1));
        soundMap.put(2,soundPool.load(this,R.raw.button_clicked,1));
        soundMap.put(3,soundPool.load(this,R.raw.buy_clicked,1));
        soundMap.put(4,soundPool.load(this,R.raw.know_clicked,1));

        marketBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(soundMap.get(2),1,1,0,0,1);
                onBackPressed();
                MarketActivity.this.finish();
            }
        });

        for (int i = 0; i < 4; i++) {
            ownedNumber[i] = preferences.getInt(petName + "ownedNumber" + i, 0);
        }

        switch (petId) {
            case AppConstant.PET_PLANT:
                marketData = getData(BabyData.morality_goodsName,
                        ownedNumber, BabyData.morality_goodsImage,
                        BabyData.morality_needMoney, BabyData.morality_up);
                break;
            case AppConstant.PET_CHICK:
                marketData = getData(BabyData.physical_goodsName,
                        ownedNumber, BabyData.physical_goodsImage,
                        BabyData.physical_needMoney, BabyData.physical_up);
                break;
            case AppConstant.PET_DOG:
                marketData = getData(BabyData.intelligence_goodsName,
                        ownedNumber, BabyData.intelligence_goodsImage,
                        BabyData.intelligence_needMoney, BabyData.intelligence_up);
                break;
        }

        MyAdapter adapter = new MyAdapter(MarketActivity.this,ownedNumber);
        goodsGridView.setAdapter(adapter);
    }

    private List<Map<String, Object>> getData(String[] goodsName, int[] ownedNumber, int[] goodsImage,
                                              int[] needMoney, int[] up) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < goodsName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("goodsName", goodsName[i]);
            map.put("ownedNumber", ownedNumber[i]);
            map.put("goodsImage", goodsImage[i]);
            map.put("needMoney", needMoney[i]);
            map.put("up", up[i]);
            map.put("goodsListBg",goodListBg[i]);

            list.add(map);
        }
        return list;
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private int[] own;

        public MyAdapter(Context context,int[] own) {
            this.mInflater = LayoutInflater.from(context);
            this.own=own;
        }

        @Override
        public int getCount() {
            return marketData.size();
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
            convertView = mInflater.inflate(R.layout.market_list, null);
            ImageView goodsImage= (ImageView) convertView.findViewById(R.id.goodsImage);
            TextView goodsName= (TextView) convertView.findViewById(R.id.goodsName);
            TextView ownedNumber= (TextView) convertView.findViewById(R.id.ownedNumber);
            TextView needMoney= (TextView) convertView.findViewById(R.id.needMoney);
            TextView up= (TextView) convertView.findViewById(R.id.up);
            Button buy= (Button) convertView.findViewById(R.id.buy);

            LinearLayout linearLayout= (LinearLayout)convertView.findViewById(R.id.market_list_lineaeLayout);
            linearLayout.setBackgroundResource((Integer) marketData.get(position).get("goodsListBg"));
            goodsImage.setImageResource((Integer) marketData.get(position).get("goodsImage"));
            goodsName.setText((String) marketData.get(position).get("goodsName"));
            goodsName.setTypeface(tf);
            ownedNumber.setText("已拥有：" + own[position]);
            ownedNumber.setTypeface(tf);
            needMoney.setText("  " + marketData.get(position).get("needMoney"));
            needMoney.setTypeface(tf);
            up.setText("经验+" + marketData.get(position).get("up"));
            up.setTypeface(tf);

            buy.setTag(position);
            buy.setTypeface(tf);
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (petId){
                        case AppConstant.PET_PLANT:
                            money = money - BabyData.morality_needMoney[position];
                            break;
                        case AppConstant.PET_CHICK:
                            money = money - BabyData.physical_needMoney[position];
                            break;
                        case AppConstant.PET_DOG:
                            money = money - BabyData.intelligence_needMoney[position];
                            break;
                    }
                    if (money < 0) {
                        switch (petId){
                            case AppConstant.PET_PLANT:
                                money = money + BabyData.morality_needMoney[position];
                                break;
                            case AppConstant.PET_CHICK:
                                money = money + BabyData.physical_needMoney[position];
                                break;
                            case AppConstant.PET_DOG:
                                money = money + BabyData.intelligence_needMoney[position];
                                break;
                        }
                        soundPool.play(soundMap.get(1),1,1,0,0,1);
                        final CustomDialog dialog = new CustomDialog(MarketActivity.this,R.layout.view_dialog_market,R.style.settingDialog);
                        dialog.show();
                        TextView dialogText = (TextView)dialog.findViewById(R.id.dialog_market_text);
                        dialogText.setText("没有金币啦，好好表现，让妈妈奖励吧！！");
                        dialogText.setTypeface(tf);
                        Button cancel=(Button)dialog.findViewById(R.id.dialog_market_cancel);
                        cancel.setTypeface(tf);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                soundPool.play(soundMap.get(4),1,1,0,0,1);
                                dialog.dismiss();
                            }
                        });
                    } else {
                        own[position]++;
                        SharedPreferencesUtils.setParam(MarketActivity.this, "money", money);
                        SharedPreferencesUtils.setParam(MarketActivity.this, getIntent().getStringExtra("petsName") + "ownedNumber" + position, own[position]);
                        money = preferences.getInt("money", 0);
                        moneyTextview.setText(": " + money);

                        for (int i = 0; i < 4; i++) {
                            own[i] = preferences.getInt(petName + "ownedNumber" + i, 0);
                        }
                        switch (petId) {
                            case AppConstant.PET_PLANT:
                                marketData = getData(BabyData.morality_goodsName,
                                        own, BabyData.morality_goodsImage,
                                        BabyData.morality_needMoney, BabyData.morality_up);
                                break;
                            case AppConstant.PET_CHICK:
                                marketData = getData(BabyData.physical_goodsName,
                                        own, BabyData.physical_goodsImage,
                                        BabyData.physical_needMoney, BabyData.physical_up);
                                break;
                            case AppConstant.PET_DOG:
                                marketData = getData(BabyData.intelligence_goodsName,
                                        own, BabyData.intelligence_goodsImage,
                                        BabyData.intelligence_needMoney, BabyData.intelligence_up);
                                break;
                        }
                    }
                    soundPool.play(soundMap.get(3),1,1,0,0,1);
                    fresh(own);
                }
            });
            return convertView;
        }

    }
}

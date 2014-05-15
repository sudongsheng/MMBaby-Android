package me.ahfun.mmbaby.activity;

import android.app.Activity;
import android.content.Context;
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
    private ViewHolder holder;

    private TextView moneyTextview;
    private SharedPreferences preferences;
    private int money;
    private int petId;
    private String petName;
    private Typeface tf;
    private Button marketBack;

    SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
    HashMap<Integer,Integer> soundMap = new HashMap<Integer, Integer>();

    //主线程创建消息处理器
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        tf = Typeface.createFromAsset(MarketActivity.this.getAssets(), "fonts/baby.ttf");

        marketBack = (Button)findViewById(R.id.marketBack);
        moneyTextview = (TextView) findViewById(R.id.moneyTextView);
        goodsGridView = (GridView) findViewById(R.id.goodsGridView);
        preferences = PreferenceManager.getDefaultSharedPreferences(MarketActivity.this);
        money = preferences.getInt("money", 0);
        moneyTextview.setText("金币：" + money);
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

        MyAdapter adapter = new MyAdapter(MarketActivity.this);
        goodsGridView.setAdapter(adapter);

        //消息处理器
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == CHANGE_UI) {
                    money = preferences.getInt("money", 0);
                    moneyTextview.setText("金币：" + money);

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
                    MyAdapter adapter = new MyAdapter(MarketActivity.this);
                    goodsGridView.setAdapter(adapter);

                } else {
                    soundPool.play(soundMap.get(1),1,1,0,0,1);
                    final CustomDialog dialog = new CustomDialog(MarketActivity.this,R.layout.view_dialog,R.style.settingDialog);
                    dialog.show();
                    TextView dialogText = (TextView)dialog.findViewById(R.id.dialogText);
                    dialogText.setText("没有金币啦，好好表现，让妈妈奖励吧！！");
                    dialogText.setTypeface(tf);
                    Button knowButton = (Button)dialog.findViewById(R.id.dialogButton);
                    knowButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            soundPool.play(soundMap.get(4),1,1,0,0,1);
                            dialog.dismiss();
                        }
                    });
                }
            }
        };
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



    public final class ViewHolder {
        public ImageView goodsImage;
        public TextView goodsName;
        public TextView ownedNumber;
        public TextView needMoney;
        public TextView up;
        public Button buy;
        public LinearLayout linearLayout;
    }



    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;


        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return marketData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyListener myListener = new MyListener(position, getIntent().getStringExtra("petsName"));
            if (convertView == null) {

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.market_list, null);
                holder.goodsImage = (ImageView) convertView.findViewById(R.id.goodsImage);
                holder.goodsName = (TextView) convertView.findViewById(R.id.goodsName);
                holder.ownedNumber = (TextView) convertView.findViewById(R.id.ownedNumber);
                holder.needMoney = (TextView) convertView.findViewById(R.id.needMoney);
                holder.up = (TextView) convertView.findViewById(R.id.up);
                holder.buy = (Button) convertView.findViewById(R.id.buy);
                holder.linearLayout = (LinearLayout)convertView.findViewById(R.id.market_list_lineaeLayout);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.linearLayout.setBackgroundResource((Integer) marketData.get(position).get("goodsListBg"));
            holder.goodsImage.setImageResource((Integer) marketData.get(position).get("goodsImage"));
            holder.goodsName.setText((String) marketData.get(position).get("goodsName"));
            holder.goodsName.setTypeface(tf);
            holder.ownedNumber.setText("已拥有：" + marketData.get(position).get("ownedNumber"));
            holder.ownedNumber.setTypeface(tf);
            holder.needMoney.setText("  " + marketData.get(position).get("needMoney"));
            holder.needMoney.setTypeface(tf);
            holder.up.setText("经验+" + marketData.get(position).get("up"));
            holder.up.setTypeface(tf);

            holder.buy.setTag(position);
            holder.buy.setTypeface(tf);
            holder.buy.setOnClickListener(myListener);


            return convertView;
        }

    }

    private class MyListener implements View.OnClickListener {
        int mPosition;
        String mPetName;

        public MyListener(int inPosition, String petName) {
            mPosition = inPosition;
            mPetName = petName;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (petId){
                case AppConstant.PET_PLANT:
                    money = money - BabyData.morality_needMoney[mPosition];
                    break;
                case AppConstant.PET_CHICK:
                    money = money - BabyData.physical_needMoney[mPosition];
                    break;
                case AppConstant.PET_DOG:
                    money = money - BabyData.intelligence_needMoney[mPosition];
                    break;
            }
            Message message = new Message();
            SharedPreferences.Editor editor = preferences.edit();
            if (money < 0) {
                message.what = TOAST;
                switch (petId){
                    case AppConstant.PET_PLANT:
                        money = money + BabyData.morality_needMoney[mPosition];
                        break;
                    case AppConstant.PET_CHICK:
                        money = money + BabyData.physical_needMoney[mPosition];
                        break;
                    case AppConstant.PET_DOG:
                        money = money + BabyData.intelligence_needMoney[mPosition];
                        break;
                }
            } else {
                ownedNumber[mPosition]++;
                editor.putInt("money", money);
                editor.putInt(mPetName + "ownedNumber" + mPosition, ownedNumber[mPosition]);
                editor.commit();
                message.what = CHANGE_UI;
            }
            soundPool.play(soundMap.get(3),1,1,0,0,1);
            message.obj = mPosition;
            handler.sendMessage(message);
        }
    }
}

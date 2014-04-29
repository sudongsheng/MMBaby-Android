package org.mmclub.mmbaby.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.mmclub.mmbaby.R;
import org.mmclub.mmbaby.database.BabyData;
import org.mmclub.mmbaby.utils.AppConstant;
import org.mmclub.mmbaby.utils.FontManager;

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
    private ListView listView;
    private ViewHolder holder;

    private TextView moneyTextview;
    private SharedPreferences preferences;
    private int money;
    private int petId;
    private String petName;
    //主线程创建消息处理器
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        ViewGroup v = (ViewGroup) findViewById(R.id.market_linearLayout);
        FontManager.changeFonts(v, MarketActivity.this, AppConstant.Baby);

        moneyTextview = (TextView) findViewById(R.id.moneyTextView);
        listView = (ListView) findViewById(R.id.listView);
        preferences = PreferenceManager.getDefaultSharedPreferences(MarketActivity.this);
        money = preferences.getInt("money", 0);
        moneyTextview.setText("金币：" + money);
        petId = getIntent().getIntExtra("petId", 0);
        petName = getIntent().getStringExtra("petsName");

        for (int i = 0; i < 4; i++) {
            ownedNumber[i] = preferences.getInt(petName + "ownedNumber" + i, 0);
        }

        switch (petId) {
            case 1:
                marketData = getData(BabyData.morality_goodsName,
                        ownedNumber, BabyData.morality_goodsImage);
                break;
            case 2:
                marketData = getData(BabyData.physical_goodsName,
                        ownedNumber, BabyData.physical_goodsImage);
                break;
            case 3:
                marketData = getData(BabyData.intelligence_goodsName,
                        ownedNumber, BabyData.intelligence_goodsImage);
                break;
        }

        MyAdapter adapter = new MyAdapter(MarketActivity.this);
        listView.setAdapter(adapter);

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
                        case 1:
                            marketData = getData(BabyData.morality_goodsName,
                                    ownedNumber, BabyData.morality_goodsImage);
                            break;
                        case 2:
                            marketData = getData(BabyData.physical_goodsName,
                                    ownedNumber, BabyData.physical_goodsImage);
                            break;
                        case 3:
                            marketData = getData(BabyData.intelligence_goodsName,
                                    ownedNumber, BabyData.intelligence_goodsImage);
                            break;
                    }

                    MyAdapter adapter = new MyAdapter(MarketActivity.this);
                    listView.setAdapter(adapter);

                } else {
                    Toast.makeText(MarketActivity.this, "余额不足！！！", 0).show();
                }

            }
        };
    }

    private List<Map<String, Object>> getData(String[] goodsName, int[] ownedNumber, int[] goodsImage) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < goodsName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("goodsName", goodsName[i]);
            map.put("ownedNumber", ownedNumber[i]);
            map.put("goodsImage", goodsImage[i]);
            list.add(map);
        }

        return list;
    }

    public final class ViewHolder {
        public ImageView goodsImage;
        public TextView goodsName;
        public TextView ownedNumber;
        public Button buy;
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
                //ViewGroup vlist = (ViewGroup)findViewById(R.id.market_list_lineaeLayout);
                //FontManager.changeFonts(vlist,MarketActivity.this,AppConstant.Baby);

                convertView = mInflater.inflate(R.layout.market_list, null);
                holder.goodsImage = (ImageView) convertView.findViewById(R.id.goodsImage);
                holder.goodsName = (TextView) convertView.findViewById(R.id.goodsName);
                holder.ownedNumber = (TextView) convertView.findViewById(R.id.ownedNumber);
                holder.buy = (Button) convertView.findViewById(R.id.buy);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }


            holder.goodsImage.setBackgroundResource((Integer) marketData.get(position).get("goodsImage"));
            holder.goodsName.setText((String) marketData.get(position).get("goodsName"));
            holder.ownedNumber.setText("已拥有：" + marketData.get(position).get("ownedNumber"));

            holder.buy.setTag(position);
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
            money = money - 20;
            Message message = new Message();
            SharedPreferences.Editor editor = preferences.edit();
            if (money < 0) {
                message.what = TOAST;

            } else {
                ownedNumber[mPosition]++;
                editor.putInt("money", money);
                editor.putInt(mPetName + "ownedNumber" + mPosition, ownedNumber[mPosition]);
                editor.commit();
                message.what = CHANGE_UI;
            }
            message.obj = mPosition;
            handler.sendMessage(message);
        }
    }
}

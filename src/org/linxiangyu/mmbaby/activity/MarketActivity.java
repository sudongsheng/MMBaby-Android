package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.linxiangyu.mmbaby.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Inner on 14-3-18.
 */
public class MarketActivity extends Activity {

    private List<Map<String, Object>> marketData;
    public  String goodsName[]=new String[]{"物品0","物品1","物品2","物品3","物品4"};
    public  int[] ownedNumber = new int[]{0,0,0,1,1};
    public  int goodsImage[]= new int[]{R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,
            R.drawable.ic_launcher,R.drawable.ic_launcher};
    private ListView listView;
    private ViewHolder holder;

    private TextView moneyTextview;
    private SharedPreferences preferences;
    int money;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);


        preferences= PreferenceManager.getDefaultSharedPreferences(MarketActivity.this);
        money=preferences.getInt("money",0);
        for (int i = 0;i<goodsName.length;i++){
            ownedNumber[i] = preferences.getInt("ownedNumber"+i,0);
        }
        marketData = getData();
        moneyTextview = (TextView)findViewById(R.id.moneyTextView);
        listView = (ListView)findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter(MarketActivity.this);
        listView.setAdapter(adapter);


        moneyTextview.setText("金币："+money);


    }

    private List<Map<String, Object>> getData() {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<goodsName.length;i++){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("goodsName", goodsName[i]);
                    map.put("ownedNumber", ownedNumber[i]);
                    map.put("goodsImage", goodsImage[i]);
                    list.add(map);
            }

                return list;
            }

    public final class ViewHolder{
        public ImageView goodsImage;
        public TextView goodsName;
        public TextView ownedNumber;
        public Button buy;
    }


    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;


        public MyAdapter(Context context){
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
            MyListener myListener = new MyListener(position);
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.market_list, null);
                holder.goodsImage = (ImageView)convertView.findViewById(R.id.goodsImage);
                holder.goodsName = (TextView)convertView.findViewById(R.id.goodsName);
                holder.ownedNumber = (TextView)convertView.findViewById(R.id.ownedNumber);
                holder.buy = (Button)convertView.findViewById(R.id.buy);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            holder.goodsImage.setBackgroundResource((Integer)marketData.get(position).get("goodsImage"));
            holder.goodsName.setText((String)marketData.get(position).get("goodsName"));
            holder.ownedNumber.setText("已拥有："+marketData.get(position).get("ownedNumber"));

            holder.buy.setTag(position);
            holder.buy.setOnClickListener(myListener);


            return convertView;
        }

    }

    private class MyListener implements View.OnClickListener {
                  int mPosition;
                          public MyListener(int inPosition){
                              mPosition= inPosition;
                          }
                          @Override
                          public void onClick(View v) {
                              // TODO Auto-generated method stub
                              ownedNumber[mPosition]++;
                              SharedPreferences.Editor editor = preferences.edit();
                              editor.putInt("ownedNumber"+mPosition,ownedNumber[mPosition]);
                              editor.commit();
                              //Log.d("TAG","ownedNumber"+mPosition+ownedNumber[mPosition]);
                              //Toast.makeText(MarketActivity.this,""+mPosition,Toast.LENGTH_SHORT).show();
                          }

                      }




    }

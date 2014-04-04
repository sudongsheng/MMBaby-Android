package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.linxiangyu.mmbaby.R;
import org.linxiangyu.mmbaby.utils.HorizontialListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Inner on 14-3-18.
 */
public class PetsActivity extends Activity {
    private int petLevel;
    private String petName;
    private TextView petLeveltv;
    private TextView petNametv;
    private Button marketButton;
    private ProgressBar progressBar;
    private SharedPreferences preferences;
    private int  integral;
    private List<Map<String, Object>> petsData;
    private HorizontialListView horizontialListView;
    public  String goodsName[]=new String[]{"物品0","物品1","物品2","物品3","物品4"};
    public  int[] ownedNumber = new int[]{0,0,0,0,0};
    public  int goodsImage[]= new int[]{R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,
            R.drawable.ic_launcher,R.drawable.ic_launcher};



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);
        petLevel = getIntent().getIntExtra("level",1);
        petName = getIntent().getStringExtra("petsName");
        petLeveltv = (TextView)findViewById(R.id.petLevel);
        petNametv = (TextView)findViewById(R.id.petName);
        marketButton = (Button)findViewById(R.id.marketButton);
        progressBar = (ProgressBar)findViewById(R.id.levelProgressBar);
        preferences = PreferenceManager.getDefaultSharedPreferences(PetsActivity.this);
        integral = preferences.getInt("intel",0);

        petLeveltv.setText("等级：LV"+ petLevel);
        petNametv.setText(petName);
        progressBar.setProgress(integral%1000);

        marketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetsActivity.this,MarketActivity.class);
                startActivity(intent);
            }
        });

        preferences= PreferenceManager.getDefaultSharedPreferences(PetsActivity.this);
        for (int i = 0;i<goodsName.length;i++){
            ownedNumber[i] = preferences.getInt("ownedNumber"+i,0);
        }
        petsData = getData();
        horizontialListView = (HorizontialListView)findViewById(R.id.horizontialListView);
        MyAdapter adapter = new MyAdapter(PetsActivity.this);
        horizontialListView.setAdapter(adapter);


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
        public Button useButton;
    }
    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;


        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return petsData.size();
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
            ViewHolder holder;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.pets_list, null);
                holder.goodsImage = (ImageView)convertView.findViewById(R.id.goodsImage);
                holder.goodsName = (TextView)convertView.findViewById(R.id.goodsName);
                holder.ownedNumber = (TextView)convertView.findViewById(R.id.ownedNumber);
                holder.useButton = (Button)convertView.findViewById(R.id.useButton);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            holder.goodsImage.setBackgroundResource((Integer)petsData.get(position).get("goodsImage"));
            holder.goodsName.setText((String)petsData.get(position).get("goodsName"));
            holder.ownedNumber.setText("已拥有："+petsData.get(position).get("ownedNumber"));

            holder.useButton.setTag(position);
            holder.useButton.setOnClickListener(myListener);


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
            ownedNumber[mPosition]--;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("ownedNumber"+mPosition,ownedNumber[mPosition]);
            editor.commit();
            //Log.d("TAG","ownedNumber"+mPosition+ownedNumber[mPosition]);
            //Toast.makeText(MarketActivity.this,""+mPosition,Toast.LENGTH_SHORT).show();
        }

    }


    public class Pet{
        public String name;
        public int level;
        public int imageId;
        public int progress;

        public Pet(String name,int level,int imageId){
            this.name = name;
            this.level = level;
            this.imageId = imageId;
        }
        public void eat(){

        }
        public void levelUp(int integral){
            progress = integral%100;
            level = integral/100;

        }

    }



}

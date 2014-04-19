package org.mmclub.mmbaby.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.mmclub.mmbaby.R;
import org.mmclub.mmbaby.database.Record;
import org.mmclub.mmbaby.utils.HorizontialListView;
import org.mmclub.mmbaby.utils.Pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Inner on 14-3-18.
 */
public class PetsActivity extends Activity {

    private String petName;
    private TextView petLeveltv;
    private TextView petNametv;
    private TextView petTypetv;
    private Button marketButton;
    private Button petBackButton;
    private ImageView petImageiv;
    private ProgressBar progressBar;
    private SharedPreferences preferences;
    private HorizontialListView horizontialListView;
    private List<Map<String, Object>> petsData;
    public  String goodsName[]=new String[]{"物品0","物品1","物品2","物品3","物品4"};
    public  int[] ownedNumber = new int[]{0,0,0,0,0};
    public  int buttonImage[]= new int[]{R.drawable.sunlight_button,R.drawable.water_button,
            R.drawable.scissors_button,R.drawable.shovel_button};
    private Pets petsDog;
    private Handler handler;
    private String morality = "morality";
    private int integral;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);
        petName = getIntent().getStringExtra("petsName");
        initView();

        preferences = PreferenceManager.getDefaultSharedPreferences(PetsActivity.this);
        integral = preferences.getInt( morality + "_integral",0);

        petsDog = new Pets();
        petsDog.levelUp(integral);

        initPets(petName,petsDog.getPetsLevel(),R.drawable.sunflower_image,
                "品德",petsDog.getNeedIntegral(),petsDog.getCurrentIntegral());

        /*petLeveltv.setText("LV"+ petsDog.getPetsLevel());
        petNametv.setText(petName);
        petImageiv.setBackgroundResource(R.drawable.sunflower_image);
        progressBar.setProgress(petsDog.getCurrentIntegral());
        progressBar.setMax(petsDog.getNeedIntegral());*/

        marketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetsActivity.this,MarketActivity.class);
                startActivity(intent);
            }
        });
        petBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                PetsActivity.this.finish();
            }
        });

        preferences= PreferenceManager.getDefaultSharedPreferences(PetsActivity.this);
        for (int i = 0;i<goodsName.length;i++){
            ownedNumber[i] = preferences.getInt("ownedNumber"+i,0);
        }
        petsData = getData(buttonImage);
        horizontialListView = (HorizontialListView)findViewById(R.id.horizontialListView);
        MyAdapter adapter = new MyAdapter(PetsActivity.this);
        horizontialListView.setAdapter(adapter);

        handler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if (msg.what == 1){
                    Toast.makeText(PetsActivity.this,"该道具已用完！！！",0).show();
                }
            }
        };


    }
    private void initView(){
        petLeveltv = (TextView)findViewById(R.id.petLevel);
        petNametv = (TextView)findViewById(R.id.petName);
        petTypetv = (TextView)findViewById(R.id.petType);
        marketButton = (Button)findViewById(R.id.marketButton);
        petBackButton = (Button)findViewById(R.id.petBack);
        petImageiv = (ImageView)findViewById(R.id.petImage);
        progressBar = (ProgressBar)findViewById(R.id.levelProgressBar);
    }
    private void initPets(String petName,int petLevel,int petImageId,String petType,int needIntegral,int currentIntegral){
        petLeveltv.setText("LV"+ petLevel);
        petNametv.setText(petName);
        petTypetv.setText(petType);
        petImageiv.setBackgroundResource(petImageId);
        progressBar.setProgress(currentIntegral);
        progressBar.setMax(needIntegral);


    }

    private List<Map<String, Object>> getData(int[] buttonImage) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<4;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            //map.put("goodsName", goodsName[i]);
            //map.put("ownedNumber", ownedNumber[i]);
            map.put("buttonImage", buttonImage[i]);
            list.add(map);
        }

        return list;
    }
    public final class ViewHolder{
        //public ImageView goodsImage;
        //public TextView goodsName;
        //public TextView ownedNumber;
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
                //holder.goodsImage = (ImageView)convertView.findViewById(R.id.goodsImage);
                //holder.goodsName = (TextView)convertView.findViewById(R.id.goodsName);
                //holder.ownedNumber = (TextView)convertView.findViewById(R.id.ownedNumber);
                holder.useButton = (Button)convertView.findViewById(R.id.useButton);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            //holder.goodsImage.setBackgroundResource((Integer)petsData.get(position).get("goodsImage"));
            //holder.goodsName.setText((String)petsData.get(position).get("goodsName"));
            //holder.ownedNumber.setText("已拥有："+petsData.get(position).get("ownedNumber"));
            holder.useButton.setBackgroundResource((Integer)petsData.get(position).get("buttonImage"));
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
            Message message = new Message();
            SharedPreferences.Editor editor = preferences.edit();
            ownedNumber[mPosition]--;
            if (ownedNumber[mPosition]<0){
                message.what = 1;
            }else {
            integral = integral + 500;
            editor.putInt(morality + "_integral", integral);
            editor.putInt("ownedNumber"+mPosition,ownedNumber[mPosition]);
            editor.commit();
            //Log.d("TAG","ownedNumber"+mPosition+ownedNumber[mPosition]);
            //Toast.makeText(MarketActivity.this,""+mPosition,Toast.LENGTH_SHORT).show();
            }
            handler.sendMessage(message);
        }

    }



}

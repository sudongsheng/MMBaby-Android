package org.mmclub.mmbaby.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import org.mmclub.mmbaby.database.Record;
import org.mmclub.mmbaby.utils.*;

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
    private TextProgressBar progressBar;
    private SharedPreferences preferences;
    private GridView gridView;
    private List<Map<String, Integer>> petsData;
    public  int[] ownedNumber = new int[]{0,0,0,0,0};
    private Pets pets;
    private Handler handler;
    private int integral;
    private int petId;
    private String integral_type;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);
        petName = getIntent().getStringExtra("petsName");
        petId = getIntent().getIntExtra("petId", 0);
        integral_type = getIntent().getStringExtra("integral_type");

        ViewGroup v = (ViewGroup) findViewById(R.id.linearLayout);
        FontManager.changeFonts(v, PetsActivity.this, AppConstant.Baby);

        initView();
        //获取对应积分
        preferences = PreferenceManager.getDefaultSharedPreferences(PetsActivity.this);
        //实例化pet类的对象
        pets = new Pets();
        integral = preferences.getInt(integral_type+"_integral",0);
        pets.levelUp(integral);
        switch (petId){
            case AppConstant.PET_DOG:
                initPets(R.drawable.morality_bg,petName, pets.getPetsLevel(), R.drawable.sunflower_image,
                        "品德", pets.getNeedIntegral(), pets.getCurrentIntegral());
                petsData = getData(BabyData.morality_buttonImage);
                break;
            case AppConstant.PET_CHICK:
                initPets(R.drawable.physical_bg,petName, pets.getPetsLevel(), R.drawable.chick_image,
                        "体育", pets.getNeedIntegral(), pets.getCurrentIntegral());
                petsData = getData(BabyData.physical_buttonImage);
                break;
            case AppConstant.PET_PLANT:
                initPets(R.drawable.intelligence_bg,petName, pets.getPetsLevel(), R.drawable.dog_image,
                        "智力", pets.getNeedIntegral(), pets.getCurrentIntegral());
                petsData = getData(BabyData.intelligence_buttonImage);
                break;
        }

        //给两个button设置监听，执行相应操作
        marketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetsActivity.this,MarketActivity.class);
                intent.putExtra("petId",petId);
                intent.putExtra("petsName",petName);
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

        //获取已经存好的拥有的道具数量，初始化horizontialListView
        for (int i = 0;i<4;i++){
            ownedNumber[i] = preferences.getInt(petName+"ownedNumber"+i,0);
        }

        MyAdapter adapter = new MyAdapter(PetsActivity.this);
        gridView.setAdapter(adapter);

        //接受消息，执行操作
        handler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if (msg.what == 1){
                    Toast.makeText(PetsActivity.this,"该道具已用完！！！",0).show();
                }else {
                    integral = preferences.getInt(integral_type+"_integral",0);
                    Log.e("TAG","integral="+integral);
                    pets = new Pets();
                    pets.levelUp(integral);
                    petLeveltv.setText("LV"+ pets.getPetsLevel());
                    progressBar.setMax(pets.getNeedIntegral());
                    progressBar.setProgress(pets.getCurrentIntegral());
                }
            }
        };


    }
    @Override
    public void onStart(){
        super.onStart();
        //preferences= PreferenceManager.getDefaultSharedPreferences(PetsActivity.this);
        for (int i = 0;i<4;i++){
            ownedNumber[i] = preferences.getInt(petName+"ownedNumber"+i,0);
        }

    }

    //获得view控件显示
    private void initView(){
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        petLeveltv = (TextView)findViewById(R.id.petLevel);
        petNametv = (TextView)findViewById(R.id.petName);
        petTypetv = (TextView)findViewById(R.id.petType);
        marketButton = (Button)findViewById(R.id.marketButton);
        petBackButton = (Button)findViewById(R.id.petBack);
        petImageiv = (ImageView)findViewById(R.id.petImage);
        progressBar = (TextProgressBar)findViewById(R.id.levelProgressBar);
        gridView = (GridView)findViewById(R.id.gridView);
    }
    //初始化view
    private void initPets(int petBackground,String petName,int petLevel,int petImageId,
                          String petType,int needIntegral,int currentIntegral){
        linearLayout.setBackgroundResource(petBackground);
        petLeveltv.setText("LV"+ petLevel);
        petNametv.setText(petName);
        petTypetv.setText(petType);
        petImageiv.setBackgroundResource(petImageId);
        progressBar.setMax(needIntegral);
        progressBar.setProgress(currentIntegral);
    }

    //生成所需的数据
    private List<Map<String, Integer>> getData(int[] buttonImage) {
        List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
        for(int i=0;i<4;i++){
            Map<String, Integer> map = new HashMap<String, Integer>();
            //map.put("goodsName", goodsName[i]);
            //map.put("ownedNumber", ownedNumber[i]);
            map.put("buttonImage", buttonImage[i]);
            list.add(map);
        }
        return list;
    }

    //定义一个ViewHolder类
    public final class ViewHolder{
        //public ImageView goodsImage;
        //public TextView goodsName;
        //public TextView ownedNumber;
        public Button useButton;
    }


    //自定义andapter获取数据
    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyListener myListener = new MyListener(position,getIntent().getStringExtra("petsName"));
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


            //holder.goodsImage.setBackgroundResource(petsData.get(position).get("goodsImage"));
            //holder.goodsName.setText((String)petsData.get(position).get("goodsName"));
            //holder.ownedNumber.setText("已拥有："+petsData.get(position).get("ownedNumber"));
            holder.useButton.setBackgroundResource(petsData.get(position).get("buttonImage"));
            holder.useButton.setTag(position);
            holder.useButton.setOnClickListener(myListener);


            return convertView;
        }

    }

    //设置按钮的监听，执行相应的操作
    private class MyListener implements View.OnClickListener {
        int mPosition;
        String mPetName;
        public MyListener(int inPosition,String petName){
            mPosition= inPosition;
            mPetName= petName;
        }
        @Override
        public void onClick(View v) {
            Message message = new Message();
            SharedPreferences.Editor editor = preferences.edit();
            ownedNumber[mPosition]--;
            if (ownedNumber[mPosition]<0){
                message.what = 1;
            }else {
                integral = integral + 200;
                editor.putInt(integral_type+"_integral", integral);
                editor.putInt(mPetName+"ownedNumber"+mPosition,ownedNumber[mPosition]);
                editor.commit();
                message.what = 2;

            //Log.d("TAG","ownedNumber"+mPosition+ownedNumber[mPosition]);
            //Toast.makeText(MarketActivity.this,""+mPosition,Toast.LENGTH_SHORT).show();
            }
            handler.sendMessage(message);
        }
    }

}

package org.mmclub.mmbaby.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.mmclub.mmbaby.R;
import org.mmclub.mmbaby.utils.AppConstant;

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
    HashMap<Integer,Integer> soundMap=new HashMap<Integer, Integer>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby);
        petDog = (Button) findViewById(R.id.petDog);
        petChick = (Button) findViewById(R.id.petChick);
        petSunFlower = (Button) findViewById(R.id.petSunFlower);
        babyBack = (Button)findViewById(R.id.babyBack);
        babyBg = (TextView)findViewById(R.id.babyBg);
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);

        soundMap.put(AppConstant.PET_PLANT,soundPool.load(BabyActivity.this,R.raw.sunlight,1));
        soundMap.put(AppConstant.PET_CHICK,soundPool.load(BabyActivity.this,R.raw.crow,1));
        soundMap.put(AppConstant.PET_DOG,soundPool.load(BabyActivity.this,R.raw.bark,1));
        soundMap.put(4,soundPool.load(this,R.raw.button_clicked,1));

        petDog.setOnClickListener(new PetClickListener("旺财", AppConstant.PET_DOG, "intelligence"));
        petChick.setOnClickListener(new PetClickListener("鸡仔", AppConstant.PET_CHICK, "physical"));
        petSunFlower.setOnClickListener(new PetClickListener("小葵", AppConstant.PET_PLANT, "morality"));

        babyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(soundMap.get(4),1,1,0,0,1);
                onBackPressed();
                BabyActivity.this.finish();
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        petDog = (Button) findViewById(R.id.petDog);
        petChick = (Button) findViewById(R.id.petChick);
        petSunFlower = (Button) findViewById(R.id.petSunFlower);
        babyBack = (Button)findViewById(R.id.babyBack);
        babyBg = (TextView)findViewById(R.id.babyBg);

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
            this.petId=petId;
            intent = new Intent(BabyActivity.this, PetsActivity.class);
            intent.putExtra("petId", petId);
            intent.putExtra("petsName", petsName);
            intent.putExtra("integral_type", integral_type);

        }

        @Override
        public void onClick(View view) {
            soundPool.play(soundMap.get(petId),1,1,0,0,1);
            startActivity(intent);
        }
    }

}

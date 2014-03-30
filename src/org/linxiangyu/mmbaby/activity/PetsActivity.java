package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.linxiangyu.mmbaby.R;
import org.linxiangyu.mmbaby.utils.AppConstant;

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
    private TextView ownedNumber;
    private Button feedButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);
        petLevel = getIntent().getIntExtra("level",1);
        petName = getIntent().getStringExtra("petsName");
        petLeveltv = (TextView)findViewById(R.id.petLevel);
        petNametv = (TextView)findViewById(R.id.petName);
        ownedNumber = (TextView)findViewById(R.id.ownedNumber);
        feedButton = (Button)findViewById(R.id.feedButton);
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

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = preferences.edit();
            }
        });


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

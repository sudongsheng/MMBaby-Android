package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.os.Bundle;
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

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);
        petLevel = getIntent().getIntExtra("level",1);
        petName = getIntent().getStringExtra("petsName");
        petLeveltv = (TextView)findViewById(R.id.petLevel);
        petNametv = (TextView)findViewById(R.id.petName);

        petLeveltv.setText("等级：LV"+ petLevel);
        petNametv.setText(petName);


    }
    public class Pet{
        public String name;
        public int level;
        public int imageId;

        public Pet(String name,int level,int imageId){
            this.name = name;
            this.level = level;
            this.imageId = imageId;
        }
        public void eat(){

        }
        public void levelUp(){

        }

    }
}

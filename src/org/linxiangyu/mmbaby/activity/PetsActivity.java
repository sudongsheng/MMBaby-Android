package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.os.Bundle;
import org.linxiangyu.mmbaby.R;
import org.linxiangyu.mmbaby.utils.AppConstant;

/**
 * Created by Inner on 14-3-18.
 */
public class PetsActivity extends Activity {
    private int pet;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);
        pet=getIntent().getIntExtra("PET", AppConstant.PET_DOG);
        Pet mPet=new Pet();
    }
    public class Pet{
        public String name;
        public String level;

    }
}

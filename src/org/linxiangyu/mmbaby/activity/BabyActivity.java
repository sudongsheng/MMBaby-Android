package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.linxiangyu.mmbaby.R;
import org.linxiangyu.mmbaby.utils.AppConstant;

/**
 * Created by helloworld on 14-3-17.
 */
public class BabyActivity extends Activity {

    private Button petDog;
    private Button petChick;
    private Button petSunFlower;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby);
        petDog = (Button)findViewById(R.id.petDog);
        petChick = (Button)findViewById(R.id.petChick);
        petSunFlower = (Button)findViewById(R.id.petSunFlower);

        petDog.setOnClickListener(new PetClickListener("旺财",AppConstant.PET_DOG));
        petChick.setOnClickListener(new PetClickListener("鸡仔",AppConstant.PET_CAT));
        petSunFlower.setOnClickListener(new PetClickListener("小葵",AppConstant.PET_RABBIT));
    }
    public class PetClickListener implements View.OnClickListener {
        private Intent intent;
        public PetClickListener(String petsName, int level){
            intent =new Intent(BabyActivity.this,PetsActivity.class);
            intent.putExtra("level",level);
            intent.putExtra("petsName",petsName);
        }
        @Override
        public void onClick(View view) {
            startActivity(intent);
        }
    }
}

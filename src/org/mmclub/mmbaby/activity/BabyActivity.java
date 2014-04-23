package org.mmclub.mmbaby.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.mmclub.mmbaby.R;
import org.mmclub.mmbaby.utils.AppConstant;

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

        petDog.setOnClickListener(new PetClickListener("旺财",3,"intelligence"));
        petChick.setOnClickListener(new PetClickListener("鸡仔",2,"physical"));
        petSunFlower.setOnClickListener(new PetClickListener("小葵",1,"morality"));
    }
    public class PetClickListener implements View.OnClickListener {
        private Intent intent;
        public PetClickListener(String petsName, int petId,String integral_type){
            intent =new Intent(BabyActivity.this,PetsActivity.class);
            intent.putExtra("petId",petId);
            intent.putExtra("petsName",petsName);
            intent.putExtra("integral_type",integral_type);
        }
        @Override
        public void onClick(View view) {
            startActivity(intent);
        }
    }
}

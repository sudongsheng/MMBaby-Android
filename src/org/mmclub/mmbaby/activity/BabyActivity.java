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
    private Button petCat;
    private Button petRabbit;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby);
        petDog = (Button)findViewById(R.id.petDog);
        petCat = (Button)findViewById(R.id.petCat);
        petRabbit = (Button)findViewById(R.id.petRabbit);

        petDog.setOnClickListener(new PetClickListener("旺财",AppConstant.PET_DOG));
        petCat.setOnClickListener(new PetClickListener("猫咪",AppConstant.PET_CAT));
        petRabbit.setOnClickListener(new PetClickListener("兰花",AppConstant.PET_RABBIT));
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

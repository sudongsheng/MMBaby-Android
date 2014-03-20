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

    private Button petMarket;
    private Button petDog;
    private Button petCat;
    private Button petRabbit;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby);
        petMarket = (Button)findViewById(R.id.petMarket);
        petDog = (Button)findViewById(R.id.petDog);
        petCat = (Button)findViewById(R.id.petCat);
        petRabbit = (Button)findViewById(R.id.petRabbit);

        petMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BabyActivity.this,MarketActivity.class);
                startActivity(intent);
            }
        });
        petDog.setOnClickListener(new PetClickListener(AppConstant.PET_DOG));
        petCat.setOnClickListener(new PetClickListener(AppConstant.PET_CAT));
        petRabbit.setOnClickListener(new PetClickListener(AppConstant.PET_RABBIT));
    }
    public class PetClickListener implements View.OnClickListener {
        private Intent intent;
        public PetClickListener(int i){
            intent =new Intent(BabyActivity.this,PetsActivity.class);
            intent.putExtra("PET",i);
        }
        @Override
        public void onClick(View view) {
            startActivity(intent);
        }
    }
}

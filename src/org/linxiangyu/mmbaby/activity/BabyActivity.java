package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.linxiangyu.mmbaby.R;

/**
 * Created by helloworld on 14-3-17.
 */
public class BabyActivity extends Activity {

    private Button petMarket;
    private Button petDog;
    private Button petCat;
    private Button petRubbit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby);
        petMarket = (Button)findViewById(R.id.petMarket);
        petDog = (Button)findViewById(R.id.petDog);
        petCat = (Button)findViewById(R.id.petCat);
        petRubbit = (Button)findViewById(R.id.petRabbit);

        petMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BabyActivity.this,MarketActivity.class);
                startActivity(intent);
            }
        });
    }

}

package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.linxiangyu.mmbaby.R;

/**
 * Created by sudongsheng on 14-3-17.
 */
public class MainActivity extends Activity{
    private Button mama;
    private Button baby;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mama=(Button)findViewById(R.id.mama);
        baby=(Button)findViewById(R.id.baby);
        mama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(MainActivity.this,MamaActivity.class);
                startActivity(intent);
            }
        });
        baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,BabyActivity.class);
                startActivity(intent);
            }
        });
    }
}

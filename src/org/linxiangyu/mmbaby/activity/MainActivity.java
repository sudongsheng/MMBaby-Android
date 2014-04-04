package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import org.linxiangyu.mmbaby.R;

/**
 * Created by sudongsheng on 14-3-17.
 */
public class MainActivity extends Activity{

    private TextView mama_tex;
    private TextView baby_tex;

    private ImageButton mama;
    private ImageButton baby;
    private ImageButton setting;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mama=(ImageButton)findViewById(R.id.mama);
        baby=(ImageButton)findViewById(R.id.baby);
        setting=(ImageButton)findViewById(R.id.setting);

        mama_tex=(TextView)findViewById(R.id.mama_tex);
        baby_tex=(TextView)findViewById(R.id.baby_tex);
        Typeface tf = Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/baby.ttf");
        mama_tex.setTypeface(tf);
        baby_tex.setTypeface(tf);

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
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}

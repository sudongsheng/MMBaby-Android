package org.mmclub.mmbaby.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.mmclub.mmbaby.R;

/**
 * Created by sudongsheng on 2014/4/26 0026.
 */
public class MamaGuideActivity extends Activity {

    private Button toMama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mama_guide);
        toMama=(Button)findViewById(R.id.toMama);
        toMama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MamaGuideActivity.this,MamaActivity.class);
                startActivity(intent);
            }
        });
    }
}

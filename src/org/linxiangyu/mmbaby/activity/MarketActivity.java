package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import org.linxiangyu.mmbaby.R;

/**
 * Created by Inner on 14-3-18.
 */
public class MarketActivity extends Activity {

    private TextView moneyTextview;
    private SharedPreferences preferences;
    int money;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        moneyTextview = (TextView)findViewById(R.id.moneyTextView);

        preferences= PreferenceManager.getDefaultSharedPreferences(MarketActivity.this);
        money=preferences.getInt("money",0);

        moneyTextview.setText("金币："+money);
    }
}

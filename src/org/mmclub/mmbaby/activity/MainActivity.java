package org.mmclub.mmbaby.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.mmclub.mmbaby.R;
import org.mmclub.mmbaby.utils.MemoryTimeTest;

/**
 * Created by sudongsheng on 14-3-17.
 */
public class MainActivity extends Activity {

    private ImageButton mama;
    private ImageButton baby;
    private ImageButton setting;

    private SharedPreferences preferences;
    private boolean isPwdExist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        isPwdExist = preferences.getBoolean("isPwdExist", false);

        mama = (ImageButton) findViewById(R.id.mama);
        baby = (ImageButton) findViewById(R.id.baby);
        setting = (ImageButton) findViewById(R.id.setting);

        mama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getBoolean("toGuide", true)) {
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("toGuide",false);
                    editor.commit();
                    Intent intent=new Intent(MainActivity.this,MamaGuideActivity.class);
                    startActivity(intent);
                } else {
                    if (isPwdExist) {
                        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_enter_password, null);
                        final Dialog dialog = new AlertDialog.Builder(MainActivity.this).setView(v).create();
                        dialog.show();
                        final EditText editText = (EditText) v.findViewById(R.id.pwd);
                        Button btn = (Button) v.findViewById(R.id.sure);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (editText.getText().toString().equals(preferences.getString("password", null))) {
                                    Intent intent = new Intent(MainActivity.this, MamaActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                                    editText.setText("");
                                }
                            }
                        });
                    } else {
                        Intent intent = new Intent(MainActivity.this, MamaActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BabyActivity.class);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
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

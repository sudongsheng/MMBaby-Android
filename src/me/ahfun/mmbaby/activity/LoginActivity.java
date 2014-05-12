package me.ahfun.mmbaby.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import me.ahfun.mmbaby.R;
import me.ahfun.mmbaby.utils.AVOS_Cloud;

/**
 * Created by sudongsheng on 14-3-17.
 */
public class LoginActivity extends Activity {

    private EditText user;
    private EditText pwd;
    private CheckBox isRemember;
    private Button login;

    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "用户名为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (pwd.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("TAG", "Check:" + isRemember.isChecked());
                        SharedPreferences.Editor editor=preferences.edit();
                        if(isRemember.isChecked()){
                            editor.putString("username",user.getText().toString());
                            editor.putString("password",pwd.getText().toString());
                            editor.putBoolean("checked",true);
                        }else {
                            editor.putString("username","");
                            editor.putString("password","");
                            editor.putBoolean("checked",false);
                        }
                        editor.commit();
                        AVOS_Cloud avos_cloud = new AVOS_Cloud(LoginActivity.this);
                        avos_cloud.login(user.getText().toString(), pwd.getText().toString());
                    }
                }
            }
        });
    }
    private void init(){

        findViewByIds();

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        user.setText(preferences.getString("username", ""));
        pwd.setText(preferences.getString("password", ""));
        isRemember.setChecked(preferences.getBoolean("checked",false));
        try {
            getIntent().getStringExtra("username").toString();
            user.setText(getIntent().getStringExtra("username"));
            pwd.setText(getIntent().getStringExtra("password"));
        }catch (Exception e){
        }
    }
    private void findViewByIds() {
        user = (EditText) findViewById(R.id.login_user);
        pwd = (EditText) findViewById(R.id.login_pwd);
        isRemember = (CheckBox) findViewById(R.id.login_check);
        login = (Button) findViewById(R.id.login);
    }
}

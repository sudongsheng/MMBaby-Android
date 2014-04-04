package org.linxiangyu.mmbaby.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import org.linxiangyu.mmbaby.R;
import org.linxiangyu.mmbaby.utils.AppConstant;
import org.linxiangyu.mmbaby.utils.FontManager;

/**
 * Created by sudongsheng on 2014/4/2 0002.
 */
public class SettingActivity extends Activity{

    private ImageButton back;
    private LinearLayout setting_password;
    private LinearLayout login;
    private LinearLayout register;
    private LinearLayout share;
    private LinearLayout about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViewByIds();
        setListeners();
        ViewGroup v=(ViewGroup)findViewById(R.id.setting_activity);
        FontManager.changeFonts(v,this, AppConstant.Mama);
    }
    private void findViewByIds(){
        back=(ImageButton)findViewById(R.id.back_setting);
        setting_password=(LinearLayout)findViewById(R.id.setting_password);
        login=(LinearLayout)findViewById(R.id.login);
        register=(LinearLayout)findViewById(R.id.register);
        share=(LinearLayout)findViewById(R.id.share);
        about=(LinearLayout)findViewById(R.id.about);
    }
    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setting_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this, WebviewActivity.class);
                intent.putExtra(WebviewActivity.EXTRA_TITLE, "MM日报与开发团队");
                intent.putExtra(WebviewActivity.EXTRA_URL, "file:///android_asset/about_us.html");
                startActivity(intent);
            }
        });
    }
}

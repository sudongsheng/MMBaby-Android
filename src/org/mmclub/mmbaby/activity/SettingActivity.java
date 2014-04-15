package org.mmclub.mmbaby.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.mmclub.mmbaby.R;
import org.mmclub.mmbaby.utils.AppConstant;
import org.mmclub.mmbaby.utils.FontManager;

/**
 * Created by sudongsheng on 2014/4/2 0002.
 */
public class SettingActivity extends Activity {

    private ImageButton back;
    private LinearLayout setting_password;
    private LinearLayout set_morality;
    private LinearLayout set_intelligence;
    private LinearLayout set_physical;
    private LinearLayout login;
    private LinearLayout register;
    private LinearLayout share;
    private LinearLayout about;
    private CheckBox checkBox;

    private SharedPreferences preferences;
    private boolean isFirstTime;
    private boolean isCancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViewByIds();
        preferences = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
        isFirstTime = preferences.getBoolean("isFirstTime", true);
        setListeners();
        ViewGroup v = (ViewGroup) findViewById(R.id.setting_activity);
        FontManager.changeFonts(v, this, AppConstant.Mama);
    }

    private void findViewByIds() {
        back = (ImageButton) findViewById(R.id.back_setting);
        setting_password = (LinearLayout) findViewById(R.id.setting_password);
        set_morality = (LinearLayout) findViewById(R.id.set_morality);
        set_intelligence = (LinearLayout) findViewById(R.id.set_intelligence);
        set_physical = (LinearLayout) findViewById(R.id.set_physical);
        login = (LinearLayout) findViewById(R.id.login);
        register = (LinearLayout) findViewById(R.id.register);
        share = (LinearLayout) findViewById(R.id.share);
        about = (LinearLayout) findViewById(R.id.about);
    }

    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        setting_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFirstTime) {
                    View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.view_set_password, null);
                    final Dialog dialog = new AlertDialog.Builder(SettingActivity.this).setView(v).create();
                    dialog.show();
                    final EditText old_pwd = (EditText) v.findViewById(R.id.old_pwd);
                    final EditText new_pwd = (EditText) v.findViewById(R.id.new_pwd);
                    final EditText re_pwd = (EditText) v.findViewById(R.id.re_pwd);
                    checkBox = (CheckBox) v.findViewById(R.id.cancel);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            isCancel = b;
                        }
                    });
                    Button btn = (Button) v.findViewById(R.id.yes);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (old_pwd.getText().toString().equals(preferences.getString("password", null))) {
                                if (isCancel) {
                                    isFirstTime=true;
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("isFirstTime",true);
                                    editor.commit();
                                    dialog.dismiss();
                                    Toast.makeText(SettingActivity.this, "密码设定取消成功", Toast.LENGTH_LONG).show();
                                } else {
                                    if (new_pwd.getText().toString().equals(re_pwd.getText().toString())) {
                                        if (!new_pwd.getText().toString().equals("")) {
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("password", new_pwd.getText().toString());
                                            editor.commit();
                                            Toast.makeText(SettingActivity.this, "密码设置成功", Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(SettingActivity.this, "新密码不能为空", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(SettingActivity.this, "新密码与重复密码不匹配", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Toast.makeText(SettingActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.view_first_time_password, null);
                    final Dialog dialog = new AlertDialog.Builder(SettingActivity.this).setView(v).create();
                    dialog.show();
                    final EditText editText = (EditText) v.findViewById(R.id.pwd);
                    Button btn = (Button) v.findViewById(R.id.sure);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!editText.getText().toString().equals("")) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("password", editText.getText().toString());
                                editor.putBoolean("isFirstTime", false);
                                editor.commit();
                                isFirstTime = false;
                                dialog.dismiss();
                                Toast.makeText(SettingActivity.this, "密码设置成功", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SettingActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        set_morality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        set_intelligence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        set_physical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
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
                Intent intent = new Intent(SettingActivity.this, WebviewActivity.class);
                intent.putExtra(WebviewActivity.EXTRA_TITLE, "MM日报与开发团队");
                intent.putExtra(WebviewActivity.EXTRA_URL, "file:///android_asset/about_us.html");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SettingActivity.this,MainActivity.class);
        startActivity(intent);
    }
}

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
    private LinearLayout setting_rate;
    private LinearLayout login;
    private LinearLayout register;
    private LinearLayout share;
    private LinearLayout about;
    private CheckBox checkBox;

    private SharedPreferences preferences;
    private boolean isPwdExist;

    private int morality_rate = 100;
    private int intelligence_rate = 100;
    private int physical_rate = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViewByIds();
        preferences = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
        isPwdExist = preferences.getBoolean("isPwdExist", false);
        setListeners();
        ViewGroup v = (ViewGroup) findViewById(R.id.setting_activity);
        FontManager.changeFonts(v, this, AppConstant.Mama);
    }

    private void findViewByIds() {
        back = (ImageButton) findViewById(R.id.back_setting);
        setting_password = (LinearLayout) findViewById(R.id.setting_password);
        setting_rate = (LinearLayout) findViewById(R.id.setting_rate);
        login = (LinearLayout) findViewById(R.id.login);
        register = (LinearLayout) findViewById(R.id.register);
        share = (LinearLayout) findViewById(R.id.share);
        about = (LinearLayout) findViewById(R.id.about);
    }

    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        setting_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPwdExist) {
                    View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.view_set_password, null);
                    final Dialog dialog = new AlertDialog.Builder(SettingActivity.this).setView(v).create();
                    dialog.show();
                    final EditText old_pwd = (EditText) v.findViewById(R.id.old_pwd);
                    final EditText new_pwd = (EditText) v.findViewById(R.id.new_pwd);
                    final EditText re_pwd = (EditText) v.findViewById(R.id.re_pwd);
                    checkBox = (CheckBox) v.findViewById(R.id.cancel);
                    checkBox.setChecked(preferences.getBoolean("isCancel",false));
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isCancel",b);
                            editor.commit();
                        }
                    });
                    Button btn = (Button) v.findViewById(R.id.yes);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (old_pwd.getText().toString().equals(preferences.getString("password", null))) {
                                if (checkBox.isChecked()) {
                                    isPwdExist = false;
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("isPwdExist", false);
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
                    View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.view_enter_password, null);
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
                                editor.putBoolean("isPwdExist", true);
                                editor.commit();
                                isPwdExist = true;
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
        setting_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPwdExist) {
                    View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.view_enter_password, null);
                    final Dialog dialog = new AlertDialog.Builder(SettingActivity.this).setView(v).create();
                    dialog.show();
                    final EditText editText = (EditText) v.findViewById(R.id.pwd);
                    Button btn = (Button) v.findViewById(R.id.sure);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (editText.getText().toString().equals(preferences.getString("password", null))) {
                                dialog.dismiss();
                                setRateDialog();
                            } else {
                                Toast.makeText(SettingActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                                editText.setText("");
                            }
                        }
                    });
                } else {
                    setRateDialog();
                }
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
                Intent intent = new Intent(SettingActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,ChooseShareActivity.class);
                startActivity(intent);
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

    private void setRateDialog() {
        View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.view_set_rate, null);
        final Dialog dialog = new AlertDialog.Builder(SettingActivity.this).setView(v).create();
        dialog.show();
        Spinner set_morality = (Spinner) v.findViewById(R.id.set_morality);
        Spinner set_intelligence = (Spinner) v.findViewById(R.id.set_intelligence);
        Spinner set_physical = (Spinner) v.findViewById(R.id.set_physical);
        Button btn = (Button) v.findViewById(R.id.yes_rate);
        setSpinnerAdapter(set_morality, AppConstant.MORALITY);
        setSpinnerAdapter(set_intelligence, AppConstant.INTELLIGENCE);
        setSpinnerAdapter(set_physical, AppConstant.PHYSICAL);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("morality_rate", morality_rate);
                editor.putInt("intelligence_rate", intelligence_rate);
                editor.putInt("physical_rate", physical_rate);
                editor.commit();
                dialog.dismiss();
                Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setSpinnerAdapter(Spinner spinner, final int identify) {
        String spinner_text[] = new String[6];
        spinner_text[0] = 50 + "";
        for (int i = 1; i < 6; i++) {
            spinner_text[i] = i * 100 + "";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_spinner_item, spinner_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SetNum(identify, i == 0 ? 50 : i * 100);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                SetNum(identify, 100);
            }
        });
    }

    private void SetNum(int identify, int i) {
        switch (identify) {
            case AppConstant.MORALITY:
                morality_rate = i;
                break;
            case AppConstant.INTELLIGENCE:
                intelligence_rate = i;
                break;
            case AppConstant.PHYSICAL:
                physical_rate = i;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

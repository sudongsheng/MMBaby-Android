package me.ahfun.mmbaby.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.avos.avoscloud.*;
import me.ahfun.mmbaby.activity.LoginActivity;
import me.ahfun.mmbaby.activity.SettingActivity;

/**
 * Created by sudongsheng on 2014/4/20 0020.
 */
public class AVOS_Cloud {

    private ProgressDialog progressDialog;
    public Context context;

    public AVOS_Cloud(Context context) {
        this.context = context;
        AVOSCloud.initialize(context, "8hncef0ezxoctsseug3hesqq8jix2huozmrsvxm8y9n9ty29", "9c0te98d91ur4c43q5hxw6hw7wfs5zpust2civmt612g3ker");
    }

    public String getPhoneInfo() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();
        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append("\nNetworkType = " + tm.getNetworkType());
        sb.append("\nPhoneType = " + tm.getPhoneType());
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSimState = " + tm.getSimState());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
        Log.i("TAG", sb.toString());
        return sb.toString();
    }

    public void register(final String username, final String password, String email) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("on register。。。");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(password);
        if (!email.equals("")) {
            user.setEmail(email);
        }
// 其他属性可以像其他AVObject对象一样使用put方法添加
        user.put("phone", getPhoneInfo());
        user.signUpInBackground(new SignUpCallback() {
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(context, "注册成功", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Intent intent=new Intent(context, LoginActivity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("password",password);
                    context.startActivity(intent);
                } else {
                    Log.i("TAG", e.toString());
                    Toast.makeText(context, "failure"+e.toString().substring(e.toString().indexOf(":")+1), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void login(String username, String password) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("on login。。。");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AVUser.logInInBackground(username, password, new LogInCallback() {
            public void done(AVUser user, AVException e) {
                if (user != null) {
                    Toast.makeText(context, "登录成功", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Intent intent=new Intent(context, SettingActivity.class);
                    context.startActivity(intent);
                } else {
                    Log.i("TAG", e.toString());
                        Toast.makeText(context, "failure"+e.toString().substring(e.toString().indexOf(":")), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });
    }
}

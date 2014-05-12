package me.ahfun.mmbaby.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.avos.avoscloud.AVOSCloud;
import me.ahfun.mmbaby.R;
import me.ahfun.mmbaby.utils.AVOS_Cloud;

/**
 * Created by sudongsheng on 2014/4/20 0020.
 */
public class RegisterActivity extends Activity {

    private EditText user;
    private EditText pwd;
    private EditText rePwd;
    private EditText email;
    private Button login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewByIds();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (pwd.getText().toString().equals("")) {
                        Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if (pwd.getText().toString().equals(rePwd.getText().toString())) {
                            AVOS_Cloud avos_cloud = new AVOS_Cloud(RegisterActivity.this);
                            avos_cloud.register(user.getText().toString(), pwd.getText().toString(), email.getText().toString());
                        } else {
                            Toast.makeText(RegisterActivity.this, "重复密码不一致", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void findViewByIds() {
        user = (EditText) findViewById(R.id.register_user);
        pwd = (EditText) findViewById(R.id.register_pwd);
        rePwd = (EditText) findViewById(R.id.register_pwd_again);
        email = (EditText) findViewById(R.id.register_email);
        login = (Button) findViewById(R.id.register);
    }
}

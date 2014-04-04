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
public class LoginActivity extends Activity{

    private TextView mama_tex;
    private TextView baby_tex;

    private ImageButton mama;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}

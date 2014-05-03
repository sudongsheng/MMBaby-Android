package org.mmclub.mmbaby.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by sudongsheng on 2014/5/1 0001.
 */
public class CustomDialog extends Dialog {

    private static int default_width = 160; // 默认宽度
    private static int default_height = 120;// 默认高度

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int layout, int style) {
        super(context, style);
        setContentView(layout);
    }
}

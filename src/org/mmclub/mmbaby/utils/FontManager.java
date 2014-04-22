package org.mmclub.mmbaby.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by sudongsheng on 2014/4/3 0003.
 */
public class FontManager {
    public static void changeFonts(ViewGroup root, Activity act, int flag) {
        Typeface tf=null;
        if (flag == AppConstant.Mama)
            tf = Typeface.createFromAsset(act.getAssets(), "fonts/mama.ttf");
        else if (flag == AppConstant.Baby)
            tf = Typeface.createFromAsset(act.getAssets(), "fonts/baby.ttf");
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } /*else if (v instanceof ViewGroup) {
                changeFonts((ViewGroup) v, act,flag);
            }*/
        }
    }
}

package org.mmclub.mmbaby.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import org.mmclub.mmbaby.R;
import org.mmclub.mmbaby.database.DatabaseHelper;
import org.mmclub.mmbaby.utils.FileUtils;

import java.util.ArrayList;

/**
 * Created by sudongsheng on 2014/4/19 0019.
 */
public class ChooseShareActivity extends Activity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private ArrayList<String> titles = new ArrayList<String>();

    private Spinner title;
    private int index = 0;
    private int index_field = 0;
    private String field;
    private ImageButton back;
    private Button share;
    private GridView photo;
    private MyAdapter myAdapter;
    private String path;
    private ArrayList<String> arrayList;

    // 首先在您的Activity中添加如下成员变量
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_share);

        dbHelper = new DatabaseHelper(ChooseShareActivity.this, "MMBaby");
        sqLiteDatabase = dbHelper.getReadableDatabase();
        cursor = sqLiteDatabase.query("record", null, null, null, null, null, "time");
        while (cursor.moveToNext()) {
            titles.add(cursor.getString(cursor.getColumnIndex("title")));
        }

        title = (Spinner) findViewById(R.id.share_item);
        setSpinnerAdapter(title);

        back = (ImageButton) findViewById(R.id.share_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cursor.moveToFirst();
        field = cursor.getString(cursor.getColumnIndex("field"));
        photo = (GridView) findViewById(R.id.share_photo);
        arrayList = new FileUtils().readFileName("亲子习惯养成/" + field + index_field);
        myAdapter = new MyAdapter(this,arrayList);
        photo.setAdapter(myAdapter);
        photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                path = arrayList.get(i);
                Toast.makeText(ChooseShareActivity.this, "图片已选择", Toast.LENGTH_LONG).show();
            }
        });

        share = (Button) findViewById(R.id.share_submit);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cursor.getCount() == 0) {
                    Toast.makeText(ChooseShareActivity.this,"您还没有新建记录，快快去记录下您宝宝的生活记录吧！",Toast.LENGTH_LONG).show();
                } else {
                    Share();
                    mController.openShare(ChooseShareActivity.this, false);
                }
            }
        });
    }

    private void setSpinnerAdapter(Spinner spinner) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChooseShareActivity.this, android.R.layout.simple_spinner_item, titles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                cursor.moveToPosition(index);
                field = cursor.getString(cursor.getColumnIndex("field"));
                cursor.moveToFirst();
                for (int num = 0; num <= index; num++) {
                    if (cursor.getString(cursor.getColumnIndex("field")).equals(field))
                        index_field++;
                    cursor.moveToNext();
                }
                index_field--;
                arrayList.clear();
                arrayList = new FileUtils().readFileName("亲子习惯养成/" + field + index_field);
                myAdapter.array=arrayList;
                myAdapter.notifyDataSetChanged();
                index_field=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ImageView mBaby;
        private ArrayList<String> array;

        public MyAdapter(Context context,ArrayList<String> array) {
            this.inflater = LayoutInflater.from(context);
            this.array=array;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.view_photos_item, null);
            mBaby = (ImageView) view.findViewById(R.id.mBaby);
            Bitmap bitmap = BitmapFactory.decodeFile(array.get(i));
            final int h = bitmap.getHeight();
            final int w = bitmap.getWidth();
            Matrix matrix = new Matrix();
            if (h > w) {
                matrix.postScale(240f / w, 320f / h);
            } else {
                matrix.postScale(240f / h, 320f / w);
                matrix.postRotate(90);
            }
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
            bitmap.recycle();
            mBaby.setImageBitmap(resizeBmp);
            return view;
        }
    }

    public void Share() {
        SocializeConstants.APPKEY = "5350bba156240bb43b012eea";
        // 设置分享内容
        mController.setShareContent(titles.get(index));
        // 设置分享图片，参数2为本地图片的路径(绝对路径)
        if(arrayList!=null) {
            if(path==null){
                path=arrayList.get(0);
            }
            mController.setShareMedia(new UMImage(ChooseShareActivity.this, BitmapFactory.decodeFile(path)));
        }
        mController.setAppWebSite(SHARE_MEDIA.RENREN, "http://www.umeng.com/social");
        mController.getConfig().removePlatform(SHARE_MEDIA.SMS);
        mController.getConfig().supportQQPlatform(ChooseShareActivity.this, "101066698", "http://www.umeng.com/social");
        mController.getConfig().setSsoHandler(new QZoneSsoHandler(ChooseShareActivity.this, "101066698"));
        //注意一定保证在新浪微博上填写应用签名
        //将SDK目录下的src覆盖到项目工程根目录下，确保已添加'com/sina/sso/RemoteSSO.aidl'.
        //mController.getConfig().setSsoHandler(new SinaSsoHandler());

        //设置腾讯微博SSO handler
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        cursor.close();
        dbHelper.close();
        sqLiteDatabase.close();
    }
}

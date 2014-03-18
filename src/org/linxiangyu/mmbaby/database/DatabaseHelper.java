package org.linxiangyu.mmbaby.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sudongsheng on 14-3-18.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name,int version) {
        this(context, name, null, version);
    }
    public DatabaseHelper(Context context, String name){
        this(context,name,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("TAB", "create a database");
        sqLiteDatabase.execSQL("create table record(title TEXT,content TEXT,time TEXT,field TEXT,photo BLOB,money int,integral int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.i("TAB", "upgrade a database"+"olderVersion:"+i+"  newVersion:"+i2);
    }
}

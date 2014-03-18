package org.linxiangyu.mmbaby.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by helloworld on 14-3-18.
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
        Log.i("db", "create a database");
        sqLiteDatabase.execSQL("create table task(title varchar(50),content varchar(500),money int,completeOrNot boolean)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.i("db", "upgrade a database"+"olderVersion:"+i+" newVersion:"+i2);
    }
}

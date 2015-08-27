package egat.birdorov.news_srod;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 26/08/2558.
 */
public class MyOpenHelper extends SQLiteOpenHelper{

    // Explicit
    private static final String DATABASE_NAME = "srod.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_USER_TABLE = "create table userTABLE " +
            "(_id integer primary key, User text, Password text, Name text);";
    private static final String CREAT_NEWS_TABLE = "create table newsTABLE " +
            "(_id integer primary key, Date text, Head text, Detail text, Image text, Owner text)";


    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }   // Constructor

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREAT_NEWS_TABLE);
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}   // Main Class

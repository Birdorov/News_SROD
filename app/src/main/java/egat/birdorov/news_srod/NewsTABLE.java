package egat.birdorov.news_srod;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.lang.ref.SoftReference;

/**
 * Created by Administrator on 27/08/2558.
 */
public class NewsTABLE {

    // Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public static final String NEWS_TABLE = "newsTABLE";
    public static final String COLUMN_ID_NEWS = "_id";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_HEAD = "Head";
    public static final String COLUMN_DETAIL = "Detail";
    public static final String COLUMN_IMAGE = "Image";
    public static final String COLUMN_OWNER = "Owner";


    public NewsTABLE(Context context) {

        objMyOpenHelper = new MyOpenHelper(context);
        writeSqLiteDatabase = objMyOpenHelper.getWritableDatabase();
        readSqLiteDatabase = objMyOpenHelper.getReadableDatabase();

    }   // Constructor

    public long addNews(String strDate, String strHead, String strDetail, String strImage, String strOwner) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_DATE, strDate);
        objContentValues.put(COLUMN_HEAD, strHead);
        objContentValues.put(COLUMN_DETAIL, strDetail);
        objContentValues.put(COLUMN_IMAGE, strImage);
        objContentValues.put(COLUMN_OWNER, strOwner);
        return writeSqLiteDatabase.insert(NEWS_TABLE, null, objContentValues);
    }   // addNews

}   // Main Class

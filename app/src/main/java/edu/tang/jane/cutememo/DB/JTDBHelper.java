package edu.tang.jane.cutememo.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 2017/10/16.
 */

public class JTDBHelper extends SQLiteOpenHelper {

    public static final String DB_TABLE_NAME = "memo";
    public static final String DB_TABLE_NOTES = "notes_info"; // The length of notes
    public static final String DB_TABLE_DINNER = "dinner_info"; // Dinner details

    private static final String DB_NAME = "history.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_NOTES = "CREATE TABLE IF NOT EXISTS notes_info (_id INTEGER PRIMARY KEY AUTOINCREMENT, date INTEGER, notes_length INTEGER)";
    private static final String CREATE_TABLE_DINNER = "CREATE TABLE IF NOT EXISTS dinner_info (_id INTEGER PRIMARY KEY AUTOINCREMENT, date INTEGER, breakfast INTEGER, lunch INTEGER, dinner INTEGER)";

    public JTDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS memo" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, date INTEGER, sleep INTEGER, weight INTEGER,dinner INTEGER, sport INTEGER, notes STRING, period INTEGER, grade INTEGER, pee INTEGER)");
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_DINNER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }
}

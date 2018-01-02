package edu.tang.jane.cutememo.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by admin on 2017/10/16.
 */

public class JTDBManager {
    private JTDBHelper mHelper;
    private SQLiteDatabase mDb;

    public JTDBManager(Context context){
        mHelper = new JTDBHelper(context);
        mDb = mHelper.getWritableDatabase();
    }

    public void add(JTMemoEntity data) {
        ContentValues cv = new ContentValues();
        cv.put("date", data.date);
        cv.put("sleep", data.sleep);
        cv.put("weight", data.weight);
        cv.put("dinner", data.dinner);
        cv.put("sport", data.sports);
        cv.put("notes", data.notes);
        cv.put("period", data.period);
        cv.put("grade", data.grade);
        cv.put("pee", data.peeColor);
        mDb.insert(JTDBHelper.DB_TABLE_NAME, null, cv);

        ContentValues noteCV = new ContentValues();
        noteCV.put("date", data.date);
        noteCV.put("notes_length", data.notes.length());
        mDb.insert(JTDBHelper.DB_TABLE_NOTES, null, noteCV);

        ContentValues dinnerCV = new ContentValues();
        int[] ret = JTDBConstant.dinnerEnum2DinnerArray(data.dinner);
        dinnerCV.put("date", data.date);
        dinnerCV.put("breakfast", ret[0]);
        dinnerCV.put("lunch", ret[1]);
        dinnerCV.put("dinner", ret[2]);
        mDb.insert(JTDBHelper.DB_TABLE_DINNER, null, dinnerCV);
    }

    public void delete(long date) {
        String[] args = { Long.toString(date) };
        mDb.delete(JTDBHelper.DB_TABLE_NAME, "date=?", args);
        mDb.delete(JTDBHelper.DB_TABLE_NOTES, "date=?", args);
        mDb.delete(JTDBHelper.DB_TABLE_DINNER, "date=?", args);
    }

    public void clearData() {
        ExecSQL("DELETE FROM " + JTDBHelper.DB_TABLE_NAME);
        ExecSQL("DELETE FROM " + JTDBHelper.DB_TABLE_NOTES);
        ExecSQL("DELETE FROM " + JTDBHelper.DB_TABLE_DINNER);
    }

    public void closeDB() {
        mDb.close();
    }

    public ArrayList<JTMemoEntity> searchAllData() {
        String sql = "SELECT * FROM memo";
        return ExecSQLForMemberInfo(sql);
    }

    public void update(JTMemoEntity newEntity, long date) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("date", newEntity.date);
        updatedValues.put("sleep", newEntity.sleep);
        updatedValues.put("weight", newEntity.weight);
        updatedValues.put("dinner", newEntity.dinner);
        updatedValues.put("sport", newEntity.sports);
        updatedValues.put("notes", newEntity.notes);
        updatedValues.put("period", newEntity.period);
        updatedValues.put("grade", newEntity.grade);
        updatedValues.put("pee", newEntity.peeColor);

        String where = "date" + "=" + date;
        mDb.update(JTDBHelper.DB_TABLE_NAME, updatedValues, where, null);

        ContentValues noteCV = new ContentValues();
        noteCV.put("date", newEntity.date);
        noteCV.put("notes_length", newEntity.notes.length());
        mDb.update(JTDBHelper.DB_TABLE_NOTES, noteCV, where, null);

        ContentValues dinnerCV = new ContentValues();
        int[] ret = JTDBConstant.dinnerEnum2DinnerArray(newEntity.dinner);
        dinnerCV.put("date", newEntity.date);
        dinnerCV.put("breakfast", ret[0]);
        dinnerCV.put("lunch", ret[1]);
        dinnerCV.put("dinner", ret[2]);
        mDb.update(JTDBHelper.DB_TABLE_DINNER, dinnerCV, where, null);
    }

    private ArrayList<JTMemoEntity> ExecSQLForMemberInfo(String sql) {
        ArrayList<JTMemoEntity> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            JTMemoEntity info = new JTMemoEntity();
            info.date = c.getLong(c.getColumnIndex("date"));
            info.sleep = c.getInt(c.getColumnIndex("sleep"));
            info.weight = c.getInt(c.getColumnIndex("weight"));
            info.dinner = c.getInt(c.getColumnIndex("dinner"));
            info.sports = c.getInt(c.getColumnIndex("sport"));
            info.notes = c.getString(c.getColumnIndex("notes"));
            info.grade = c.getInt(c.getColumnIndex("grade"));
            info.peeColor = c.getInt(c.getColumnIndex("pee"));

            info.updateYMD();
            list.add(info);
        }

        c.close();
        return list;
    }

    private void ExecSQL(String sql) {
        try {
            mDb.execSQL(sql);
        } catch (Exception e) {
            Log.e("ExecSQL Exception", e.getMessage());
            e.printStackTrace();
        }
    }

    private Cursor ExecSQLForCursor(String sql) {
        Cursor c = mDb.rawQuery(sql, null);
        return c;
    }
}

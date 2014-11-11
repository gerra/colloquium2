package ru.ifmo.md.colloquium2.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.style.SubscriptSpan;

/**
 * Created by german on 11.11.14.
 */
public class CandidatesTable {
    public static String TABLE_NAME = "candidates";

    public static String CAND_ID = "_id";
    public static String CAND_NAME = "name";
    public static String CAND_SCORE = "score";
    public static String CAND_PERCENT = "percent";

    public static String getCreateScript() {
        return new String("CREATE TABLE " + TABLE_NAME + "("
                + CAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CAND_NAME + " TEXT NOT NULL, "
                + CAND_SCORE + " INTEGER, "
                + CAND_PERCENT + " FLOAT);");
    }

    public static void insert(SQLiteDatabase db, String name) {
        ContentValues cv = new ContentValues();
        cv.put(CAND_NAME, name);
        cv.put(CAND_SCORE, 0);
        cv.put(CAND_PERCENT, 0.0f);
        long rowId = db.insert(TABLE_NAME, null, cv);
        System.out.println("Insert to " + rowId);
    }

    private static void updatePercents(SQLiteDatabase db) {
        float totalScore = 0;
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + CAND_SCORE + ") FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            totalScore = cursor.getFloat(0);
        }
        db.execSQL("update " + TABLE_NAME + " set percent = 100*(score/" + totalScore+")");
    }

    public static void increaseById(SQLiteDatabase db, String id) {
        db.execSQL("update " + TABLE_NAME + " set score = score + 1 where _id = " + id);
        updatePercents(db);
    }

    public static void resetScore(SQLiteDatabase db) {
        db.execSQL("update " + TABLE_NAME + " set score = 0");
        db.execSQL("update " + TABLE_NAME + " set percent = 0");
    }
}

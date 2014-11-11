package ru.ifmo.md.colloquium2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by german on 11.11.14.
 */
public class MySQLManager {
    static final String DB_NAME = "ElectionsDB";
    static final int DB_VERSION = 1;

    public class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String script = CandidatesTable.getCreateScript();
            db.execSQL(script);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + CandidatesTable.TABLE_NAME);
            onCreate(db);
        }
    }

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public MySQLManager(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addCandidateByName(String name) {
        CandidatesTable.insert(db, name);
    }

    public Cursor getAllCandidates() {
        Cursor c = db.query(CandidatesTable.TABLE_NAME, null, null, null, null, null, null);
        return c;
    }

    public void increaseScore(String id) {
        CandidatesTable.increaseById(db, id);
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }
}

package io.github.fishercloud.fsnake.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public static final String scoresdb = "create table scores ("
            + "id integer primary key autoincrement, "
            + "date text, "
            + "score text )";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);

    }

    public DatabaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DatabaseHelper(Context context) {
        this(context, "scores", VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(scoresdb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
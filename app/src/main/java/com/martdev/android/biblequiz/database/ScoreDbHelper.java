package com.martdev.android.biblequiz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.martdev.android.biblequiz.database.ScoreSchema.Score.Cols.PLAYER_NAME;
import static com.martdev.android.biblequiz.database.ScoreSchema.Score.Cols.PLAYER_SCORE;
import static com.martdev.android.biblequiz.database.ScoreSchema.Score.NAME;

public class ScoreDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "score.db";
    private static final int VERSION =  1;

    public ScoreDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NAME + "(" +
                "_id integer primary key autoincrement, " +
                PLAYER_NAME + " TEXT, " +
                PLAYER_SCORE + " INTEGER " +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void insertScore(SQLiteDatabase db, String name, int score) {
        ContentValues scoreValues = new ContentValues();
        scoreValues.put(PLAYER_NAME, name);
        scoreValues.put(PLAYER_SCORE, score);
        db.insert(NAME, null, scoreValues);
    }
}

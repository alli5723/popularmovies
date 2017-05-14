package com.omo_lanke.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.omo_lanke.android.popularmovies.data.MoviesContract.MoviesEntry;

/**
 * Created by omo_lanke on 07/05/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    public MoviesDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE "
                + MoviesEntry.TABLE_NAME + " ("+
                MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MoviesEntry.COLUMN_NAME_ID + " INTEGER NOT NULL,"+
                MoviesEntry.COLUMN_NAME_BACKDROP + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_NAME_HOMEPAGE + " TEXT NULL," +
                MoviesEntry.COLUMN_NAME_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_NAME_OVERVIEW + " TEXT NULL," +
                MoviesEntry.COLUMN_NAME_POSTER + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_NAME_RELEASE_DATE + " TEXT NULL," +
                MoviesEntry.COLUMN_NAME_RUNTIME + " TEXT NULL," +
                MoviesEntry.COLUMN_NAME_VOTE_AVERAGE + " TEXT NOT NULL" +
            ");";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

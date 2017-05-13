package com.omo_lanke.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.omo_lanke.android.popularmovies.data.MoviesContract;
import com.omo_lanke.android.popularmovies.data.MoviesDbHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by omo_lanke on 07/05/2017.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private final Context mContext = InstrumentationRegistry.getTargetContext();

    private final Class mDbHelperClass = MoviesDbHelper.class;

    @Before
    public void setUp(){ deleteDatabase(); }

    @Test
    public void create_database_test() throws Exception{


        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use MoviesDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();


        /* We think the database is open, let's verify that here */
        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen,
                true,
                database.isOpen());

        /* This Cursor will contain the names of each table in our database */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        MoviesContract.MoviesEntry.TABLE_NAME + "'",
                null);

        /*
         * If tableNameCursor.moveToFirst returns false from this query, it means the database
         * wasn't created properly. In actuality, it means that your database contains no tables.
         */
        String errorInCreatingDatabase =
                "Error: This means that the database has not been created correctly";
        assertTrue(errorInCreatingDatabase,
                tableNameCursor.moveToFirst());

        /* If this fails, it means that your database doesn't contain the expected table(s) */
        assertEquals("Error: Your database was created without the expected tables.",
                MoviesContract.MoviesEntry.TABLE_NAME, tableNameCursor.getString(0));

        /* Always close a cursor when you are done with it */
        tableNameCursor.close();
    }

    @Test
    public void insert_single_record_test() throws Exception{

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use MoviesDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_BACKDROP, "test backdrop");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_HOMEPAGE, "test Homepage");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_ID, 123456);
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_ORIGINAL_TITLE, "test title");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_OVERVIEW, "test OVERVIEW");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_POSTER, "test POSTER");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_RELEASE_DATE, "test DATE");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_RUNTIME, "test 2HRS");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_VOTE_AVERAGE, "9");

        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                MoviesContract.MoviesEntry.TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert into the database", -1, firstRowId);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                /* Name of table on which to perform the query */
                MoviesContract.MoviesEntry.TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from Movies DB query";
        assertTrue(emptyQueryError,
                wCursor.moveToFirst());

        /* Close cursor and database */
        wCursor.close();
        dbHelper.close();
    }


    /**
     * Tests to ensure that inserts into your database results in automatically
     * incrementing row IDs.
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void autoincrement_test() throws Exception{

        /* First, let's ensure we have some values in our table initially */
        insert_single_record_test();

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use MoviesDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_BACKDROP, "test backdrop");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_HOMEPAGE, "test Homepage");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_ID, 123456);
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_ORIGINAL_TITLE, "test title");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_OVERVIEW, "test OVERVIEW");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_POSTER, "test POSTER");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_RELEASE_DATE, "test DATE");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_RUNTIME, "test 2HRS");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_VOTE_AVERAGE, "9");

        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                MoviesContract.MoviesEntry.TABLE_NAME,
                null,
                testValues);

        /* Insert ContentValues into database and get another row ID back */
        long secondRowId = database.insert(
                MoviesContract.MoviesEntry.TABLE_NAME,
                null,
                testValues);

        assertEquals("ID Autoincrement test failed!",
                firstRowId + 1, secondRowId);


    }


    /**
     * Tests that onUpgrade works by inserting 2 rows then calling onUpgrade and verifies that the
     * database has been successfully dropped and recreated by checking that the database is there
     * but empty
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void upgrade_database_test() throws Exception{

        /* Insert 2 rows before we upgrade to check that we dropped the database correctly */

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use MoviesDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_BACKDROP, "test backdrop");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_HOMEPAGE, "test Homepage");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_ID, 123456);
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_ORIGINAL_TITLE, "test title");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_OVERVIEW, "test OVERVIEW");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_POSTER, "test POSTER");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_RELEASE_DATE, "test DATE");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_RUNTIME, "test 2HRS");
        testValues.put(MoviesContract.MoviesEntry.COLUMN_NAME_VOTE_AVERAGE, "9");

        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                MoviesContract.MoviesEntry.TABLE_NAME,
                null,
                testValues);

        /* Insert ContentValues into database and get another row ID back */
        long secondRowId = database.insert(
                MoviesContract.MoviesEntry.TABLE_NAME,
                null,
                testValues);

        dbHelper.onUpgrade(database, 0, 1);
        database = dbHelper.getReadableDatabase();

        /* This Cursor will contain the names of each table in our database */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        MoviesContract.MoviesEntry.TABLE_NAME + "'",
                null);

        assertTrue(tableNameCursor.getCount() == 1);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                /* Name of table on which to perform the query */
                MoviesContract.MoviesEntry.TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */

        assertFalse("Database doesn't seem to have been dropped successfully when upgrading",
                wCursor.moveToFirst());

        tableNameCursor.close();
        database.close();
    }

    /**
     * Deletes the entire database.
     */
    void deleteDatabase(){
        try {
            /* Use reflection to get the database name from the db helper class */
            Field f = mDbHelperClass.getDeclaredField("DATABASE_NAME");
            f.setAccessible(true);
            mContext.deleteDatabase((String)f.get(null));
        }catch (NoSuchFieldException ex){
            fail("Make sure you have a member called DATABASE_NAME in the MoviesDbHelper");
        }catch (Exception ex){
            fail(ex.getMessage());
        }

    }
}

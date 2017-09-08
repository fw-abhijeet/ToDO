package com.example.abhijeet.todo.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Abhijeet on 9/8/2017.
 */

public class TodoProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = TodoProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the to-do table
     */
    private static final int TODO = 100;

    /**
     * URI matcher code for the content URI for a single to-do in the pets table
     */
    private static final int TODO_ID = 101;

    /**
     * It is a Urimatcher object to match a  URI with the corresponding code (like @to-do or todo_ID)
     * The input passed into the constructor represents the code to return for the root URI.
     * It is common to use Urimatcher.NO_MATCH for this case
     */
    private static final UriMatcher mUrimatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //Static initialization block, this block runs before anything from this class is called i.e. when the class is first loaded
    static {
        //All calls to addURI go here
        //this method assigns a predefined code (like TODO_ID) to the URI

        //case 1 when the requested URI ends at "/todo"
        //i.e the whole table
        mUrimatcher.addURI(TodoContract.CONTENT_AUTHORITY, TodoContract.PATH_TODO, TODO);

        //case 2 when the requested URI ends at case 1/# , where # is a wildcard operator which stands for a random integer
        mUrimatcher.addURI(TodoContract.CONTENT_AUTHORITY, TodoContract.PATH_TODO + "/#", TODO_ID);
    }

    //Global variable as most of the CRUD methods uses this to get a database connection
    private TodoDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new TodoDbHelper(getContext()); // AS pet the documentation iy is advised not to call
        return true;                                // getreadable()/ getwriteable() database in this method
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        //get readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //The result cursor that will hold the result to the query
        Cursor cursor;

        //match the URI to the appropriate case
        int match = mUrimatcher.match(uri);

        //pass the match code into a switch and do the operations as needed
        switch (match) {
            //The case when more than 1 row of the table is requested
            case TODO:
                // For this code, query the table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.

                //update the value in the cursor object
                cursor = db.query(TodoContract.TodoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            //The case when only single row is requested with appropriate ID
            case TODO_ID:
                // For the TODO_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.abhijeet.todo/todo/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                //
                //Extract the ID from the URI passed
                long id = ContentUris.parseId(uri);

                selection = TodoContract.TodoEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};

                //Now query the database with the ID of the row
                cursor = db.query(TodoContract.TodoEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            //when URI does not match to any case
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        //match the URI to the appropriate case
        int match = mUrimatcher.match(uri);
        switch (match) {
            case TODO:
                return insertTodo(uri, contentValues);
            default:
                throw new IllegalArgumentException("Cannot insert at unknown URI" + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    //HELPER METHOD
    //To insert the new TO-DO data

    private Uri insertTodo(Uri uri, ContentValues contentValues) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new to-do with the given values
        long id = database.insert(TodoContract.TodoEntry.TABLE_NAME, null, contentValues);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);


    }
}

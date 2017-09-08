package com.example.abhijeet.todo.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    @Override
    public boolean onCreate() {

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

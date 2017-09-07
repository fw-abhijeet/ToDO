package com.example.abhijeet.todo.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Abhijeet on 9/7/2017.
 */

public class TodoDbHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "todo.db";

    //This version needs to be updated whenever a new database is created
    public static int DATABASE_VERSION = 1;

    //Public so that any class can access it and use it to create a new database instance
    public TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TODO_TABLE = "CREATE TABLE" + TodoContract.TodoEntry.TABLE_NAME + " ("
                + TodoContract.TodoEntry._ID + " INTEGER PRIMARY KEY AUTO INCREMENT, "
                + TodoContract.TodoEntry.COLUMN_TASK + " INTEGER NOT NULL, "
                + TodoContract.TodoEntry.COLUMN_TIME + " INTEGER NOT NULL);";

        //Execute the SQL statement
        db.execSQL(SQL_CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Don't care
    }
}

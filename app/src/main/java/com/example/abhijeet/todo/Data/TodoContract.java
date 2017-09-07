package com.example.abhijeet.todo.Data;

import android.provider.BaseColumns;

/**
 * Created by Abhijeet on 9/7/2017.
 */

public final class TodoContract {
    //To make sure that no one instantiates it, declaring a private constructor
    private TodoContract() {

    }

    public final class TodoEntry implements BaseColumns {

        /**
         * Unique ID number for the pet (only for use in the database table).
         * <p>
         * Type: INTEGER
         */

        public final static String _ID = BaseColumns._ID;

        public static final String TABLE_NAME = "todo";

        public static final String COLUMN_TIME = "time";

        public final static String COLUMN_TASK = "task";


    }
}

package com.example.abhijeet.todo.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Abhijeet on 9/7/2017.
 */

public final class TodoContract {

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.abhijeet.todo";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.abhijeet.to-do/to-do/ is a valid path for
     * looking at to-do data. content://com.example.abhijeet.to-do/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_TODO = "todo";     //In simple words it is the table name of the concerned data
    //To make sure that no one instantiates it, declaring a private constructor
    private TodoContract() {
    }

    //Inner class for the table to-do and all its entries
    public static final class TodoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TODO);

        /**
         * Unique ID number for the to-do (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        public static final String TABLE_NAME = "todo";

        public static final String COLUMN_TIME = "time";

        public final static String COLUMN_TASK = "task";

        public final static String COLUMN_DATE_MONTH = "month";

        public final static String COLUMN_DATE_YEAR = "year";

        public final static String COLUMN_DATE_DAYOFMONTH = "day";

        public final static String COLUMN_PRIORITY = "priority";

        /**
         * Possible values of Priority
         */
        public final static int PRIORITY_NO_PRIORITY = 0;
        public final static int PRIORITY_LOW = 1;
        public final static int PRIORITY_MEDIUM = 2;
        public final static int PRIORITY_HIGH = 3;
    }
}

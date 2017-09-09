package com.example.abhijeet.todo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.abhijeet.todo.Data.TodoContract;

/**
 * Created by Abhijeet on 9/9/2017.
 */

/**
 * {@link TodoCursorAdaptor} is an adapter for a list or grid view
 * that uses a {@link Cursor} of to-do data as its data source. This adapter knows
 * how to create list items for each row of data in the {@link Cursor}.
 */
public class TodoCursorAdaptor extends CursorAdapter {

    /**
     * Constructs a new {@link TodoCursorAdaptor}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */

    public TodoCursorAdaptor(Context context, Cursor c) {
        super(context, c, 0 /* FLAGS = 0*/);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the to-do data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the task for the current to-do can be set on the TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Get the relevant views where the info has to be shown
        TextView tasktextview = (TextView) view.findViewById(R.id.task_show);
        TextView timedatetextview = (TextView) view.findViewById(R.id.time_date_show);

        //Read the relevant attributes from the cursor to display in the textviews
        String task = cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TASK));
        int minutes_after_midnight = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TIME));
        String normalized_time = Date_time_normalizer.getTimeAsNormalized(minutes_after_midnight);
        //Update the Textviews
        tasktextview.setText(task);
        timedatetextview.setText(normalized_time);
    }
}

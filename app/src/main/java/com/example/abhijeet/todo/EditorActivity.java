package com.example.abhijeet.todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.abhijeet.todo.Data.TodoContract;

import java.util.Calendar;


public class EditorActivity extends AppCompatActivity {

    //Selected time by the user if not it is the current system time (Initially Current Time)
    private static Calendar selecteddatetime = Calendar.getInstance();

    //Global field for the EditTime Field
    private static TextView time_textview;

    //Global Field for TaskEdit Field
    private static TextView task_textview;

    //Global Field fot DateEdit Field
    private static TextView date_textview;

    //IMPORTANT: This variable needs to be updated everytime anything in the task info has changed,
    // else a lot of functionalities would break :(
    private boolean misaNewTask = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            misaNewTask = true;
            return false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //Get references to all the data fields in the Editor Window
        time_textview = (TextView) findViewById(R.id.time_edit_field);
        task_textview = (TextView) findViewById(R.id.task_edit_field);
        date_textview = (TextView) findViewById(R.id.date_edit_field);

        //Set on touch Listeners For all the fields available in the editor
        //To find out if the user has changed any data or not
        //Set the variable misaNewtask to true if any touch event occurs
        task_textview.setOnTouchListener(mTouchListener);
        time_textview.setOnTouchListener(mTouchListener);
        date_textview.setOnTouchListener(mTouchListener);

        //Set OnClick Listener for the Time Picker Fragment
        time_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picker timepicker = new Picker();
                timepicker.show(getFragmentManager(), "TimePicker");
            }
        });

        //Set OnClick Listener for the Date Picker Fragment
        date_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        //Set the current Dat/ time as selected Date/Time if user has not selected anything
        //Update the text view with the formatted Date & Time
        time_textview.setText(Date_time_normalizer.timeFormatter(selecteddatetime));
        date_textview.setText(Date_time_normalizer.dateFormatter(selecteddatetime));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.taskeditor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Return false to allow normal menu processing to proceed,
        // true to consume it here.
        switch (item.getItemId()) {
            case R.id.add_menu_button:
                Uri uri = addtodo();
                //If the Uri equals null means that insertion is not successful
                //This case is already handled by Todoprovider insert method

                //If the URI is not null, that means that the insertion is successful
                if (uri != null) {
                    Toast.makeText(this, R.string.task_added_successfully, Toast.LENGTH_SHORT).show();
                }

                finish();
                return true;

            // Implementation of CONFIRM DIALOG if misaNewtask == true and the user has pressed back button
            case android.R.id.home:
                //misNewTask changes whenever user touches any field in the  editor window
                //If this is set to true, then it means that data has changed and,
                //We should notify the user about the possible loss of data
                if (!misaNewTask) {
                    //If no data has been changed
                    //Return to the normal processing and go back.
                    return false;
                }
                //If this a new task (data has changed) and the user presses option menu back button
                showUnsavedChangesDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.are_you_sure_you_want_to_discard_changes);
        //Do not pay much attention to the positive/negative
        builder.setNegativeButton(R.string.yes_discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setPositiveButton(R.string.no_keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //It means that the user wants to make the changes,
                //hence dismiss the dialog
                dialog.dismiss();
            }
        });

        //Now that all the click listeners have been implemented, we can display the dialog
        //Create the dialog
        AlertDialog alertDialog = builder.create();
        //Display the created dailog
        alertDialog.show();
    }

    /**
     * HELPER METHOD FOR ADDING A NEW TO_DO
     * returns a uri
     */
    private Uri addtodo() {
        //Get the data from Task textview
        TextView task_view = (TextView) findViewById(R.id.task_edit_field);
        String task = task_view.getText().toString().trim();

        //Get time from the global calender variable
        int minutes_after_midnight = Date_time_normalizer.getTimeAsMinutes(selecteddatetime);

        //Get date from global calendar variable
        int day_of_month = selecteddatetime.get(Calendar.DAY_OF_MONTH);
        int month = selecteddatetime.get(Calendar.MONTH);
        int year = selecteddatetime.get(Calendar.YEAR);

        //Construct the ContentValues object with the data
        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_TASK, task);
        values.put(TodoContract.TodoEntry.COLUMN_TIME, minutes_after_midnight);
        values.put(TodoContract.TodoEntry.COLUMN_DATE_DAYOFMONTH, day_of_month);
        values.put(TodoContract.TodoEntry.COLUMN_DATE_MONTH, month);
        values.put(TodoContract.TodoEntry.COLUMN_DATE_YEAR, year);
        Uri uri = getContentResolver().insert(TodoContract.TodoEntry.CONTENT_URI, values);
        return uri;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Calendar c = Calendar.getInstance();
        selecteddatetime = c;
    }

    //Inner Class for the time picker
    public static class Picker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            int hour = selecteddatetime.get(Calendar.HOUR_OF_DAY);
            int minute = selecteddatetime.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }


        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            //Initialise a calendar object with the user selected time
            selecteddatetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selecteddatetime.set(Calendar.MINUTE, minute);

            //update the time text view by calling the timeformatter helper method to format the time as per the needs
            time_textview.setText(Date_time_normalizer.timeFormatter(selecteddatetime));
        }

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Initialize a calendar object with the user selected date
            selecteddatetime.set(Calendar.DAY_OF_MONTH, day);
            selecteddatetime.set(Calendar.MONTH, month);
            selecteddatetime.set(Calendar.YEAR, year);

            //update the date text view by calling the dateformatter helper method to format the date as per the needs
            date_textview.setText(Date_time_normalizer.dateFormatter(selecteddatetime));

        }
    }
}


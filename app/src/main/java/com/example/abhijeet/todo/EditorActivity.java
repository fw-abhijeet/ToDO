package com.example.abhijeet.todo;

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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.abhijeet.todo.Data.TodoContract;

import java.util.Calendar;


public class EditorActivity extends AppCompatActivity {

    //Selected time by the user if not it is the current system time
    private static Calendar selecteddatetime;

    //Global field for the EditTime Field
    private static TextView time_textview;

    //Global Field for TaskEdit Field
    private static TextView task_textview;

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

        //Set on touch Listeners For all the fields available in the editor
        //To find out if the user has changed any data or not
        //Set the variable misaNewtask to true if any touch event occurs
        task_textview.setOnTouchListener(mTouchListener);
        time_textview.setOnTouchListener(mTouchListener);

        time_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picker timepicker = new Picker();
                timepicker.show(getFragmentManager(), "TimePicker");
            }
        });

        //Get the Current Time and use it as default display for time text view
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        //set the current time as selected time if user has not selected any time
        selecteddatetime = c;
        //Update the text view with the formatted Time
        time_textview.setText(Date_time_normalizer.timeFormatter(c));


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
                //Get the data from Task textview
                TextView task_view = (TextView) findViewById(R.id.task_edit_field);
                String task = task_view.getText().toString().trim();

                //get time from the global calender variable
                int minutes_after_midnight = Date_time_normalizer.getTimeAsMinutes(selecteddatetime);
                ContentValues values = new ContentValues();
                values.put(TodoContract.TodoEntry.COLUMN_TASK, task);
                values.put(TodoContract.TodoEntry.COLUMN_TIME, minutes_after_midnight);
                Uri uri = getContentResolver().insert(TodoContract.TodoEntry.CONTENT_URI, values);

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

    //Inner Class for the time picker
    public static class Picker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }


        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            //Initialise a calendar object with the user selected time
            selecteddatetime = Calendar.getInstance();
            selecteddatetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selecteddatetime.set(Calendar.MINUTE, minute);

            //update the time text view by calling the timeformatter helper method to format the time as per the needs
            time_textview.setText(Date_time_normalizer.timeFormatter(selecteddatetime));
        }

    }


}


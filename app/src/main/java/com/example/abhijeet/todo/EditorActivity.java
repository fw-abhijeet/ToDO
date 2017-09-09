package com.example.abhijeet.todo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
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

        //Return early if nothing has changed
        //   if (!misaNewTask) {
        //        return false;
        //   }

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
                //// TODO: 9/9/2017 implement confirm dialog box
        }
        return super.onOptionsItemSelected(item);
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


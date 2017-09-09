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
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.abhijeet.todo.Data.TodoContract;

import java.util.Calendar;


public class EditorActivity extends AppCompatActivity {

    //Selected time by the user if not it is the current system time
    private static Calendar selecteddatetime;
    //Global Variable for the EditTextTime Field
    private static TextView time_textview;

    //IMPORTANT: This variable needs to be updated everytime anything in the task info has changed,
    // else a lot of functionalities would break :(
    private boolean misaNewTask = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        time_textview = (TextView) findViewById(R.id.time_edit_field);

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
                int time = selecteddatetime.get(Calendar.MINUTE);
                ContentValues values = new ContentValues();
                values.put(TodoContract.TodoEntry.COLUMN_TASK, task);
                values.put(TodoContract.TodoEntry.COLUMN_TIME, time);
                Uri uri = getContentResolver().insert(TodoContract.TodoEntry.CONTENT_URI, values);

                //The Uri == null means that insertion is not successful
                if (uri == null) {
                    //// TODO: 9/9/2017 add this functionality
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


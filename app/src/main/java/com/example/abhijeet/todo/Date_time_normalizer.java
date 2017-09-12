package com.example.abhijeet.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.min;

/**
 * Created by Abhijeet on 9/9/2017.
 */

//HELPER CLASS
//The only function of this class is to normalize the date and time values which
// are stored in the database.
// Time is stored in databse as a INTEGER which represents the number of minutes past after midnight
//
//
public class Date_time_normalizer {
    //Makes no sense to initialize this class
    private Date_time_normalizer() {
    }

    ;

    /**
     * HELPER METHOD to get the time formatted properly
     *
     * @param {@Calender } reference for which the time formatting is to be done
     *                   Returns a String with the correctly Formatted Time
     */
    public static String timeFormatter(Calendar calReference) {
        String am_pm = "";

        if (calReference.get(Calendar.AM_PM) == Calendar.AM) //Ignore Error This code runs perfect
            am_pm = "AM";
        else if (calReference.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (calReference.get(Calendar.HOUR) == 0) ? "12" : calReference.get(Calendar.HOUR) + "";
        String formattedTime = strHrsToShow + ":" + calReference.get(Calendar.MINUTE) + " " + am_pm;

        return formattedTime;
    }

    /**
     * HELPER METHOD to get the time normalized
     * Because the time is stored as an integer variable in the database(minutes_after_midnight)
     *
     * @param {@Calender } Reference for which the normalizing is to be done
     * @return Returns a int with the correctly Formatted Time
     */

    public static int getTimeAsMinutes(Calendar calReference) {

        int hourOfDay = calReference.get(Calendar.HOUR_OF_DAY);
        int minute = calReference.get(Calendar.MINUTE);
        int minutes_after_midnight = (hourOfDay * 60) + minute;
        return minutes_after_midnight;
    }

    /**
     * HELPER METHOD to get the time normalized back to readable format
     * Because the time is stored as an integer variable in the database(minutes_after_midnight)
     *
     * @param minutes_after_midnight time output from the database
     * @return Returns a String with the correctly Formatted Time
     */
    public static String getTimeAsNormalized(int minutes_after_midnight) {
        Calendar c = Calendar.getInstance();
        int hourOfDay = minutes_after_midnight / 60;
        int minute = minutes_after_midnight % 60;
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        return timeFormatter(c);
    }

    public static String dateFormatter(Calendar calReference) {
        int month = calReference.get(Calendar.MONTH);
        int year = calReference.get(Calendar.YEAR);
        int day = calReference.get(Calendar.DAY_OF_MONTH);
        return "" + day + "-" + month + "-" + year;
    }
}

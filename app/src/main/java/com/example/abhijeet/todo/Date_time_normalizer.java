package com.example.abhijeet.todo;

import java.util.Calendar;

/**
 * Created by Abhijeet on 9/9/2017.
 */

//HELPER CLASS
//The only function of this class is to normalize the date and time values which
// are stored in the database.
//Suppose Time is stored as INTEGER as number of minutes past after midnight
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

    public static String timeNormalizer() {   //// TODO: 9/9/2017  IMPLEMENT time formatter to convert integer values stored in database to normal form
        return null;
    }
}

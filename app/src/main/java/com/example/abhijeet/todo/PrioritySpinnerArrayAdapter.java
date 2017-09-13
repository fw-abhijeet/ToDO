package com.example.abhijeet.todo;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

/**
 * Created by Abhijeet on 9/13/2017.
 */

public class PrioritySpinnerArrayAdapter extends ArrayAdapter {

    public PrioritySpinnerArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
}

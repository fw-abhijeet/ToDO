package com.example.abhijeet.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        EditText time_editField = (EditText) findViewById(R.id.time_editext_field);

        time_editField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picker timepicker = new Picker();
                timepicker.show(getFragmentManager(),"TimePicker");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.taskeditor_menu, menu);
        return true;
    }


}


package com.example.abhijeet.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.abhijeet.todo.Data.TodoContract;
import com.example.abhijeet.todo.Data.TodoDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Click listener for the FAB ADD button
        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.addFAB);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editor_intent = new Intent(MainActivity.this,EditorActivity.class);
                startActivity(editor_intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainscreen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_dummy_data:
                TodoDbHelper dbhelper = new TodoDbHelper(getBaseContext());
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TodoContract.TodoEntry.COLUMN_TASK, "Task 1 Time:");
                values.put(TodoContract.TodoEntry.COLUMN_TIME, 1);
                db.insert(TodoContract.TodoEntry.TABLE_NAME, null, values);
                values.clear();
                updatedata();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updatedata() {


        Cursor cursor = getContentResolver().query(TodoContract.TodoEntry.CONTENT_URI, null, null, null, null);
        TextView tv = (TextView) findViewById(R.id.Maintextview);
        tv.setText("");
        if (cursor == null)
            Log.e("ABHIJEET", "NULL EXCEPTION");
        while (cursor.moveToNext() != false) {
            String task = cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TASK));
            int time = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TIME));
            tv.append(task + "" + time + "\n");
        }

    }
}

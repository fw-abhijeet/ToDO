package com.example.abhijeet.todo;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.abhijeet.todo.Data.TodoContract;
import com.example.abhijeet.todo.Data.TodoDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the pet data loader
     */
    private static final int PET_LOADER = 0;

    //Gloabal variable for the cursor adaptor as it will be used several times, hah just to put it in plain english.
    private TodoCursorAdaptor mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Click listener for the FAB ADD button
        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.addFAB);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editor_intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(editor_intent);
            }
        });

        // Find the ListView which will be populated with the to-do data
        ListView listView = (ListView) findViewById(R.id.mainListView);


        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes, cause its a Async task) so pass in null for the Cursor.
        mCursorAdapter = new TodoCursorAdaptor(this, null);
        listView.setAdapter(mCursorAdapter);

        // Kick off the loader, as in start the loader. (Async task)
        getLoaderManager().initLoader(PET_LOADER, null, this);
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
                db.close();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                TodoContract.TodoEntry._ID,
                TodoContract.TodoEntry.COLUMN_TASK,
                TodoContract.TodoEntry.COLUMN_TIME};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,                 // Parent activity context
                TodoContract.TodoEntry.CONTENT_URI,   // Provider content URI to query
                projection,                           // Columns to include in the resulting Cursor
                null,                                 // No selection clause
                null,                                 // No selection arguments
                null);                                // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor newDataCursor) {
        //update the list View the new data after loading
        mCursorAdapter.swapCursor(newDataCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Callback called when the loader resets i.e. data needs to be deleted
        mCursorAdapter.swapCursor(null);

    }
}

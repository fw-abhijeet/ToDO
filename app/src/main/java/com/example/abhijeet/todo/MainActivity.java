package com.example.abhijeet.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
}

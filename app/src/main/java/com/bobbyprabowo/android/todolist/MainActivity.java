package com.bobbyprabowo.android.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bobbyprabowo.android.todolist.todolist.TodoActivity;

public class MainActivity extends AppCompatActivity {

    private Button todosButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todosButton = (Button) findViewById(R.id.button_todos);
        todosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TodoActivity.class);
                startActivity(i);
            }
        });
    }
}

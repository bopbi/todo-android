package com.bobbyprabowo.android.todolist.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.bobbyprabowo.android.todolist.R;
import com.bobbyprabowo.android.todolist.TodoApplication;
import com.bobbyprabowo.android.todolist.data.TodoRepository;
import com.bobbyprabowo.android.todolist.data.local.Constant;
import com.bobbyprabowo.android.todolist.data.local.TransactionManager;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class TodoActivity extends AppCompatActivity {

    private TodoView todoView;
    private TodoTimelineReceiver todoTimelineReceiver;
    private TodoRepository todoRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todos);

        todoView = (TodoView) findViewById(R.id.todo_view);

        todoRepository =  ((TodoApplication)getApplication()).getTodoRepository();

        todoTimelineReceiver = new TodoTimelineReceiver();
        todoView.setTodoRepository(todoRepository);
    }

    @Override
    protected void onStart() {
        super.onStart();
        todoView.start();

        Toast.makeText(this, "Please Pull to Refresh", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        todoView.setRefreshing(TransactionManager.getTimelineTransactionStatus(this));

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(todoTimelineReceiver, new IntentFilter(Constant.INTENT_UPDATE_TIMELINE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(todoTimelineReceiver);
    }

    class TodoTimelineReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            todoView.start();
            todoView.setRefreshing(false);
            Log.i("TODO", "OK RECEIVED");
        }
    }
}

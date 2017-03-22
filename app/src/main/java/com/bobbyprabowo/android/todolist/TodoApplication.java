package com.bobbyprabowo.android.todolist;

import android.app.Application;

import com.bobbyprabowo.android.todolist.data.TodoRepository;
import com.bobbyprabowo.android.todolist.data.local.TodoLocalDataSource;
import com.bobbyprabowo.android.todolist.data.remote.TodoRemoteDataSource;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class TodoApplication extends Application {

    private TodoLocalDataSource todoLocalDataSource;
    private TodoRemoteDataSource todoRemoteDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        todoLocalDataSource = TodoLocalDataSource.getInstance(this);
        todoRemoteDataSource = TodoRemoteDataSource.getInstance(this);
    }

    public TodoRepository getTodoRepository() {
        return TodoRepository.getInstance(todoLocalDataSource, todoRemoteDataSource);
    }

}

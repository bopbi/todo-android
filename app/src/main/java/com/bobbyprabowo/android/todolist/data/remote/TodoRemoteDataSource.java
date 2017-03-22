package com.bobbyprabowo.android.todolist.data.remote;

import android.content.Context;

import com.bobbyprabowo.android.todolist.data.Todo;
import com.bobbyprabowo.android.todolist.data.TodoDataSource;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class TodoRemoteDataSource implements TodoDataSource {

    private static TodoRemoteDataSource INSTANCE;
    private TodoNetwork todoNetwork;

    private TodoRemoteDataSource(Context context) {
        todoNetwork = TodoNetwork.getInstance(context);
    }

    public static TodoRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TodoRemoteDataSource(context);
        }
        return INSTANCE;
    }
    @Override
    public Observable<List<Todo>> getAllTodos(boolean refresh) {

        return todoNetwork
                .getTodoService()
                .getTodos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<TodoDTO>>>() {
                    @Override
                    public Observable<? extends List<TodoDTO>> call(Throwable throwable) {
                        return null;
                    }
                })
                .map(new Func1<List<TodoDTO>, List<Todo>>() {

                    @Override
                    public List<Todo> call(List<TodoDTO> todoDTOList) {
                        List<Todo> todos = new ArrayList<>(todoDTOList.size());
                        for (TodoDTO todoDTO:
                                todoDTOList) {
                            Todo todo = new Todo(todoDTO);
                            todos.add(todo);
                        }
                        return todos;
                    }
                });
    }

    @Override
    public Observable<List<Todo>> addTodos(List<Todo> todos) {
        return null;
    }
}

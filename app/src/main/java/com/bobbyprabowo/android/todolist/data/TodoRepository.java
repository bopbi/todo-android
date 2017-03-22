package com.bobbyprabowo.android.todolist.data;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class TodoRepository implements TodoDataSource {

    private static TodoRepository INSTANCE = null;

    private TodoDataSource localTodoDataSource;
    private TodoDataSource remoteTodoDataSource;

    private TodoRepository(TodoDataSource localTodoDataSource, TodoDataSource remoteTodoDataSource) {
        this.localTodoDataSource = localTodoDataSource;
        this.remoteTodoDataSource = remoteTodoDataSource;
    }

    public static TodoRepository getInstance(TodoDataSource localTodoDataSource, TodoDataSource remoteTodoDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TodoRepository(localTodoDataSource, remoteTodoDataSource);
        }

        return INSTANCE;
    }
    @Override
    public Observable<List<Todo>> getAllTodos(final boolean refresh) {
        if (refresh) {
            return remoteTodoDataSource
                    .getAllTodos(refresh)
                    .switchMap(new Func1<List<Todo>, Observable<List<Todo>>>() {
                        @Override
                        public Observable<List<Todo>> call(List<Todo> spiks) {
                            return localTodoDataSource.addTodos(spiks);
                        }
                    })
                    .switchMap(new Func1<List<Todo>, Observable<List<Todo>>>() {
                        @Override
                        public Observable<List<Todo>> call(List<Todo> spiks) {
                            return localTodoDataSource.getAllTodos(refresh);
                        }
                    });
        } else {
            return localTodoDataSource.getAllTodos(refresh);
        }
    }

    @Override
    public Observable<List<Todo>> addTodos(List<Todo> todos) {
        return null;
    }
}

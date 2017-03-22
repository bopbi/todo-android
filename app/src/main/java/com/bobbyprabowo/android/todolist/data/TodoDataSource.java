package com.bobbyprabowo.android.todolist.data;

import java.util.List;

import rx.Observable;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public interface TodoDataSource {

    Observable<List<Todo>> getAllTodos(boolean refresh);

    Observable<List<Todo>> addTodos(List<Todo> todos);

}

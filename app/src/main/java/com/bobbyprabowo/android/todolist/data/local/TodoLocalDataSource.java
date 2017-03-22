package com.bobbyprabowo.android.todolist.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.bobbyprabowo.android.todolist.TodoProvider;
import com.bobbyprabowo.android.todolist.data.Todo;
import com.bobbyprabowo.android.todolist.data.TodoDB;
import com.bobbyprabowo.android.todolist.data.TodoDataSource;
import com.bobbyprabowo.android.todolist.data.Utils;
import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class TodoLocalDataSource implements TodoDataSource {

    private static TodoLocalDataSource INSTANCE = null;
    private Context context;
    private SqlBrite sqlBrite;

    private TodoLocalDataSource(Context context) {
        this.context = context;
        sqlBrite = new SqlBrite.Builder().build();
    }

    public static TodoLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TodoLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Todo>> getAllTodos(boolean refresh) {
        BriteContentResolver resolver = sqlBrite.wrapContentProvider(context.getContentResolver(), Schedulers.io());
        return resolver
                .createQuery(Uri.parse(TodoProvider.SPIKS_URI), null, null, null, null, false)
                .map(new Func1<SqlBrite.Query, List<Todo>>() {

                    @Override
                    public List<Todo> call(SqlBrite.Query query) {
                        Cursor cursor = query.run();

                        if (cursor == null) {
                            return null;
                        }

                        List<Todo> todoList = null;
                        if (cursor.moveToFirst()) {
                            int colId = cursor.getColumnIndex(TodoDB.COLUMN_TODO_ID);
                            int colUserId = cursor.getColumnIndex(TodoDB.COLUMN_TODO_USER_ID);
                            int colRemoteId = cursor.getColumnIndex(TodoDB.COLUMN_TODO_REMOTE_ID);

                            int colTitle = cursor.getColumnIndex(TodoDB.COLUMN_TODO_TITLE);
                            int colCompleted = cursor.getColumnIndex(TodoDB.COLUMN_TODO_COMPLETED);
                            todoList = new ArrayList<>(cursor.getCount());

                            // first cursor
                            Todo todo = new Todo();
                            todo.setId(cursor.getInt(colId));
                            todo.setUserId(cursor.getInt(colUserId));
                            todo.setTitle(cursor.getString(colTitle));
                            todo.setCompleted(Utils.BooleanFromInt(cursor.getInt(colCompleted)));
                            todo.setRemoteId(cursor.getInt(colRemoteId));
                            todoList.add(todo);

                            // others
                            while (cursor.moveToNext()) {
                                Todo mTodo = new Todo();
                                mTodo.setUserId(cursor.getInt(colUserId));
                                mTodo.setId(cursor.getInt(colId));
                                mTodo.setRemoteId(cursor.getInt(colRemoteId));
                                mTodo.setTitle(cursor.getString(colTitle));
                                mTodo.setCompleted(Utils.BooleanFromInt(cursor.getInt(colCompleted)));
                                todoList.add(mTodo);
                            }
                        }
                        cursor.close();
                        return todoList;
                    }
                });
    }

    @Override
    public Observable<List<Todo>> addTodos(final List<Todo> todos) {
        return Observable.fromCallable(new Callable<List<Todo>>() {
            @Override
            public List<Todo> call() throws Exception {

                // clean all first
                context.getContentResolver().delete(Uri.parse(TodoProvider.SPIKS_URI), null, null);

                List<ContentValues> contentValues = new ArrayList<>(todos.size());

                for (Todo todo : todos) {
                    ContentValues cv = new ContentValues();
                    cv.put(TodoDB.COLUMN_TODO_USER_ID, todo.getUserId());
                    cv.put(TodoDB.COLUMN_TODO_REMOTE_ID, todo.getRemoteId());
                    cv.put(TodoDB.COLUMN_TODO_TITLE, todo.getTitle());
                    cv.put(TodoDB.COLUMN_TODO_COMPLETED, Utils.IntFromBoolean(todo.isCompleted()));
                    contentValues.add(cv);
                }
                context.getContentResolver().bulkInsert(Uri.parse(TodoProvider.SPIKS_URI), contentValues.toArray(new ContentValues[todos.size()]));

                return todos;
            }
        });
    }
}

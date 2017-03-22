package com.bobbyprabowo.android.todolist.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class TodoDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todos.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TODOS = "todos";

    public static final String COLUMN_TODO_ID = "_id";
    public static final String COLUMN_TODO_USER_ID = "userId";
    public static final String COLUMN_TODO_REMOTE_ID = "id";
    public static final String COLUMN_TODO_TITLE = "title";
    public static final String COLUMN_TODO_COMPLETED = "completed";

    private static final String CREATE_TODO = "create table "
            + TABLE_TODOS + "( " + COLUMN_TODO_ID  + " integer primary key autoincrement, "
            + "" + COLUMN_TODO_USER_ID + " integer not null, "
            + "" + COLUMN_TODO_REMOTE_ID + " integer not null, "
            + "" + COLUMN_TODO_TITLE + " text not null, "
            + "" + COLUMN_TODO_COMPLETED + " integer not null "
            + ");";

    public TodoDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, new DatabaseErrorHandler() {
            @Override
            public void onCorruption(SQLiteDatabase db) {

            }
        });
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

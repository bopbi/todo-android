package com.bobbyprabowo.android.todolist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bobbyprabowo.android.todolist.data.TodoDB;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class TodoProvider extends ContentProvider {

    private TodoDB todoDB;

    private static final int TODOS = 10;

    public static final String AUTHORITY = "com.bobbyprabowo.android.todolist.contentprovider";

    private static final String BASE_URI = "content://"+ AUTHORITY + "/";
    public static final String TODOS_PATH = "todos";
    public static final String TODO_URI = BASE_URI + TODOS_PATH;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, TODOS_PATH , TODOS);
    }

    @Override
    public boolean onCreate() {
        todoDB = new TodoDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase;
        Cursor cursor = null;
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case TODOS:
                sqLiteDatabase = todoDB.getReadableDatabase();
                String query = "SELECT * FROM "+ TodoDB.TABLE_TODOS + " ORDER BY _id DESC";
                cursor = sqLiteDatabase.rawQuery(query, null);
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        SQLiteDatabase sqLiteDatabase;
        int count = 0;
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case TODOS:
                sqLiteDatabase = todoDB.getWritableDatabase();
                sqLiteDatabase.beginTransaction();
                for (ContentValues contentValue : values) {
                    long newId = sqLiteDatabase.insert(TodoDB.TABLE_TODOS, null, contentValue);
                    if ( newId > 0) {
                        count++;
                    }
                }
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
      SQLiteDatabase sqLiteDatabase;
      int count = 0;
      int uriType = uriMatcher.match(uri);
      switch (uriType) {
          case TODOS:
              sqLiteDatabase = todoDB.getWritableDatabase();
              sqLiteDatabase.beginTransaction();
              count = sqLiteDatabase.delete(TodoDB.TABLE_TODOS, null, null);
              sqLiteDatabase.endTransaction();
              break;
      }
      return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

package com.bobbyprabowo.android.todolist.todolist;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bobbyprabowo.android.todolist.TodoApplication;
import com.bobbyprabowo.android.todolist.data.Todo;
import com.bobbyprabowo.android.todolist.data.TodoRepository;
import com.bobbyprabowo.android.todolist.data.local.Constant;
import com.bobbyprabowo.android.todolist.data.local.TransactionManager;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class TodoFetchService extends Service {

    private TodoRepository todoRepository;
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        todoRepository = ((TodoApplication) getApplication()).getTodoRepository();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (!isRunning) {
            isRunning = true;
            fetchTimeline();
        }
        return Service.START_STICKY;
    }

    private void fetchTimeline() {
        TransactionManager.saveTimelineTransactionStatus(TodoFetchService.this, true);
        final Intent i = new Intent(Constant.INTENT_UPDATE_TIMELINE);

        LocalBroadcastManager.getInstance(TodoFetchService.this).sendBroadcast(i);

        todoRepository
                .getAllTodos(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Todo>>() {
                    @Override
                    public void onCompleted() {
                        TransactionManager.saveTimelineTransactionStatus(TodoFetchService.this, false);
                        LocalBroadcastManager.getInstance(TodoFetchService.this).sendBroadcast(i);
                        isRunning = false;
                        stopSelf();
                    }

                    @Override
                    public void onError(Throwable e) {
                        TransactionManager.saveTimelineTransactionStatus(TodoFetchService.this, false);
                        LocalBroadcastManager.getInstance(TodoFetchService.this).sendBroadcast(i);
                        isRunning = false;
                        stopSelf();
                    }

                    @Override
                    public void onNext(List<Todo> todos) {
                        TransactionManager.saveTimelineTransactionStatus(TodoFetchService.this, false);

                        LocalBroadcastManager.getInstance(TodoFetchService.this).sendBroadcast(i);
                        Log.i("TODO", "OK");
                    }
                });
    }
}

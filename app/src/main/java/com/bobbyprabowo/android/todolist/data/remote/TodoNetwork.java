package com.bobbyprabowo.android.todolist.data.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class TodoNetwork {

    private TodoService todoService;

    private TodoNetwork() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        todoService = retrofit.create(TodoService.class);
    }

    private static TodoNetwork todoNetwork;

    public static TodoNetwork getInstance() {
        if (todoNetwork == null) {
            todoNetwork = new TodoNetwork();
        }
        return todoNetwork;
    }

    public TodoService getTodoService() {
        return todoService;
    }


    public interface TodoService {
        @GET("todos")
        Observable<List<TodoDTO>> getTodos();

    }
}

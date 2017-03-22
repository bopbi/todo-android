package com.bobbyprabowo.android.todolist.data;

import com.bobbyprabowo.android.todolist.data.remote.TodoDTO;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class Todo {

    private long userId;

    private long id;

    private long remoteId;

    private String title;

    private boolean completed;

    public Todo() {

    }

    public Todo(TodoDTO todoDTO) {
        userId = todoDTO.getUserId();
        remoteId = todoDTO.getId();
        title = todoDTO.getTitle();
        completed = todoDTO.isCompleted();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

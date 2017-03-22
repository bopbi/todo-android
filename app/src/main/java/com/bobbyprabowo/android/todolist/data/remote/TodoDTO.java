package com.bobbyprabowo.android.todolist.data.remote;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class TodoDTO {

    private long userId;

    private long id;

    private String title;

    private boolean completed;

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

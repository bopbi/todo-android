package com.bobbyprabowo.android.todolist.todolist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobbyprabowo.android.todolist.R;
import com.bobbyprabowo.android.todolist.data.Todo;
import com.bobbyprabowo.android.todolist.data.TodoRepository;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class TodoView extends SwipeRefreshLayout {

    private TodoAdapter todoAdapter;
    private List<Todo> todoList;
    private RecyclerView recyclerView;

    public void setTodoRepository(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    private TodoRepository todoRepository;

    public TodoView(Context context) {
        super(context);
    }

    public TodoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        init();
    }

    void start() {
        todoRepository.getAllTodos(false) // get all
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Todo>>() {
                    @Override
                    public void onCompleted() {
                        todoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        todoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNext(List<Todo> todos) {
                        todoList = todos;
                        todoAdapter.notifyDataSetChanged();
                        Log.i("TODO", "TODO SIZE "+ todos.size());
                    }
                });
    }

    void init() {
        recyclerView = (RecyclerView) findViewById(R.id.todos_recyclerview);
        todoAdapter = new TodoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(todoAdapter);
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefreshing(true);

                Intent fetchIntent = new Intent(getContext(), TodoFetchService.class);
                getContext().startService(fetchIntent);
            }
        });
    }

    class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

        @Override
        public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // refer https://www.bignerdranch.com/blog/understanding-androids-layoutinflater-inflate/
            View v = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
            return new TodoViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TodoViewHolder holder, int position) {
            Todo todo = todoList.get(position);

            Log.i("TODO", "DRAW");
            holder.todoTitle.setText(todo.getTitle());
            holder.todoCompleted.setText("Complete : " + todo.isCompleted());

        }

        @Override
        public int getItemCount() {
            if (todoList == null) {
                return 0;
            }
            return todoList.size();
        }
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {

        TextView todoTitle;
        TextView todoCompleted;

        public TodoViewHolder(View itemView) {
            super(itemView);

            todoTitle = (TextView) itemView.findViewById(R.id.item_todo_textview_title);
            todoCompleted = (TextView) itemView.findViewById(R.id.item_todo_textview_completed);

        }
    }
}

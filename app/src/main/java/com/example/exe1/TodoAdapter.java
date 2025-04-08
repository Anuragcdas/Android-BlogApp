package com.example.exe1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {

    private Context context;
    private List<Todo> todoList;

    public TodoAdapter(Context context, List<Todo> todoList) {
        this.context = context;
        this.todoList = todoList;

    }

    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        return new TodoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoHolder holder, int position) {

        Todo todo = todoList.get(position);

        holder.TodoTitle.setText(todo.getTitle().toUpperCase());
        holder.TodoTask.setText(todo.isCompleted() ? " ✔ Completed" : "Pending");

        holder.Todocard.setCardBackgroundColor(todo.isCompleted() ?

                ContextCompat.getColor(context, android.R.color.holo_green_light) :

                ContextCompat.getColor(context, android.R.color.holo_red_light));


    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class TodoHolder extends RecyclerView.ViewHolder {

        TextView TodoTitle, TodoTask;
        CardView Todocard;

        public TodoHolder(@NonNull View itemView) {
            super(itemView);

            TodoTitle = itemView.findViewById(R.id.todotitle);
            TodoTask = itemView.findViewById(R.id.textstatus);
            Todocard = itemView.findViewById(R.id.todocardview);

        }
    }
}

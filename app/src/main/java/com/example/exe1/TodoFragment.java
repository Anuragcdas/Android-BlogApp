package com.example.exe1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TodoFragment extends Fragment {


    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private List<Todo> todoList;
    private ProgressBar progressBar;
    private FloatingActionButton fabbutton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTodos);
        progressBar = view.findViewById(R.id.progressbartodo);
        fabbutton = view.findViewById(R.id.fabButtonTodo);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        todoList = new ArrayList<>();
        todoAdapter = new TodoAdapter(getContext(), todoList);
        recyclerView.setAdapter(todoAdapter);


        loadTodos();

        fabbutton.setOnClickListener(V -> startActivity(new Intent(getContext(), AddTodoActivity.class)));


        return view;


    }

    private void loadTodos() {
        progressBar.setVisibility(View.VISIBLE);

        String url = AppConstants.API_TODO;


        RequestQueue requestQueue;

        requestQueue = Volley.newRequestQueue(requireContext());


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,

                response -> {

                    progressBar.setVisibility(View.GONE);


                    try {
                        for (int i = 0; i < response.length(); i++) {


                            JSONObject obj = response.getJSONObject(i);
                            int id = obj.getInt("id");
                            String title = obj.getString("title");
                            Boolean task = obj.getBoolean("completed");

                            todoList.add(new Todo(id, title, task));    // Don't make error tin this stage


                        }

                        todoAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                },
                error ->
                {
                    Toast.makeText(getContext(), "Error in response from Todo API ", Toast.LENGTH_SHORT).show();

                }


        );

        requestQueue.add(jsonArrayRequest);


    }


}
package com.example.exe1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity {

    private RecyclerView rvPosts, rvAlbums, rvTodos;
    private int userId;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_detail);

        // UI Components
        TextView tvUserId = findViewById(R.id.tvUserID);
        rvPosts = findViewById(R.id.rvPosts);
        rvAlbums = findViewById(R.id.rvAlbums);
        rvTodos = findViewById(R.id.rvTodos);

        // Get User ID from Intent
        userId = getIntent().getIntExtra("user_Id", -1);
        tvUserId.setText("User ID: " + userId);

        // Initialize RecyclerViews
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvAlbums.setLayoutManager(new LinearLayoutManager(this));
        rvTodos.setLayoutManager(new LinearLayoutManager(this));


        // Initialize RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Load Data
        loadPosts(userId);
        loadALbum(userId);

        loadTodo(userId);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void loadPosts(int userId) {
        String url = AppConstants.API_POST + "?userId=" + userId;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Post> postList = new ArrayList<>();

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject postObject = response.getJSONObject(i);

                            int userID = postObject.getInt("userId");
                            int postId = postObject.getInt("id");  // Corrected: post ID

                            String title = postObject.getString("title");
                            String body = postObject.getString("body");

                            Log.d("debug", "postId" + postId + "userId" + userID);

                            Post post = new Post(postId, userID, title, body);
                            postList.add(post);
                            // Set adapter for RecyclerView


                        }
                        Log.d("postList", "loadPosts: " + postList.size());


                        PostAdapter postAdapter = new PostAdapter(postList, UserDetailActivity.this, post -> {
                            Intent intent = new Intent(UserDetailActivity.this, PostDetailActivity.class);
                            Log.d("postId", "loadPosts: " + post.getId());
                            intent.putExtra("postId", post.getId());  // Correctly passing post ID
                            startActivity(intent);
                        });

                        rvPosts.setAdapter(postAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(UserDetailActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(UserDetailActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonArrayRequest);
    }


    public void loadALbum(int userId) {


        String url = AppConstants.API_AlBUM + "?userId=" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {

                    List<Album> albumList = new ArrayList<>();

                    try {


                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            int ID = obj.getInt("id");

                            String title = obj.getString("title");

                            albumList.add(new Album(ID, title));
                        }


                        AlbumAdapter albumAdapter = new AlbumAdapter(UserDetailActivity.this, albumList, album -> {

                            Intent intent = new Intent(UserDetailActivity.this, PhotoActivity.class);
                            intent.putExtra("album_id", album.getId());
                            // startActivity(intent);
                        });

                        rvAlbums.setAdapter(albumAdapter);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                },
                error -> {

                    Toast.makeText(this, "Error in response from user", Toast.LENGTH_SHORT).show();


                }

        );

        requestQueue.add(jsonArrayRequest);


    }


    private void loadTodo(int userId) {


        String url = AppConstants.API_TODO + "?userId=" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {

                    try {

                        List<Todo> todoList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            int id = obj.getInt("id");
                            String title = obj.getString("title");
                            boolean task = obj.getBoolean("completed");


                            todoList.add(new Todo(id, title, task));


                        }

                        TodoAdapter todoAdapter = new TodoAdapter(UserDetailActivity.this, todoList);

                        rvTodos.setAdapter(todoAdapter);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }
                , error -> {
            Toast.makeText(this, "Error in getting response from Todo url", Toast.LENGTH_SHORT).show();


        }
        );


        requestQueue.add(jsonArrayRequest);


    }


}

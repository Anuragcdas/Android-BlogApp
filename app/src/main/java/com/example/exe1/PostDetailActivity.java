package com.example.exe1;

import static com.example.exe1.AppConstants.showToastMessage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private TextView tvPostTitle, tvPostBody, tvUserInfo;
    private ProgressBar progressBar;
    private EditText etCommentName, etCommentEmail, etCommentBody;
    private List<Comment> commentList = new ArrayList<>();
    private int postId, userId;
    private CommentAdapter commentAdapter;
    private RequestQueue requestQueue;

    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        root=findViewById(R.id.main);

        // Initialize UI components
        tvPostTitle = findViewById(R.id.tvPostTitle);
        tvPostBody = findViewById(R.id.tvPostBody);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        etCommentName = findViewById(R.id.etCommentName);
        etCommentEmail = findViewById(R.id.etCommentEmail);
        etCommentBody = findViewById(R.id.etCommentBody);
        Button addCommentButton = findViewById(R.id.AddCommentButton);
        progressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerViewComments = findViewById(R.id.recyclerViewComments);


        Toolbar toolbar;

        toolbar=findViewById(R.id.toolbar);


        AppConstants.OnCallBack(this,toolbar,"Post Details");



        // Setup RecyclerView
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(commentList);
        recyclerViewComments.setAdapter(commentAdapter);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);


        //geting post Id from Home Fragment


        postId = getIntent().getIntExtra("postId", -1);


        if (postId != -1) {
            fetchPostDetails(postId);
            fetchComments(postId);
        } else {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        addCommentButton.setOnClickListener(view -> addComment());

        // Adjust layout for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchPostDetails(int postId) {
        progressBar.setVisibility(View.VISIBLE);
        String url = AppConstants.API_POST + postId;

        Log.d("DEBUG", "Fetching post details from: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        tvPostTitle.setText(response.getString("title").toUpperCase());
                        String body = response.getString("body");
                        String capitalizedBody = body.substring(0, 1).toUpperCase() + body.substring(1);
                        tvPostBody.setText(capitalizedBody);
                        userId = response.getInt("userId");
                        fetchUserDetails(userId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    showToastMessage(this, "Error fetching post details", Toast.LENGTH_SHORT);
                    Toast.makeText(PostDetailActivity.this, "Error fetching post details", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }

    private void fetchUserDetails(int userId) {
        String url = "https://jsonplaceholder.typicode.com/users/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        String name = response.getString("name");
                        String email = response.getString("email");
                        tvUserInfo.setText("By " + name + " | Email: " + email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }

    private void fetchComments(int postId) {
        String url = "https://jsonplaceholder.typicode.com/posts/" + postId + "/comments";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    commentList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Comment comment = new Comment(
                                    obj.getString("name"),
                                    obj.getString("email"),
                                    obj.getString("body")
                            );
                            commentList.add(comment);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    commentAdapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Error fetching comments", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(request);
    }

    private void addComment() {
        String url = "https://jsonplaceholder.typicode.com/comments";

        String name = etCommentName.getText().toString().trim();
        String email = etCommentEmail.getText().toString().trim();
        String body = etCommentBody.getText().toString().trim();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("postId", postId);
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (name.isEmpty() || email.isEmpty() || body.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        } else {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    response -> {
                        Toast.makeText(this, "Comment Added Successfully", Toast.LENGTH_SHORT).show();
                        etCommentBody.setText("");
                        etCommentEmail.setText("");
                        etCommentName.setText("");
                        fetchComments(postId);
                    },
                    error ->
                    {
                        NetworkUtils.handleVolleyError(this,error,root,()->
                        {
                            addComment();
                        });
                    }
            );

            requestQueue.add(request);


        }


    }
}

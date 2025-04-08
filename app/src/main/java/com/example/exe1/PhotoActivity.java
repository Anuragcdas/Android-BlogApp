package com.example.exe1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;



import androidx.activity.EdgeToEdge;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPhotos;
    private PhotoAdapter photoAdapter;
    private List<Photo> photoList;
    private ProgressBar progressBar;
    private int albumId;
    private  View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_photo);

        rootView = findViewById(android.R.id.content);



        progressBar=findViewById(R.id.progressbarphoto);


        Toolbar toolbar= findViewById(R.id.toolbarphoto);

        AppConstants.OnCallBack(this,toolbar,"Photos");


        recyclerViewPhotos = findViewById(R.id.recyclerViewPhotos);
        recyclerViewPhotos.setLayoutManager( new LinearLayoutManager(this));

        photoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(this, photoList, photo -> {

            Intent intent = new Intent(PhotoActivity.this, FullScreenPhotoActivity.class);
            intent.putExtra("photo_url", photo.getUrl());
            startActivity(intent);
        });

        recyclerViewPhotos.setAdapter(photoAdapter);

         albumId = getIntent().getIntExtra("album_id", 0);
        fetchPhotos(albumId);


        //////////////////////////////////
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchPhotos(int albumId) {

        progressBar.setVisibility(View.VISIBLE);

        String url = AppConstants.API_PHOTO + "?albumId=" + albumId;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null

                , response -> {
            try {

                progressBar.setVisibility(View.GONE);

                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);

                    String title = obj.getString("title");

                    Log.d("", "fetchPhotos: " + title);
                    String thumbnailUrl = obj.getString("url");

                    Log.d("", "fetchPhotos: " + thumbnailUrl);
                    String urlPhoto = obj.getString("thumbnailUrl");

                    Log.d("", "fetchPhotos: " + title);

                    photoList.add(new Photo(title, urlPhoto, thumbnailUrl));

                }

                photoAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        },
                error -> {

                      NetworkUtils.handleVolleyError(this,error, rootView,()->
                      {
                          if(NetworkUtils.isNetworkError(error)) {
                              fetchPhotos(albumId);
                          }
                          else {
                              Toast.makeText(this, "Error in fetching data from API", Toast.LENGTH_SHORT).show();

                          }
                      });

                }


        );

        requestQueue.add(jsonArrayRequest);


    }
}
package com.example.exe1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private FloatingActionButton fabAddPost;
    private ProgressBar progressBar;
    private BottomNavigationView bottomNavigationView;
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components

        SearchView searchView = root.findViewById(R.id.searchView);


        recyclerView = root.findViewById(R.id.recyclerView);
        progressBar = root.findViewById(R.id.progressBar);
        fabAddPost = root.findViewById(R.id.fabAddPost);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        // Floating Action Button to add a new post
        fabAddPost.setOnClickListener(v -> startActivity(new Intent(getContext(), AddActivity.class)));

        // Load posts from API
        loadPosts();

        //

        bottomNavigationView = getActivity().findViewById(R.id.navagation_bar);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {



            @Override
            public boolean onQueryTextSubmit(String query) {



                if (postAdapter != null) {
                    postAdapter.filter(query);

                }
                bottomNavigationView.setVisibility(View.GONE);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (postAdapter != null) {
                    postAdapter.filter(newText);
                }

                if (newText.isEmpty()) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                } else {
                    bottomNavigationView.setVisibility(View.GONE);
                }
                return false;
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bottomNavigationView != null) {

            bottomNavigationView.setVisibility(View.VISIBLE);
        }

    }

    public void loadPosts() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Post>> call = apiService.getPost();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> postList = response.body();
                    progressBar.setMax(postList.size());

                    // Simulating progress update
                    new Thread(() -> {
                        for (int i = 1; i <= postList.size(); i++) {
                            try {
                                Thread.sleep(2); // Small delay to show progress effect
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // Ensure fragment is attached before updating UI
                            if (isAdded() && getActivity() != null) {
                                int progress = i;
                                getActivity().runOnUiThread(() -> progressBar.setProgress(progress));
                            }
                        }

                        // Hide progress bar safely
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                        }
                    }).start();


                    // Set adapter for RecyclerView
                    postAdapter = new PostAdapter(postList, getContext(), post -> {
                        Intent intent = new Intent(getContext(), PostDetailActivity.class);
                        Log.d("postId", "" + post.getId());
                        intent.putExtra("postId", post.getId());  // Pass the post ID
                        startActivity(intent);


                    });

                    recyclerView.setAdapter(postAdapter);

                } else {
                    progressBar.setVisibility(View.GONE);
                    NetworkUtils.showErrorToast(getContext(),"Error in Loading Post");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

                 NetworkUtils.handleRetrofitError(requireContext(),t,root,HomeFragment.this::loadPosts);
            }
        });
    }
}

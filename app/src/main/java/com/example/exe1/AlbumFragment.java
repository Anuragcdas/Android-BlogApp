package com.example.exe1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.exe1.databinding.FragmentAlbumBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AlbumFragment extends Fragment {


    private RecyclerView recyclerViewAlbums;
    private AlbumAdapter albumAdapter;
    private List<Album> albumList;
    private RequestQueue requestQueue;
    private FragmentAlbumBinding binding;

    public AlbumFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album, container, false);

        recyclerViewAlbums = view.findViewById(R.id.recyclerViewAlbums);

        recyclerViewAlbums.setLayoutManager(new LinearLayoutManager(getContext()));

        albumList = new ArrayList<>();
        albumAdapter = new AlbumAdapter(getContext(), albumList, album -> {
            Intent intent = new Intent(getContext(), PhotoActivity.class);
            intent.putExtra("album_id", album.getId());
            startActivity(intent);
        });

        requestQueue = Volley.newRequestQueue(requireContext());
        recyclerViewAlbums.setAdapter(albumAdapter);


        fetchAlbums();


        return view;

    }


    private void fetchAlbums() {

        String url = AppConstants.API_AlBUM;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,

                response ->
                {

                    try {
                        albumList.clear();

                        for (int i = 0; i < response.length(); i++) {

                            JSONObject obj = response.getJSONObject(i);
                            int ID = obj.getInt("id");
                            Log.d(" ", "fetchAlbums: " + ID);
                            String title = obj.getString("title");

                            Log.d("title", "fetchAlbums: " + title);
                            albumList.add(new Album(ID, title));


                        }

                        albumAdapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                },


                error ->
                {

                    Toast.makeText(getContext(), "Errrot in response", Toast.LENGTH_SHORT).show();

                }


        );


        requestQueue.add(jsonArrayRequest);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
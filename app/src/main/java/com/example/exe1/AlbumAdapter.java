package com.example.exe1;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {


    private List<Album> albumList;
    private OnAlbumClickListener listener;
    private Context context;

    public AlbumAdapter(Context context, List<Album> albumList, OnAlbumClickListener listener) {
        this.context = context;
        this.albumList = albumList;
        this.listener = listener;

    }

    @NonNull
    @Override

    public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        return new AlbumHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
        Album album = albumList.get(position);
      //  holder.AlbumId.setText(String.valueOf(album.getId() ));

        int x= album.getId();
        holder.Albumtile.setText(HtmlCompat.fromHtml("<b>" + album.getId() + ":</b> " + album.getTitle().toUpperCase(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.itemView.setOnClickListener(V -> listener.OnAlbumCLick(album));

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public interface OnAlbumClickListener {

        void OnAlbumCLick(Album album);
    }

    public class AlbumHolder extends RecyclerView.ViewHolder {

        TextView AlbumId, Albumtile;

        public AlbumHolder(@NonNull View itemView) {
            super(itemView);

           // AlbumId = itemView.findViewById(R.id.tvAlbumId);
            Albumtile = itemView.findViewById(R.id.tvAlbumTItle);
        }
    }

}


